package com.mca.sca

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.mca.sca.databinding.HomeBinding
import de.hdodenhof.circleimageview.CircleImageView

/*data class City(
    val name:String?=null,
    val state:String?=null,
    val country:String?=null,
    val isCapital:Boolean?=null,
    val population:Long?=null,
    val regions:List<String>?=null
)*/
class MainActivity : AppCompatActivity() {

    private lateinit var binding : HomeBinding
    private var db=Firebase.firestore
    lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        // Check if user is authenticated
        if (auth.currentUser == null) {
            // User is not authenticated, navigate to Register activity
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
            return // Ensure the rest of the onCreate method does not run
        }

        binding= HomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        //NOTIFICATION
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            Log.e("myToken", "" + token)

            // Log and toast
//            val msg = getString(R.string.msg_token_fmt, token)
//            Log.d(TAG, msg)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

        })
        /*
        // USER data
        var userName: TextView =findViewById(R.id.UserName)
        var userImage: CircleImageView =findViewById(R.id.UserImage)
        var userState: TextView =findViewById(R.id.UserState)

        val uid= FirebaseAuth.getInstance().currentUser!!.uid
        val ref= db.collection("Users").document(uid)


        //Toast.makeText(this, "ID:"+uid.toString(), Toast.LENGTH_SHORT).show()
        ref.get().addOnSuccessListener {
            if(it != null)
            {
                val name = it.data?.get("name")?.toString()
                val email = it.data?.get("email")?.toString()

                userName.text= name
                userState.text= email
                Toast.makeText(this, "DONE!!!", Toast.LENGTH_LONG).show()
            }
        }
            .addOnFailureListener {
                Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show()
            }
        val storage = Firebase.storage
        val storageRef = storage.reference
        val pathReference = storageRef.child("/ProfileImage/"+uid.toString())

        storageRef.child("/ProfileImage/"+uid.toString()).downloadUrl
            .addOnSuccessListener {
                Glide.with(this).load(it).into(userImage);
        }.addOnFailureListener {
                Toast.makeText(this, "URL not Found", Toast.LENGTH_LONG).show()
        }*/

        binding= HomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())

        binding.bottomNavigationbar.setOnItemSelectedListener {


            when(it.itemId)
            {
                R.id.nav_option1 -> replaceFragment(HomeFragment())
                R.id.nav_option2 -> replaceFragment(ResourcesFragment())
                R.id.nav_option3 -> replaceFragment(BatchFragment())
                R.id.nav_option4 -> replaceFragment(NotificationFragment())
                R.id.nav_option5 -> replaceFragment(ProfileFragment())

                else -> {


                }
            }

            true



        }




        /*super.onCreate(savedInstanceState)
        setContentView(R.layout.home)
        var button_register: Button = findViewById(R.id.buttonLogout)
        var button_data: Button = findViewById(R.id.buttonData)
        val db=FirebaseFirestore.getInstance()
        button_data.setOnClickListener {
            val city=City("Noida","UP","India",false,5000000L,listOf("west_coast","social"))
            db.collection("cities").document("LA").set(city)
        }
        button_register.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this,Register::class.java)
            startActivity(intent)
        }*/



    }


    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.Frame_Layout, fragment)
        fragmentTransaction.commit()
    }
}