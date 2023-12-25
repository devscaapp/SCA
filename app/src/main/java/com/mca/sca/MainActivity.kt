package com.mca.sca

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.mca.sca.databinding.HomeBinding

data class City(
    val name:String?=null,
    val state:String?=null,
    val country:String?=null,
    val isCapital:Boolean?=null,
    val population:Long?=null,
    val regions:List<String>?=null
)
class MainActivity : AppCompatActivity() {

    private lateinit var binding : HomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                R.id.nav_option4 -> replaceFragment(ProfileFragment())

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
        button_register.setOnClickListener {
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