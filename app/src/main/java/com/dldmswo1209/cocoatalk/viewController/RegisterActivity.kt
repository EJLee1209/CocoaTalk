package com.dldmswo1209.cocoatalk.viewController

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dldmswo1209.cocoatalk.databinding.ActivityRegisterBinding
import com.dldmswo1209.cocoatalk.viewModel.LoginViewModel

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityRegisterBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        binding.registerButton.setOnClickListener {
            val id = binding.inputIdEditText.text.toString()
            val pw = binding.inputPwEditText.text.toString()
            val name = binding.inputNameEditText.text.toString()
            if(id == ""|| pw == ""|| name == "") return@setOnClickListener

            viewModel.register(id,pw,name)
        }

        viewModel.isNew.observe(this, Observer {
            if(it){
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
            }
        })

    }
}