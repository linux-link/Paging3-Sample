package com.wujia.jetpack.paging3.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wujia.jetpack.paging3.sample.databinding.ActivityMainBinding
import com.wujia.jetpack.paging3.sample.ui.net.NetworkActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNet.setOnClickListener {
            startActivity(Intent(this, NetworkActivity::class.java))
        }

    }
}