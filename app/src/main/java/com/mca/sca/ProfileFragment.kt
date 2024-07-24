package com.mca.sca

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var nameTextView: TextView
    private lateinit var placeTextView: TextView
    private lateinit var rollTextView: TextView
    private lateinit var skillsTextView: TextView
    private lateinit var userImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        auth = FirebaseAuth.getInstance()

        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), options)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragmentView = inflater.inflate(R.layout.fragment_profile, container, false)

        nameTextView = fragmentView.findViewById(R.id.userName)
        placeTextView = fragmentView.findViewById(R.id.userCityState)
        rollTextView = fragmentView.findViewById(R.id.userRoll)
        skillsTextView = fragmentView.findViewById(R.id.userSkills)
        userImageView = fragmentView.findViewById(R.id.UserImage)

        val logoutButton: Button = fragmentView.findViewById(R.id.buttonLogout)
        logoutButton.setOnClickListener {
            // Sign out the user
            auth.signOut()
            // Sign out the user from GoogleSignInClient
            googleSignInClient.signOut().addOnCompleteListener {
                // Navigate to Register activity
                val intent = Intent(activity, Register::class.java)
                startActivity(intent)
                activity?.finish()
            }
        }

        loadUserData()

        return fragmentView
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid
        if (userId != null) {
            val docRef = db.collection("Users").document(userId)
            docRef.get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        val name = document.getString("name")?.toUpperCase()
                        val city = document.getString("city")?.toUpperCase()
                        val state = document.getString("state")?.toUpperCase()
                        val roll = document.getString("rollno")?.toUpperCase()
                        val skills = document.get("skill") as? List<String>
                        val imageUrl = document.getString("profile_imgurl")

                        nameTextView.text = name
                        placeTextView.text = "$city, $state"
                        rollTextView.text = roll
                        skillsTextView.text = skills?.joinToString(", ")?.toUpperCase()

                        // Load the user image
                        if (imageUrl != null) {
                            Glide.with(this).load(imageUrl).into(userImageView)
                        }
                    } else {
                        Log.d(TAG, "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "get failed with ", exception)
                }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
