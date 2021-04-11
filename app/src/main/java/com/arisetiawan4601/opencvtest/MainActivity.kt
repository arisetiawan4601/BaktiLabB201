package com.arisetiawan4601.opencvtest

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import org.opencv.android.OpenCVLoader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val testCameraButton = findViewById<Button>(R.id.testCamera)
        val intent = Intent(this, CameraViewActivity::class.java)
        testCameraButton.setOnClickListener {
            startActivity(intent)
        }
    }

    fun testCamera() {
        Toast.makeText(applicationContext, "Button Pressed", Toast.LENGTH_LONG).show()
    }
}