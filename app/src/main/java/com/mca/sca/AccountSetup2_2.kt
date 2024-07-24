package com.mca.sca

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import nl.bryanderidder.themedtogglebuttongroup.ThemedButton
import nl.bryanderidder.themedtogglebuttongroup.ThemedToggleButtonGroup

data class User(
    val uid: String? = null,
    val name: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val reg_no: String? = null,
    val rollno: String? = null,
    val batch: String? = null,
    val year: String? = null,
    val city: String? = null,
    val state: String? = null,
    val linkedln: String? = null,
    val github: String? = null,
    val verified: String? = null,
    val skill: List<String>? = null,
    val profile_imgurl: String? = null,
    val id_imgurl: String? = null,
    val isAccountSetupComplete: Boolean = false
)

class AccountSetup2_2 : AppCompatActivity() {

    private var storageRef = Firebase.storage
    private lateinit var uri_profile: Uri
    private lateinit var uri_id: Uri
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_setup22)

        val button_Upload_profilepic: ConstraintLayout = findViewById(R.id.UploadImgAndId1)
        val image_show: ImageView = findViewById(R.id.ShowImage)
        val img_id: ImageView = findViewById(R.id.ShowImage2)
        val button_UploadID: ConstraintLayout = findViewById(R.id.UploadImgAndId2)
        val back_btn: ImageView = findViewById(R.id.imageViewBackgroundSU)
        progressBar = findViewById(R.id.progressBar)

        val editTextName: EditText = findViewById(R.id.UserName)
        val editTextPhone: EditText = findViewById(R.id.PhoneNumber)
        val editText_reg_no: EditText = findViewById(R.id.RegistrationNumber)
        val editText_rollno: EditText = findViewById(R.id.RollNo)
        val editText_city: EditText = findViewById(R.id.City)
        val editText_state: EditText = findViewById(R.id.State)
        val editText_linkedln: EditText = findViewById(R.id.linkedln)
        val editText_github: EditText = findViewById(R.id.Github)
        val button_Submit: Button = findViewById(R.id.buttonRegisterSU)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val db = FirebaseFirestore.getInstance()
        storageRef = FirebaseStorage.getInstance()

        // Retrieve user data from FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser
        val email = currentUser?.email

        //spinner DATA
        val batch: Spinner = findViewById(R.id.batch_spin)
        val year: Spinner = findViewById(R.id.year_spin)

        val galleryImage = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                image_show.setImageURI(it)
                image_show.visibility = View.VISIBLE
                uri_profile = it!!
            }
        )
        button_Upload_profilepic.setOnClickListener {
            galleryImage.launch("image/*")
        }

        val galleryImage_id = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback {
                img_id.setImageURI(it)
                img_id.visibility = View.VISIBLE
                uri_id = it!!
            }
        )
        button_UploadID.setOnClickListener {
            galleryImage_id.launch("image/*")
        }

        back_btn.setOnClickListener {
            val intent = Intent(this, sign_in::class.java)
            startActivity(intent)
        }

        button_Submit.setOnClickListener {
            progressBar.visibility = View.VISIBLE  // Show progress bar
            val name = editTextName.text.toString()
            val phone = editTextPhone.text.toString()
            val reg_no = editText_reg_no.text.toString()
            val roll_no = editText_rollno.text.toString()
            val city = editText_city.text.toString()
            val state = editText_state.text.toString()
            val linkedln = editText_linkedln.text.toString()
            val github = editText_github.text.toString()

            val batchData = batch.selectedItem.toString()
            val yearData = year.selectedItem.toString()

            val themedButtonGroup = findViewById<ThemedToggleButtonGroup>(R.id.time_button_group)
            val selectedButtons = themedButtonGroup.selectedButtons
            val skillset = mutableListOf<String>()

            for (skills in selectedButtons) {
                skillset.add(skills.text)
            }

            // Profile Image Upload
            val profileImageRef = storageRef.getReference("ProfileImage").child(userId)
            val uploadTaskProfile = profileImageRef.putFile(uri_profile)
            uploadTaskProfile.addOnSuccessListener { task ->
                task.metadata!!.reference!!.downloadUrl.addOnSuccessListener { profileImageUrl ->
                    // ID Image Upload
                    val idImageRef = storageRef.getReference("IDverify").child(userId)
                    val uploadTaskID = idImageRef.putFile(uri_id)
                    uploadTaskID.addOnSuccessListener { task ->
                        task.metadata!!.reference!!.downloadUrl.addOnSuccessListener { idImageUrl ->
                            // Store user data in Firestore
                            val user = User(
                                uid = userId,
                                name = name,
                                email = email,
                                phone = phone,
                                reg_no = reg_no,
                                rollno = roll_no,
                                batch = batchData,
                                year = yearData,
                                city = city,
                                state = state,
                                linkedln = linkedln,
                                github = github,
                                verified = "N",
                                skill = skillset,
                                profile_imgurl = profileImageUrl.toString(),
                                id_imgurl = idImageUrl.toString(),
                                isAccountSetupComplete = true
                            )

                            db.collection("Users").document(userId).set(user)
                                .addOnSuccessListener {
                                    progressBar.visibility = View.GONE  // Hide progress bar
                                    Toast.makeText(this, "Data Uploaded", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                }
                                .addOnFailureListener {
                                    progressBar.visibility = View.GONE  // Hide progress bar
                                    Toast.makeText(this, "Data Upload Failed", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }
            }
        }
    }
}
