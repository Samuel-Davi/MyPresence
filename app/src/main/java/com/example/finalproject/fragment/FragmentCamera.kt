package com.example.finalproject.fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.hardware.camera2.CameraCharacteristics
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.finalproject.R
import com.example.finalproject.opencv.OpenCVTeste
import com.google.firebase.firestore.FirebaseFirestore
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

class FragmentCamera : Fragment(){

    private lateinit var progressDialog:ProgressDialog

    private lateinit var db:FirebaseFirestore
    private lateinit var fragmentHome:FragmentHomeAluno

    companion object {

        val TAG = "MYLOG"
        fun lgd(s: String) = Log.d(TAG, s)
        fun lge(s: String) = Log.e(TAG, s)
        fun lgi(s: String) = Log.i(TAG, s)

        fun shortMsg(context: Context, s: String) =
            Toast.makeText(context, s, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_camera, container, false)

        progressDialog = ProgressDialog(activity)
        progressDialog.setMessage("Carregando...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        fragmentHome = FragmentHomeAluno()

        val data = activity?.intent?.extras
        val nome = data?.getString("nome")
        val turma = data?.getString("turma")
        val sob = data?.getString("sob")
        val ra = data?.getString("ra")
        val email = data?.getString("email")
        val inst = data?.getString("inst")

        db = FirebaseFirestore.getInstance()

        val webIntent: Intent = Uri.parse("http://192.168.0.17:5500").let { webpage ->
            Intent(Intent.ACTION_VIEW, webpage)
        }

        val constraintTrue:ConstraintLayout = view?.findViewById(R.id.constraintTrue)!!
        val constraintFalse:ConstraintLayout = view?.findViewById(R.id.constraintFalse)!!

        val docRef = db.collection("Adm").document(inst!!).collection("Alunos").document(ra!!)
        docRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                Log.d(TAG, "Current data: ${snapshot.data}")
                val entrou = snapshot.getBoolean("entrouSite")!!
                Log.d(TAG, entrou.toString())
                if(entrou){
                    if(snapshot.getBoolean("confirmaRN")!!){
                        constraintTrue.isVisible = true
                    }else{
                        docRef.get().addOnSuccessListener {
                            var nFaltas = it.getLong("totalFaltas")
                            nFaltas = nFaltas?.plus(1)
                            val faltas = hashMapOf(
                                "email" to email,
                                "totalFaltas" to nFaltas,
                                "nome" to nome,
                                "ra" to ra,
                                "confirmaRN" to false,
                                "ativado" to false,
                                "entrouSite" to false,
                                "sobrenome" to sob,
                                "turma" to turma
                            )
                            docRef.set(faltas)
                        }
                        constraintFalse.isVisible = true
                    }
                    if(progressDialog.isShowing) progressDialog.dismiss()
                }
            } else {
                Log.d(TAG, "Current data: null")
            }
        }

        db.collection("Adm").document(inst!!).collection("Alunos").document(ra!!)
            .update("ativado", true)

        startActivity(webIntent)

        val imgCancelTrue:ImageView = view?.findViewById(R.id.imgCancelTrue)!!
        imgCancelTrue.setOnClickListener {
            constraintTrue.isVisible = false
            db.collection("Adm").document(inst!!).collection("Alunos").document(ra!!)
                .update("ativado", false,
                "confirmaRN", false,
                "entrouSite", false)
            replaceFragment(fragmentHome)
        }
        val imgCancelFalse:ImageView = view?.findViewById(R.id.imgCancelFalse)!!
        imgCancelFalse.setOnClickListener {
            constraintFalse.isVisible = false
            db.collection("Adm").document(inst!!).collection("Alunos").document(ra!!)
                .update("ativado", false,
                    "confirmaRN", false,
                    "entrouSite", false)
            replaceFragment(fragmentHome)
        }


        return view;
    }
    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = activity?.supportFragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.fragmentsAluno, fragment)
        fragmentTransaction?.commit()
    }

}