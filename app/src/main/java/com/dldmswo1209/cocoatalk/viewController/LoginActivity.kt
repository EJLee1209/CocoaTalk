package com.dldmswo1209.cocoatalk.viewController

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dldmswo1209.cocoatalk.databinding.ActivityLoginBinding
import com.dldmswo1209.cocoatalk.viewModel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.loginButton.setOnClickListener {
            val id = binding.inputIdEditText.text.toString()
            val password = binding.inputPwEditText.text.toString()
            if(id == "" || password == "") return@setOnClickListener
            viewModel.login(id, password)
        }

        binding.registerTextView.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        viewModel.currentUser.observe(this, Observer {
            it ?: return@Observer
            if(it.uid == -1){
                Toast.makeText(this, "로그인 실패, 아이디 또는 비밀번호를 확인해주세요",Toast.LENGTH_SHORT).show()
            }else{
                val sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("user_id", it.id)
                editor.commit()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("user", it)
                startActivity(intent)
                finish()
            }
        })

    }
}