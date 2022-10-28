package com.example.finalproject.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.decodeFile
import android.graphics.Movie.decodeFile
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.finalproject.R
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class AdmHomeFragment : Fragment() {

//    private lateinit var buttonProfs:TextView
//    private lateinit var buttonTurmas:TextView
//
    private lateinit var profsFragment: FragmentProfs
    private lateinit var turmasFragment: FragmentTurmas
    private lateinit var bitmap: Bitmap

    val storageRef = FirebaseStorage.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val bit = requireArguments().getByteArray("bitmap")


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_adm_home, container, false)

        val imageAdm: ImageView = view.findViewById(R.id.imgAdmPrincipal)


        val data = activity?.intent?.extras
        val nome = data?.getString("nome")
        val email = data?.getString("email")
        val inst = data?.getString("inst")


        val localFile = File.createTempFile("tempImage", "png")
        storageRef.child("Adm/$inst/admImage.png").getFile(localFile)
            .addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                Log.d("TAG", bitmap.toString())
                imageAdm.setImageBitmap(bitmap)
            }.addOnFailureListener{
                Log.e("TAG", it.message.toString())
            }



        val txtOla:TextView = view.findViewById(R.id.txtOla)
        txtOla.text = "Olá, $nome"

        val buttonProfs: TextView = view.findViewById(R.id.txtProf)

        val buttonTurmas: TextView = view.findViewById(R.id.txtTurma)

        profsFragment = FragmentProfs()
        turmasFragment = FragmentTurmas()

        addFragment(profsFragment)

        buttonProfs?.setOnClickListener {
            replaceFragment(profsFragment)
            buttonProfs.setBackgroundResource(R.drawable.editlink)
            buttonTurmas.setBackgroundResource(0)
            Log.d("TAG", "ta aq")
        }
        buttonTurmas?.setOnClickListener {
            replaceFragment(turmasFragment)
            buttonTurmas.setBackgroundResource(R.drawable.editlink)
            buttonProfs.setBackgroundResource(0)
            Log.d("TAG", "agr ta aq")
        }

        return view
    }

    private fun addFragment(fragment: Fragment){
        val fragmentTransaction = activity?.supportFragmentManager!!.beginTransaction().add(R.id.fragmentsAdmHome, fragment)
        fragmentTransaction.commit()
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentTransaction = activity?.supportFragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentsAdmHome, fragment)
        fragmentTransaction.commit()
    }
}