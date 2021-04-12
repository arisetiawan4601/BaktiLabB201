package com.arisetiawan4601.opencvtest

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import org.opencv.android.OpenCVLoader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val testCameraButton = findViewById<Button>(R.id.testCamera)
        val testPdfButton = findViewById<Button>(R.id.testPdf)
        val intentCamera = Intent(this, CameraViewActivity::class.java)
        val intentReferences = Intent(this, ListReferences::class.java)
        testCameraButton.setOnClickListener {
            startActivity(intentCamera)
        }
        testPdfButton.setOnClickListener{
            startActivity(intentReferences)
        }
    }
}