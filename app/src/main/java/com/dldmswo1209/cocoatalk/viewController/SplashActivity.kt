package com.dldmswo1209.cocoatalk.viewController

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dldmswo1209.cocoatalk.R
import com.dldmswo1209.cocoatalk.viewModel.LoginViewModel
import com.dldmswo1209.cocoatalk.viewModel.MainViewModel
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        viewModel.currentUser.observe(this, Observer{
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            intent.putExtra("user", it)
            startActivity(intent)
        })

        CoroutineScope(Dispatchers.Default).launch {
            async {
                delay(3000)
            }.await()
            val id = sharedPreferences.getString("id", "").toString()
            val pw = sharedPreferences.getString("password" , "").toString()

            if(id != "" && pw != ""){
                viewModel.login(id, pw)
            }else{
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            }

        }
    }
}