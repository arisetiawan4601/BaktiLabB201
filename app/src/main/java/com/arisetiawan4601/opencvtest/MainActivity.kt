package com.arisetiawan4601.opencvtest

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import org.opencv.android.OpenCVLoader

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkOpenCV(this)
    }
    companion object {

        val TAG = "MYLOG " + MainActivity::class.java.simpleName
        fun lgd(s: String) = Log.d(TAG, s)
        fun lge(s: String) = Log.e(TAG, s)
        fun lgi(s: String) = Log.i(TAG, s)

        fun shortMsg(context: Context, s: String) =
                Toast.makeText(context, s, Toast.LENGTH_SHORT).show()

        // messages:
        private const val OPENCV_SUCCESSFUL = "OpenCV Loaded Successfully!"
        private const val OPENCV_FAIL = "Could not load OpenCV!!!"

        fun checkOpenCV(context: Context) =
                if (OpenCVLoader.initDebug()) shortMsg(context, OPENCV_SUCCESSFUL)
                else shortMsg(context, OPENCV_FAIL)
    }
}