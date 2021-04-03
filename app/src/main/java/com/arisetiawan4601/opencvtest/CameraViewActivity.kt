package com.arisetiawan4601.opencvtest

import android.Manifest.permission.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.SurfaceView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.JavaCamera2View
import org.opencv.android.OpenCVLoader
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import kotlin.math.log

// Permission vars:
private const val REQUEST_CODE_PERMISSIONS = 111
private val REQUIRED_PERMISSIONS = arrayOf(
    CAMERA,
    WRITE_EXTERNAL_STORAGE,
    READ_EXTERNAL_STORAGE,
    RECORD_AUDIO,
    ACCESS_FINE_LOCATION
)

class CameraViewActivity : AppCompatActivity(), CameraBridgeViewBase.CvCameraViewListener2 {

    private lateinit var cameraBridgeViewBase: CameraBridgeViewBase
    private lateinit var baseLoaderCallback: BaseLoaderCallback
    lateinit var frame: Mat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera_view)

        // Request camera permissions
        if (allPermissionsGranted()) {
            Toast.makeText(applicationContext, "Haha", Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS )
        }

        cameraBridgeViewBase = findViewById<JavaCamera2View>(R.id.cameraView)
        cameraBridgeViewBase.visibility = SurfaceView.VISIBLE
        cameraBridgeViewBase.setCameraPermissionGranted();
        cameraBridgeViewBase.setCvCameraViewListener(this)

        baseLoaderCallback = object : BaseLoaderCallback(this) {
            override fun onManagerConnected(status: Int) {
                super.onManagerConnected(status)

                when  (status) {
                    SUCCESS -> {
                        cameraBridgeViewBase.enableView()
                        Toast.makeText(applicationContext, "Camera View Enabled", Toast.LENGTH_LONG).show()

                    }


                    else -> super.onManagerConnected(status)
                }
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                Toast.makeText(applicationContext, "Haha", Toast.LENGTH_LONG).show()
            } else {
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    // Camera View Listeners member

    override fun onCameraViewStarted(width: Int, height: Int) {

    }

    override fun onCameraViewStopped() {
        TODO("Not yet implemented")
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        // frame to show
        frame = inputFrame!!.rgba()
        return frame
    }

    override fun onResume() {
        super.onResume()

        if (!OpenCVLoader.initDebug()) {
            Toast.makeText(applicationContext, "Ada yang salah gan", Toast.LENGTH_LONG).show()
        }
        else {
            baseLoaderCallback.onManagerConnected(0)
        }
    }

    override fun onPause() {
        super.onPause()

        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase.disableView()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
