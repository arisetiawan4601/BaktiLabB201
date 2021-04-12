package com.arisetiawan4601.opencvtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ListReferences : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_references)

        val bookButton = findViewById<Button>(R.id.bookButton)
        val intentPdf = Intent(this, PdfViewActivity::class.java)
        bookButton.setOnClickListener{
            startActivity(intentPdf)
        }
    }
}