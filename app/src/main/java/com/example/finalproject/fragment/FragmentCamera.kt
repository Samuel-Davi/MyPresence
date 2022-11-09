package com.example.finalproject.fragment

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.finalproject.R
import com.example.finalproject.opencv.OpenCVTeste
import kotlinx.android.synthetic.main.activity_open_cvteste.*
import kotlinx.android.synthetic.main.fragment_camera.*
import org.opencv.android.BaseLoaderCallback
import org.opencv.android.CameraBridgeViewBase
import org.opencv.android.OpenCVLoader
import org.opencv.core.*
import org.opencv.imgproc.Imgproc
import org.opencv.objdetect.CascadeClassifier
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlin.concurrent.schedule

private const val REQUEST_CODE_PERMISSIONS_ = 111
private val REQUIRED_PERMISSIONS_ = arrayOf(
    Manifest.permission.CAMERA,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.RECORD_AUDIO,
    Manifest.permission.ACCESS_FINE_LOCATION
)

class FragmentCamera : Fragment(), CameraBridgeViewBase.CvCameraViewListener2 {

    private lateinit var viewFinder: CameraBridgeViewBase

    //fragment
    private lateinit var homeFragment:FragmentHomeAluno

    //openCV callback
    lateinit var cvBaseLoaderCallback: BaseLoaderCallback

    //image storage
    lateinit var imageMat: Mat
    lateinit var grayMat: Mat

    //progressDialog
    private lateinit var progressDialog:ProgressDialog

    //face library
//    private val faceLibInputStream = resources.openRawResource(R.raw.haarcascade_frontalface_alt2)
//
    var faceDetector: CascadeClassifier? = null
    lateinit var faceDir: File
    var imageRatio = 0.0 // scale down ratio

    //orientation
    private var screenRotation:Int = 0

    companion object {

        val TAG = "MYLOG"
        fun lgd(s: String) = Log.d(TAG, s)
        fun lge(s: String) = Log.e(TAG, s)
        fun lgi(s: String) = Log.i(TAG, s)

        fun shortMsg(context: Context, s: String) =
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show()

        // messages:
        private const val OPENCV_SUCCESSFUL = "OpenCV Loaded Successfully!"
        private const val OPENCV_FAIL = "Could not load OpenCV!!!"
        private const val OPENCV_PROBLEM = "There's a problem in OpenCV."
        private const val PERMISSION_NOT_GRANTED = "Permissions not granted by the user."

        // Face model
        private const val FACE_DIR = "facelib"
        private const val FACE_MODEL = "haarcascade_frontalface_alt2.xml"
        private const val byteSize = 4096 // buffer size

        // RGB
        private val YELLOW = Scalar(255.0, 255.0, 0.0)
        private val BLUE = Scalar(0.0, 0.0, 255.0)
        private val RED = Scalar(255.0, 0.0, 0.0)
        private val GREEN = Scalar(0.0, 255.0, 0.0)
    }

    private fun checkOpenCV(context: Context) {

        if (OpenCVLoader.initDebug()) {
            shortMsg(context, OPENCV_SUCCESSFUL)
            lgi("OpenCV started...")
        } else {
            shortMsg(context, OPENCV_FAIL)
            lge(OPENCV_FAIL)
        }
    }

    private fun loadFaceLib(){
        try {
            val modelInputStream = resources.openRawResource(R.raw.haarcascade_frontalface_alt2)

            faceDir = activity?.getDir(FACE_DIR, Context.MODE_PRIVATE)!!

            val faceModel = File(faceDir, FACE_MODEL)

            if(!faceModel.exists()){
                val modelOutputStream = FileOutputStream(faceModel)

                val buffer = ByteArray(byteSize)
                var byteRead = modelInputStream.read(buffer)
                while (byteRead != -1){
                    modelOutputStream.write(buffer, 0, byteRead)
                    byteRead = modelInputStream.read(buffer)
                }

                modelInputStream.close()
                modelOutputStream.close()
            }

            faceDetector = CascadeClassifier(faceModel.absolutePath)
        } catch (e: IOException){
            lge("Error loading cascade face model...$e")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_camera, container, false)

        homeFragment = FragmentHomeAluno()

        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN)
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

//        if (allPermissionsGranted()) {
//            checkOpenCV(requireActivity())
//        } else {
//            ActivityCompat.requestPermissions(
//                requireActivity(),
//                REQUIRED_PERMISSIONS_,
//                REQUEST_CODE_PERMISSIONS_
//            )
//        }

        cvBaseLoaderCallback = object : BaseLoaderCallback(activity) {
            override fun onManagerConnected(status: Int) {

                when (status) {
                    SUCCESS -> {
                        OpenCVTeste.lgi(OPENCV_SUCCESSFUL)
                        OpenCVTeste.shortMsg(activity!!, OPENCV_SUCCESSFUL)

                        loadFaceLib()
                        if(faceDetector!!.empty()){
                            faceDetector = null
                        }else{
                            faceDir.delete()
                        }

                        viewFinder.enableView()
                    }

                    else -> super.onManagerConnected(status)
                }
            }
        }

        viewFinder = view?.findViewById(R.id.cameraView)!!
        viewFinder.visibility = SurfaceView.VISIBLE
        viewFinder.setCameraIndex(
            CameraCharacteristics.LENS_FACING_BACK)
        viewFinder.setCvCameraViewListener(this)



        val mOrientationEventListener =  object : OrientationEventListener(activity) {
            override fun onOrientationChanged(orientation: Int) {
                // Monitors orientation values to determine the target rotation value
                when (orientation) {
                    in 45..134 -> {
                        rotation_tv.text = getString(R.string.n_270_degree)
                        screenRotation = 270
                    }
                    in 135..224 -> {
                        rotation_tv.text = getString(R.string.n_180_degree)
                        screenRotation = 180
                    }
                    in 225..314 -> {
                        rotation_tv.text = getString(R.string.n_90_degree)
                        screenRotation = 90
                    }
                    else -> {
                        rotation_tv.text = getString(R.string.n_0_degree)
                        screenRotation = 0
                    }
                }

            }
        }
        if (mOrientationEventListener.canDetectOrientation()) {
            mOrientationEventListener.enable();
        } else {
            mOrientationEventListener.disable();
        }

        return view;
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS_) {
            if (allPermissionsGranted()) {
                checkOpenCV(requireActivity())
            } else {
                OpenCVTeste.shortMsg(requireActivity(), PERMISSION_NOT_GRANTED)
                Log.e("MYLOG", "erro")
//                activity?.finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS_.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onPause() {
        super.onPause()
        viewFinder?.let { viewFinder.disableView() }
    }

    override fun onResume() {
        super.onResume()
        OpenCVTeste.lgi("ta aq")
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, activity, cvBaseLoaderCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewFinder?.let { viewFinder.disableView() }
        if (faceDir.exists()) faceDir.delete()
    }

    override fun onCameraViewStarted(width: Int, height: Int) {
        imageMat = Mat(width, height, CvType.CV_8UC4)
        grayMat = Mat(width, height, CvType.CV_8UC4)
    }

    override fun onCameraViewStopped() {
        imageMat.release()
        grayMat.release()
    }

    override fun onCameraFrame(inputFrame: CameraBridgeViewBase.CvCameraViewFrame?): Mat {
        imageMat = inputFrame!!.rgba()
//        grayMat = inputFrame!!.gray()
        grayMat = get480Image(inputFrame.gray())
//        imageRatio = 1.0

        drawFaceRectangle()

        return imageMat
    }

    private fun get480Image(src: Mat): Mat {
        val imageSize = Size(src.width().toDouble(), src.height().toDouble())
        imageRatio = ratioTo480(imageSize)

        if (imageRatio.equals(1.0)) return src

        val dstSize = Size(imageSize.width*imageRatio, imageSize.height*imageRatio)
        val dst = Mat()
        Imgproc.resize(src, dst, dstSize)

        when (screenRotation) {
            0-> {
                Core.rotate(dst, dst, Core.ROTATE_90_CLOCKWISE)
                Core.flip(dst, dst, 1)
            }
            180-> {
                Core.rotate(dst, dst, Core.ROTATE_90_COUNTERCLOCKWISE)
                Core.flip(dst, dst, 1)
            }
        }

        return dst
    }

    private fun ratioTo480(imageSize: Size): Double {
        val w = imageSize.width
        val h = imageSize.height
        val heightMax = 480
        var ratio: Double = 0.0

        if (w > h) {
            if (w < heightMax) return 1.0
            ratio = heightMax / w
        } else {
            if (h < heightMax) return 1.0
            ratio = heightMax / h
        }

        return ratio
    }

    private fun drawFaceRectangle() {
        val faceRects = MatOfRect()
        faceDetector!!.detectMultiScale(
            grayMat,
            faceRects)

        val scrW = imageMat.width().toDouble()
        val scrH = imageMat.height().toDouble()

        for (rect in faceRects.toArray()) {
            var x = rect.x.toDouble()
            var y = rect.y.toDouble()
            var w = 0.0
            var h = 0.0
            var rw = rect.width.toDouble() // rectangle width
            var rh = rect.height.toDouble() // rectangle height

            if (imageRatio.equals(1.0)) {
                w = x + rw
                h = y + rh
            } else {
                x /= imageRatio
                y /= imageRatio
                rw /= imageRatio
                rh /= imageRatio
                w = x + rw
                h = y + rh
            }

            when (screenRotation) {
                90-> {
                    rectFace(x, y, w, h, RED)
                    drawDot(x, y, GREEN)
                }
                0-> {
                    rectFace(y, x, h, w, RED)
                    drawDot(y, x, GREEN)
                }
                180-> {
                    OpenCVTeste.lgd("x: $x -- y: $y :: sW: $scrW, sH: $scrH")
                    rectFace(y, x, h, w, RED)
                    drawDot(y, x, GREEN)

                    // fix height
                    val yFix = scrH - y
                    val hFix = yFix - rh

                    rectFace(yFix, x, hFix, w, YELLOW)
                    drawDot(yFix, x, BLUE)
                }
            }
        }
    }

    private fun liberaCaixa() {
        viewFinder.disableView()
        Timer().schedule(2000){
            progressDialog = ProgressDialog(requireContext())
            progressDialog.setMessage("Carregando...")
            progressDialog.setCancelable(false)
            progressDialog.show()
        }
        Timer().schedule(5000){
            if (progressDialog.isShowing) progressDialog.dismiss()
            val fragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
            fragmentTransaction.replace(R.id.fragmentsAluno, homeFragment)
            fragmentTransaction.commit()
        }
//        val boxRf:ConstraintLayout = activity?.findViewById(R.id.boxRfPositivo)!!
//        boxRf.visibility = SurfaceView.VISIBLE

    }

    fun rectFace(x: Double, y: Double, w: Double, h: Double, color:Scalar) {
        Imgproc.rectangle(
            imageMat, // image
            Point(x, y), // upper corner
            Point(w, h),  // opposite corner
            color  // RGB
        )
//        liberaCaixa()
    }

    fun drawDot(x: Double, y:Double, color:Scalar) {
        Imgproc.circle(
            imageMat, // image
            Point(x, y),  // center
            4, // radius
            color, // RGB
            -1, // thickness: -1 = filled in
            8 // line type
        )
    }

}