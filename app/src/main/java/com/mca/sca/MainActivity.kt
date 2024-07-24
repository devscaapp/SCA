package com.mca.sca

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mca.sca.databinding.HomeBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: HomeBinding
    private var db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    private val homeFragment = HomeFragment()
    private val resourcesFragment = ResourcesFragment()
    private val batchFragment = BatchFragment()
    private val notificationFragment = NotificationFragment()
    private val profileFragment = ProfileFragment()

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

        // User is authenticated, check if account setup is complete
        checkAccountSetupStatus(auth.currentUser!!.uid)

        binding = HomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(homeFragment)

        binding.bottomNavigationbar.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_option1 -> replaceFragment(homeFragment)
                R.id.nav_option2 -> replaceFragment(resourcesFragment)
                R.id.nav_option3 -> replaceFragment(batchFragment)
                R.id.nav_option4 -> replaceFragment(notificationFragment)
                R.id.nav_option5 -> replaceFragment(profileFragment)
                else -> {}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.Frame_Layout, fragment)
        fragmentTransaction.commit()
    }

    private fun checkAccountSetupStatus(userId: String) {
        val docRef = db.collection("Users").document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val isAccountSetupComplete = document.getBoolean("accountSetupComplete") ?: false
                    if (isAccountSetupComplete) {
                        // Proceed to main activity
                        // No action needed, let the onCreate method continue
                    } else {
                        // Redirect to account setup activity
                        val intent = Intent(this, AccountSetup2_2::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    // Redirect to account setup activity if document doesn't exist
                    val intent = Intent(this, AccountSetup2_2::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            .addOnFailureListener {
                // Handle the error
                val intent = Intent(this, AccountSetup2_2::class.java)
                startActivity(intent)
                finish()
            }
    }
}
