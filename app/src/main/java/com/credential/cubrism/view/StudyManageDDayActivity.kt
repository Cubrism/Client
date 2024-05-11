package com.credential.cubrism.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.credential.cubrism.databinding.ActivityStudygroupDdayBinding
import com.credential.cubrism.databinding.DialogScheduleDatepickBinding
import com.credential.cubrism.model.dto.DDayDto
import com.credential.cubrism.model.repository.StudyGroupRepository
import com.credential.cubrism.viewmodel.StudyGroupViewModel
import com.credential.cubrism.viewmodel.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class StudyManageDDayActivity : AppCompatActivity() {
    private val binding by lazy { ActivityStudygroupDdayBinding.inflate(layoutInflater) }
    private val studyGroupViewModel: StudyGroupViewModel by viewModels { ViewModelFactory(StudyGroupRepository()) }

    private val groupId by lazy { intent.getIntExtra("groupId", -1) }
    private val ddayTitle by lazy { intent.getStringExtra("ddayTitle") }
    private val dday by lazy { intent.getStringExtra("dday") }

    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupToolbar()
        setupView()
        setupDialog()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
    }

    private fun setupView() {
        binding.editTitle.setText(ddayTitle)
        binding.txtDate.text = dday

        // D-Day가 설정되어있지 않은 경우에만 설정 가능
        if (dday.isNullOrEmpty() && ddayTitle.isNullOrEmpty()) {
            binding.txtDate.setOnClickListener {
                dialog.show()
            }

            binding.btnSet.setOnClickListener {
                val title = binding.editTitle.text.toString()
                val date = binding.txtDate.text.toString()

                if (title.isEmpty()) {
                    Toast.makeText(this, "제목을 입력하세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (date.isEmpty()) {
                    Toast.makeText(this, "날짜를 선택하세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                AlertDialog.Builder(this).apply {
                    setMessage("D-Day를 설정하면 수정이 불가능합니다.")
                    setNegativeButton("취소", null)
                    setPositiveButton("확인") { _, _ ->
                        studyGroupViewModel.setDday(DDayDto(groupId, title, date))
                    }
                    show()
                }
            }
        } else {
            binding.editTitle.isEnabled = false
            binding.btnSet.isEnabled = false
        }
    }

    private fun setupDialog() {
        val builder = AlertDialog.Builder(this)
        val dialogBinding = DialogScheduleDatepickBinding.inflate(layoutInflater)

        builder.setView(dialogBinding.root)
        dialog = builder.create()

        // 날짜 선택 이벤트 처리
        dialogBinding.calendarViewDialog.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time

            binding.txtDate.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selectedDate)
            dialog.dismiss()
        }
    }

    private fun observeViewModel() {
        studyGroupViewModel.setDday.observe(this) {
            Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}