package com.mca.sca

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

data class City(
    val name:String?=null,
    val state:String?=null,
    val country:String?=null,
    val isCapital:Boolean?=null,
    val population:Long?=null,
    val regions:List<String>?=null
)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        }
    }
}