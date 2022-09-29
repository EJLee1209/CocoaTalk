package com.dldmswo1209.cocoatalk.viewController

import android.content.Context
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dldmswo1209.cocoatalk.R
import com.dldmswo1209.cocoatalk.bottomSheetDialog.AddFriendBottomFragment
import com.dldmswo1209.cocoatalk.databinding.ActivityMainBinding
import com.dldmswo1209.cocoatalk.model.User
import com.dldmswo1209.cocoatalk.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    lateinit var user: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        user = intent.getSerializableExtra("user") as User

        val friendFragment = FriendFragment()
        val chatFragment = ChatFragment()

        // 액션바 설정
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 뷰 초기화
        replaceFragment(friendFragment)

        // 하단 네비게이션 뷰 클릭 이벤트
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.friend ->{
                    replaceFragment(friendFragment)
                    binding.toolbarTextView.text = "친구"
                }

                R.id.chat -> {
                    replaceFragment(chatFragment)
                    binding.toolbarTextView.text = "채팅"
                }
            }
            true
        }

        // 친구추가 버튼 클릭시
        binding.addFriendButton.setOnClickListener {
            val bottomSheet = AddFriendBottomFragment()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }

    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFrameLayout, fragment)
            .commit()
    }
}





























