package com.dedebkc.intermediate.ui.mapmenu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dedebkc.intermediate.databinding.ActivityMapBinding

class MapActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMapBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}