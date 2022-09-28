package com.dldmswo1209.cocoatalk

import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dldmswo1209.cocoatalk.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val friendFragment = FriendFragment()
        val chatFragment = ChatFragment()

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

    }
    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFrameLayout, fragment)
            .commit()
    }
}





























