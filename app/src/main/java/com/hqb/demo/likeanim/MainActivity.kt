package com.hqb.demo.likeanim

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.hqb.demo.likeanim.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        initEvents()
    }

    private fun initEvents() {
        binding.btnLike.setOnClickListener {
            binding.vLikeAnim.addLike()
        }
    }
}