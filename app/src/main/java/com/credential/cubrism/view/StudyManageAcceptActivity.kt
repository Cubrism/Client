package com.credential.cubrism.view

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.credential.cubrism.databinding.ActivityStudygroupManageacceptBinding
import com.credential.cubrism.model.dto.StudyGroupJoinReceiveListDto
import com.credential.cubrism.model.repository.StudyGroupRepository
import com.credential.cubrism.view.adapter.GroupAcceptButtonClickListener
import com.credential.cubrism.view.adapter.GroupDenyButtonClickListener
import com.credential.cubrism.view.adapter.JoinAcceptAdapter
import com.credential.cubrism.view.utils.ItemDecoratorDivider
import com.credential.cubrism.viewmodel.StudyGroupViewModel
import com.credential.cubrism.viewmodel.ViewModelFactory

class StudyManageAcceptActivity : AppCompatActivity(), GroupAcceptButtonClickListener, GroupDenyButtonClickListener {
    private val binding by lazy { ActivityStudygroupManageacceptBinding.inflate(layoutInflater) }

    private val studyGroupViewModel: StudyGroupViewModel by viewModels { ViewModelFactory(StudyGroupRepository()) }

    private lateinit var joinAcceptAdapter: JoinAcceptAdapter

    private val groupId by lazy { intent.getIntExtra("groupId", -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        observeViewModel()

        if (groupId != -1) {
            studyGroupViewModel.getStudyGroupJoinReceiveList(groupId)
        }
    }

    override fun onAcceptButtonClick(item: StudyGroupJoinReceiveListDto) {
        AlertDialog.Builder(this).apply {
            setTitle(item.userName)
            setMessage("가입 신청을 수락하시겠습니까?")
            setNegativeButton("취소", null)
            setPositiveButton("수락") { _, _ ->
                studyGroupViewModel.acceptJoinRequest(item.memberId)
            }
            show()
        }
    }

    override fun onDenyButtonClick(item: StudyGroupJoinReceiveListDto) {
        AlertDialog.Builder(this).apply {
            setTitle(item.userName)
            setMessage("가입 신청을 거절하시겠습니까?")
            setNegativeButton("취소", null)
            setPositiveButton("거절") { _, _ ->
                studyGroupViewModel.denyJoinRequest(item.memberId)
            }
            show()
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupRecyclerView() {
        joinAcceptAdapter = JoinAcceptAdapter(this, this)
        binding.recyclerView.apply {
            adapter = joinAcceptAdapter
            itemAnimator = null
            addItemDecoration(ItemDecoratorDivider(0, 0, 0, 0, 2, 0, Color.parseColor("#E0E0E0")))
            setHasFixedSize(true)
        }
    }

    private fun observeViewModel() {
        studyGroupViewModel.apply {
            joinReceiveList.observe(this@StudyManageAcceptActivity) { list ->
                binding.progressIndicator.hide()
                if (list.isEmpty()) {
                    binding.txtNoJoin.visibility = View.VISIBLE
                } else {
                    binding.txtNoJoin.visibility = View.GONE
                    joinAcceptAdapter.setItemList(list)
                }
            }

            acceptJoinRequest.observe(this@StudyManageAcceptActivity) {
                Toast.makeText(this@StudyManageAcceptActivity, it.message, Toast.LENGTH_SHORT).show()
                studyGroupViewModel.getStudyGroupJoinReceiveList(groupId)
            }

            denyJoinRequest.observe(this@StudyManageAcceptActivity) {
                Toast.makeText(this@StudyManageAcceptActivity, it.message, Toast.LENGTH_SHORT).show()
                studyGroupViewModel.getStudyGroupJoinReceiveList(groupId)
            }

            errorMessage.observe(this@StudyManageAcceptActivity) { event ->
                event.getContentIfNotHandled()?.let {
                    binding.progressIndicator.hide()
                }
            }
        }
    }
}