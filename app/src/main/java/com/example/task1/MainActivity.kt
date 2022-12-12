package com.example.task1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.task1.databinding.MyProfileBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MyProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MyProfileBinding.inflate(layoutInflater)
        binding.tvName.text = intent.getStringExtra("name")
        setContentView(binding.root)
    }
}