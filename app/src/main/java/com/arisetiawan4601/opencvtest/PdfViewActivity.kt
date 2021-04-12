package com.arisetiawan4601.opencvtest

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.barteksc.pdfviewer.PDFView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import java.io.File


//gs://baktilabb201.appspot.com/C++ For Dummies (7th Edition).pdf
class PdfViewActivity : AppCompatActivity() {
    private lateinit var storageReference: StorageReference
    private lateinit var pdfReference: StorageReference
    private lateinit var pdfView: PDFView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_view)

        pdfView = findViewById<PDFView>(R.id.pdfView)
        retrievePdf()
    }

    fun retrievePdf(filename: String = "jaja") = CoroutineScope(Dispatchers.IO).launch {
        try {
            storageReference = FirebaseStorage.getInstance().getReference()
            val localFile: File = File.createTempFile("C++ For Dummies (7th Edition)", "pdf")
            pdfReference = storageReference.child("C++ For Dummies (7th Edition).pdf")
            pdfReference.getFile(localFile)
                    .addOnSuccessListener { taskSnapshot ->
                        Toast.makeText(this@PdfViewActivity, "Sukses Lur", Toast.LENGTH_SHORT).show()
                        pdfView.fromFile(localFile).load()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this@PdfViewActivity, exception.message, Toast.LENGTH_SHORT).show()
                    }
                    .addOnProgressListener { taskSnapshoot ->
                        Toast.makeText(this@PdfViewActivity, "Loading pdf", Toast.LENGTH_SHORT).show()
                    }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(this@PdfViewActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }

    }
}