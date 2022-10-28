package com.example.finalproject.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import android.view.Surface.ROTATION_0
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import com.example.finalproject.databinding.ActivityTelaCameraBinding
import com.example.finalproject.util.Draw
import com.google.common.util.concurrent.ListenableFuture
import com.google.firebase.storage.FirebaseStorage
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetector
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


typealias LumaListener = (luma: Double) -> Unit


class TelaCamera : AppCompatActivity() {

    private var imageCapture: ImageCapture? = null


    private lateinit var binding: ActivityTelaCameraBinding
    private lateinit var faceDetector: FaceDetector
    private lateinit var progressDialog: ProgressDialog
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProvider: ProcessCameraProvider
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
    private var storageRef = FirebaseStorage.getInstance().reference
    private lateinit var bitmap: Bitmap
    private var estadoCamera: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.extras
        val nome = data?.getString("nome")
        val turma = data?.getString("turma")
        val sob = data?.getString("sob")
        val ra = data?.getString("ra")
        val email = data?.getString("email")
        val inst = data?.getString("inst")

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }

        // Set up the listeners for take photo and video capture buttons
        binding.imageCaptureButton.setOnClickListener { takePhoto() }
        binding.virarTela.setOnClickListener { flipCamera() }
        binding.btSairTelaCamera.setOnClickListener {
            val intent = Intent(this, TelaMainAluno::class.java)
            intent.putExtra("nome", nome)
            intent.putExtra("sob", sob)
            intent.putExtra("turma", turma)
            intent.putExtra("email", email)
            intent.putExtra("ra", ra)
            intent.putExtra("inst", inst)
            startActivity(intent);
        }

        cameraExecutor = Executors.newSingleThreadExecutor()

//        val localModel = LocalModel.Builder()
//            .setAbsoluteFilePath("object_detection.tflite")
//            .build()

//        val customFaceDetectorOptions = FaceDetectorOptions.Builder()
//            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
//            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
//            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
//            .build()
//
//        faceDetector = FaceDetection.getClient(customFaceDetectorOptions)
//
//        val localFile = File.createTempFile("tempImage", "png")
//        storageRef.child("Adm/$inst/alunos/$ra/$ra.png").getFile(localFile)
//            .addOnSuccessListener {
//                bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
//                analyzePhoto(bitmap)
//            }.addOnFailureListener{
//                Log.e("TAG", it.message.toString())
//            }

    }

    private fun analyzePhoto(bitmap: Bitmap) {
        val smallerBitmap = Bitmap.createScaledBitmap(
            bitmap,
            bitmap.width / 10,
            bitmap.height / 10,
            false
        )

        val inputImage = InputImage.fromBitmap(bitmap, 0)
        faceDetector.process(inputImage)
            .addOnSuccessListener { faces ->
                Toast.makeText(this, "Face detectada com sucesso!", Toast.LENGTH_LONG).show()

                for (face in faces) {
                    val rect = face.boundingBox
                    rect.set(
                        rect.left * 10,
                        rect.top * (10 - 1),
                        rect.right * 10,
                        rect.bottom * 10 + 90
                    )
                }

                //cropDetectedFace(bitmap, faces)
            }.addOnFailureListener {
                Log.e("TelaCameraError", it.message.toString())
            }
    }

//    private class LuminosityAnalyzer(private val listener: LumaListener) : ImageAnalysis.Analyzer {
//
//        private fun ByteBuffer.toByteArray(): ByteArray {
//            rewind()    // Rewind the buffer to zero
//            val data = ByteArray(remaining())
//            get(data)   // Copy the buffer into a byte array
//            return data // Return the byte array
//        }
//
//        override fun analyze(image: ImageProxy) {
//
//            val buffer = image.planes[0].buffer
//            val data = buffer.toByteArray()
//            val pixels = data.map { it.toInt() and 0xFF }
//            val luma = pixels.average()
//
//            listener(luma)
//
//            image.close()
//        }
//    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun bindPreview(cameraProvider: ProcessCameraProvider) {

        val preview = Preview.Builder().build()

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.setSurfaceProvider(binding.previewView.surfaceProvider)

        val imageCapture = ImageCapture.Builder()
            .setTargetRotation(ROTATION_0)
            .build()

        var camera = cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)

//        val imageAnalysis = ImageAnalysis.Builder()
//            .setTargetResolution(Size(1280, 720))
//            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//            .build()
//
//        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
//            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
//
//            val image = imageProxy.image
//
//            if (image != null) {
//                val processImage = InputImage.fromMediaImage(image, rotationDegrees)
//
//                faceDetector
//                    .process(processImage)
//                    .addOnSuccessListener { objects ->
//                        for (i in objects) {
//                            if (binding.parentLayout.childCount > 1) binding.parentLayout.removeViewAt(
//                                1
//                            )
//
//                            val element = Draw(
//                                context = this,
//                                rect = i.boundingBox,
//                                text = i.labels.firstOrNull()?.text ?: "Undefined"
//                            )
//
//                            binding.parentLayout.addView(element)
//                        }
//
//                        imageProxy.close()
//                    }.addOnFailureListener {
//                        Log.v("TelaCamera", it.message.toString())
//                        imageProxy.close()
//                    }
//            }
//        }
    }
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        cameraProvider.unbindAll()
        binding.imageCaptureButton.isVisible = false
        binding.virarTela.isVisible = false
        binding.btSairTelaCamera.isVisible = true
    }

    private fun flipCamera() {
        Log.d(TAG, "salve")
        if (cameraSelector === CameraSelector.DEFAULT_FRONT_CAMERA) cameraSelector =
            CameraSelector.DEFAULT_BACK_CAMERA else if (cameraSelector === CameraSelector.DEFAULT_BACK_CAMERA) cameraSelector =
            CameraSelector.DEFAULT_FRONT_CAMERA
            startCamera()
    }

    @SuppressLint("UnsafeOptInUsageError")
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            // Used to bind the lifecycle of cameras to the lifecycle owner
            cameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.previewView.surfaceProvider)
                }
            imageCapture = ImageCapture.Builder().build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .setTargetResolution(Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

//            imageAnalyzer.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
//                val rotationDegrees = imageProxy.imageInfo.rotationDegrees
//
//                val image = imageProxy.image
//
//                if (image != null) {
//                    val processImage = InputImage.fromMediaImage(image, rotationDegrees)
//
//                    faceDetector
//                        .process(processImage)
//                        .addOnSuccessListener { objects ->
//                            Toast.makeText(this, "Rosto detectado", Toast.LENGTH_LONG).show()
//                            imageProxy.close()
//                        }.addOnFailureListener {
//                            Log.v("TelaCamera", it.message.toString())
//                            imageProxy.close()
//                        }
//                }
//            }


            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer)
                estadoCamera = true;

            } catch(exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun takePhoto(){
        progressDialog = ProgressDialog(this@TelaCamera)
        progressDialog.setMessage("Carregando...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        // Get a stable reference of the modifiable image capture use case
        val imageCapture =  imageCapture ?: return

        // Create time stamped name and MediaStore entry.
        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
            }
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues)
            .build()

        // Set up image capture listener, which is triggered after photo has
        // been taken
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun
                        onImageSaved(output: ImageCapture.OutputFileResults){
                    val msg = "Photo capture succeeded: ${output.savedUri}"
                    Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    if(progressDialog.isShowing) progressDialog.dismiss()
                    onDestroy()
                    Log.d(TAG, msg)
                }
            }
        )
    }

    companion object {
        private const val TAG = "CameraXApp"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
    }
}