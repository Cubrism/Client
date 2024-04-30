package com.credential.cubrism.view

import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.credential.cubrism.R
import com.credential.cubrism.databinding.ActivityStudyBinding
import com.google.android.material.tabs.TabLayout

class StudyActivity : AppCompatActivity() {
    private val binding by lazy { ActivityStudyBinding.inflate(layoutInflater) }

    private lateinit var currentFragment: Fragment
    private val homeFragment = StudyGroupHomeFragment()
    private val func2Fragment = StudyGroupFunc2Fragment()
    private val func3Fragment = StudyGroupFunc3Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // 기본 타이틀 제거

        intent.getStringExtra("studyGroupTitle")?.let { title ->
            binding.txtTitle.text = title
        }

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, 0, 0)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationInit()
        menuSetUp()

        var count = 5 // 참가자 인원수
        drawerInit(count)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.study_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.manage -> {     // 관리 버튼을 누르면 관리 화면으로 이동
                val intent = Intent(this, StudyManageActivity::class.java)
                intent.putExtra("titleName", binding.txtTitle.text.toString())
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigationInit() {
        currentFragment = homeFragment
        val fragmentList = listOf(homeFragment, func2Fragment, func3Fragment)
        val transaction = supportFragmentManager.beginTransaction()

        for (fragment in fragmentList) {
            transaction.add(binding.fragmentContainerView.id, fragment)
        }
        for (fragment in listOf(func2Fragment, func3Fragment)) {
            transaction.hide(fragment)
        }

        transaction.commit()
    }

    private fun drawerInit(count: Int) { // 참가자명 drawer에 표시
        val menu = binding.navigation.menu

        for (i in 1..count) { menu.add(R.id.groupList, Menu.NONE, Menu.NONE, "참가자 $i") }
        for (i in 1..count) { menu.getItem(i - 1).isEnabled = false }
    }

    private fun menuSetUp() { // 상단 프래그먼트 메뉴 이동 버튼 설정
        for (tabIndex in 1..2) {
            val tab = binding.tabLayoutStudyGroup.getTabAt(tabIndex)
            tab?.icon?.setColorFilter(Color.parseColor("#BFBFBF"), PorterDuff.Mode.SRC_IN)
        }

        binding.tabLayoutStudyGroup.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(p0: TabLayout.Tab?) {
                // 선택된 탭의 아이콘 색상을 변경
                p0?.icon?.setColorFilter(resources.getColor(R.color.blue), PorterDuff.Mode.SRC_IN)

                when (p0?.position) {
                    0 -> changeFragment(homeFragment)
                    1 -> changeFragment(func2Fragment)
                    2 -> changeFragment(func3Fragment)
                }
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                p0?.icon?.setColorFilter(Color.parseColor("#BFBFBF"), PorterDuff.Mode.SRC_IN)
            }

            override fun onTabReselected(p0: TabLayout.Tab?) {}
        })
    }

    private fun changeFragment(fragment: Fragment) { // 프래그먼트 전환 함수
        if (fragment != currentFragment) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.custom_fade_in, R.anim.custom_fade_out)
                .hide(currentFragment)
                .show(fragment)
                .commit()
        }
        currentFragment = fragment
    }
}