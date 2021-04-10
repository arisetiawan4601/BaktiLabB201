package com.arisetiawan4601.opencvtest

import android.Manifest.permission.*
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
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
import org.opencv.core.CvType.*
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
    private lateinit var kernel: Mat
    private lateinit var frameForProcessing: Mat
    private lateinit var frame: Mat
    private var contours: MutableList<MatOfPoint> = ArrayList()
    private var contours2: MutableList<MatOfPoint> = ArrayList()
    var i: Double = 0.0

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
        frame = Mat(height, width, CvType.CV_8UC4)
        frameForProcessing = Mat(height, width, CvType.CV_8UC4)
    }

    override fun onCameraViewStopped() {
        frame.release()
        frameForProcessing.release()
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        contours.clear()
        frame = inputFrame!!.rgba()
        Imgproc.cvtColor(frame, frameForProcessing, Imgproc.COLOR_RGB2HSV);
        kernel = Imgproc.getStructuringElement(0, Size(10.0, 10.0))
        Imgproc.medianBlur(frameForProcessing, frameForProcessing, 5)
        Imgproc.cvtColor(frameForProcessing, frameForProcessing, Imgproc.COLOR_RGB2HLS)
        Core.inRange(frameForProcessing, Scalar(99.0, 96.0, 0.0), Scalar(157.0, 180.0, 255.0), frameForProcessing)
        Imgproc.morphologyEx(frameForProcessing, frameForProcessing, Imgproc.MORPH_CLOSE, kernel)
        Imgproc.findContours(frameForProcessing, contours, Mat(), Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE)
        val approx = MatOfPoint()
        val approx2f = MatOfPoint2f()
        val approx2 = mutableListOf<MatOfPoint>()

        for (contour in contours) {
            if (Imgproc.contourArea(contour) > 15000) {
                val contour2f = MatOfPoint2f()
                contour.convertTo(contour2f, CvType.CV_32F)
                val peri = Imgproc.arcLength(contour2f, true)
                Imgproc.approxPolyDP(contour2f, approx2f, 0.08 * peri, true)
                approx2f.convertTo(approx, CvType.CV_32SC2)
                approx2.add(approx)
                contour2f.release()
            }
        }
        Imgproc.drawContours(frame, approx2, -1, Scalar(255.0, 0.0, 0.0), 4)
        approx.release()
        approx2f.release()
        approx2.clear()
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
