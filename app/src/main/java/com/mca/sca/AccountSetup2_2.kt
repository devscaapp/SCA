package com.mca.sca

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

data class User(
    val uid:String?=null,
    // val profile_imgurl:String?=null,
    //val id_imgurl:String?=null,
    val name:String?=null,
    val email:String?=null,
    val phone:String?=null,
    val reg_no:String?=null,
    val rollno:String?=null,
    val city:String?=null,
    val state:String?=null,
    val linkedln:String?=null,
    val github:String?=null,
    val verified:String?=null,
    //val skill:String?=null,
)
data class profileImg(
    val profile_imgurl: String?=null
)
data class idImg(
    val id_imgurl: String?=null
)
class AccountSetup2_2 : AppCompatActivity() {

    private var storageRef= Firebase.storage
    //private val PICK_IMAGE_REQUEST = 1
    private lateinit var uri_profile : Uri
    private lateinit var uri_id : Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setup22)
        var button_Upload_profilepic: ConstraintLayout = findViewById(R.id.UploadImgAndId1)
        var image_show: ImageView= findViewById(R.id.ShowImage);
        var img_id: ImageView= findViewById(R.id.ShowImage2);
        var button_UploadID: ConstraintLayout = findViewById(R.id.UploadImgAndId2)

        var editText_reg_no:EditText = findViewById(R.id.RegistrationNumber)
        var editText_rollno:EditText = findViewById(R.id.RollNo)
        var editText_city:EditText = findViewById(R.id.City)
        var editText_state:EditText = findViewById(R.id.State)
        var editText_linkedln:EditText = findViewById(R.id.linkedln)
        var editText_github:EditText = findViewById(R.id.Github)
        //var editText_skill:EditText = findViewById(R.id.skill)
        var button_Submit: Button= findViewById(R.id.buttonRegisterSU)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val db= FirebaseFirestore.getInstance()
        storageRef= FirebaseStorage.getInstance()

        var galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                image_show.setImageURI(it)
                image_show.setVisibility(View.VISIBLE);
                uri_profile= it!!
            }
        )
        button_Upload_profilepic.setOnClickListener {
            galleryImage.launch("image/*")
           /* val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, PICK_IMAGE_REQUEST)*/
        }

        var galleryImage_id = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                img_id.setImageURI(it)
                img_id.setVisibility(View.VISIBLE);
                uri_id= it!!
            }
        )
        button_UploadID.setOnClickListener {
            galleryImage_id.launch("image/*")

        }


        button_Submit.setOnClickListener {
            var reg_no= editText_reg_no.text.toString()
            var roll_no= editText_rollno.text.toString()
            var city= editText_city.text.toString()
            var state= editText_state.text.toString()
            var linkedln= editText_linkedln.text.toString()
            var github= editText_github.text.toString()
            //var skill= editText_reg_no.text.toString()

            //Profile Image Upload
            storageRef.getReference("ProfileImage").child(userId)
                .putFile(uri_profile)
                .addOnSuccessListener {
                        task ->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener {
                            //Image Uploaded to storage
                            val dp=profileImg( it.toString())
                            db.collection("Users").document(userId).set(dp)
                                .addOnSuccessListener {
                                }
                        }
                }

            // ID Image Upload
            storageRef.getReference("IDverify").child(userId)
                .putFile(uri_id)
                .addOnSuccessListener {
                        task ->
                    task.metadata!!.reference!!.downloadUrl
                        .addOnSuccessListener {
                            //Image Uploaded to storage
                           /* val id=idImg(it.toString())
                            db.collection("Users").document(userId).set(id)
                                .addOnSuccessListener {
                                }*/
                        }
                }
            val name = intent.getStringExtra("name")
            val email = intent.getStringExtra("email")
            val phone = intent.getStringExtra("phone")
            //Data Upload
            val user=User( userId,name,email, phone,reg_no,roll_no,city,state, linkedln, github,"N")
            db.collection("Users").document(userId).set(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data Uploaded", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Data Upload Failed", Toast.LENGTH_SHORT).show()
                }
            /*data class User(
    val uid:String?=null,
    // val profile_imgurl:String?=null,
    //val id_imgurl:String?=null,
    val name:String?=null,
    val email:String?=null,
    val phone:String?=null,
    val reg_no:String?=null,
    val rollno:String?=null,
    val city:String?=null,
    val state:String?=null,
    val linkedln:String?=null,
    val github:String?=null,
    val verified:String?=null,
    //val skill:String?=null,
            )*/
        }


    }
}