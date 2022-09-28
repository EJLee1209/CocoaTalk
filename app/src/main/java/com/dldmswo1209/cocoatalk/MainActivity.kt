package com.dldmswo1209.cocoatalk

import android.content.Context
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dldmswo1209.cocoatalk.databinding.ActivityMainBinding
import com.dldmswo1209.cocoatalk.viewModel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
    lateinit var user_id : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        val friendFragment = FriendFragment()
        val chatFragment = ChatFragment()
        val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        user_id = sharedPreferences.getString("user_id", "").toString()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        replaceFragment(friendFragment)

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





























