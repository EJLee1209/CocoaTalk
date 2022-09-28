package com.dldmswo1209.cocoatalk

import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.dldmswo1209.cocoatalk.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}





























