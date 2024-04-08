package com.credential.cubrism.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.credential.cubrism.MyApplication
import com.credential.cubrism.R
import com.credential.cubrism.databinding.ActivityMainBinding
import com.credential.cubrism.model.repository.JwtTokenRepository
import com.credential.cubrism.model.repository.UserRepository
import com.credential.cubrism.model.utils.ResultUtil
import com.credential.cubrism.viewmodel.UserViewModel
import com.credential.cubrism.viewmodel.ViewModelFactory
import com.etebarian.meowbottomnavigation.MeowBottomNavigation

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private val userViewModel: UserViewModel by viewModels { ViewModelFactory(UserRepository(JwtTokenRepository())) }
    private val userDataManager = MyApplication.getInstance().getUserDataManager()

    private var backPressedTime: Long = 0
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime >= 2000) {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(this@MainActivity, "뒤로 가기를 한번 더 누르면 종료 합니다.", Toast.LENGTH_SHORT).show()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)

        navigationSet()

        userViewModel.getUserInfo()
        observeViewModel()
    }

    private val bottomNavItems = listOf(
        MeowBottomNavigation.Model(1, R.drawable.home),
        MeowBottomNavigation.Model(2, R.drawable.study),
        MeowBottomNavigation.Model(3, R.drawable.calendar),
        MeowBottomNavigation.Model(4, R.drawable.qualification),
        MeowBottomNavigation.Model(5, R.drawable.mypage)
    )
    private lateinit var currentFragment: Fragment
    private var homeFragment: HomeFragment = HomeFragment()
    private var studyFragment: StudyFragment = StudyFragment()
    private var calFragment: CalFragment = CalFragment()
    private var qualificationFragment: QualificationFragment = QualificationFragment()
    private var myPageFragment: MyPageFragment = MyPageFragment()

    private fun navigationSet() {
        binding.bottomNavigationView.show(1, true)

        bottomNavItems.forEach {
            binding.bottomNavigationView.add(it)
        }
        navigationInit()

        binding.bottomNavigationView.setOnClickMenuListener {
            when (it.id) {
                1 -> showFragment(homeFragment)
                2 -> showFragment(studyFragment)
                3 -> showFragment(calFragment)
                4 -> showFragment(qualificationFragment)
                5 -> showFragment(myPageFragment)
            }
        }
    }
    private fun navigationInit() {
        currentFragment = homeFragment
        val fragmentList = listOf(homeFragment, studyFragment, calFragment, qualificationFragment, myPageFragment)
        val transaction = supportFragmentManager.beginTransaction()

        for (fragment in fragmentList) {
            transaction.add(R.id.fragmentContainerView, fragment)
        }

        for (fragment in listOf(studyFragment, calFragment, qualificationFragment, myPageFragment)) {
            transaction.hide(fragment)
        }

        transaction.commit()
    }

    private fun showFragment(fragment: Fragment) {
        if (fragment != currentFragment) {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.custom_fade_in, R.anim.custom_fade_out)
                .hide(currentFragment)
                .show(fragment)
                .commit()
        }
        currentFragment = fragment
    }

    private fun observeViewModel() {
        userViewModel.userInfo.observe(this) { result ->
            when (result) {
                is ResultUtil.Success -> {
                    userDataManager.setUserInfo(result.data)
                }
                is ResultUtil.Error -> { Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show() }
                is ResultUtil.NetworkError -> { Toast.makeText(this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show() }
            }
        }
    }
}