package com.mca.sca

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mca.sca.Models.Student


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var userList: ArrayList<Student>
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val fragmentView = inflater.inflate(R.layout.fragment_profile, container, false)

        var nameTextView: TextView =fragmentView.findViewById(R.id.userName)
        var placeTextView: TextView =fragmentView.findViewById(R.id.userCityState)
        var rollTextView: TextView =fragmentView.findViewById(R.id.userRoll)
        var userLinkedinBtn: ImageView =fragmentView.findViewById(R.id.userLinkedin)
        var userGitBtn: ImageView =fragmentView.findViewById(R.id.userGit)
        var userImageView: ImageView =fragmentView.findViewById(R.id.UserImage)


        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val docRef = db.collection("Users").document(userId)
//        Log.d(TAG, "UID123 data: "+userId)
        var linkLinkedin: String?=" "
        var linkgit: String? =" "
        docRef.get()
            .addOnSuccessListener {
                if(it!=null){
                    val name= it.data?.get("name")?.toString()
                    val city= it.data?.get("city")?.toString();
                    val state= it.data?.get("state")?.toString();
                    val roll= it.data?.get("rollno")?.toString();
//                    linkLinkedin= it.data?.get("linkedln")?.toString().toString()
//                    linkgit= it.data?.get("github")?.toString().toString()
                    Log.d(TAG, "UID123 data: $linkLinkedin GIT123$linkgit")
                    nameTextView.setText(name)
                    placeTextView.setText(city+ ", "+ state)
                    rollTextView.setText(roll)
                    }
            }
            .addOnFailureListener{
                Toast.makeText(requireContext(),it.toString(),Toast.LENGTH_SHORT).show()
            }
        userLinkedinBtn.setOnClickListener {
            docRef.get()
                .addOnSuccessListener {
                    if(it!=null){
                    linkLinkedin= it.data?.get("linkedln")?.toString().toString()
                        val uri = Uri.parse(linkLinkedin) // missing 'http://' will cause crashed
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                }
                .addOnFailureListener{
                    Toast.makeText(requireContext(),it.toString(),Toast.LENGTH_SHORT).show()
                }

        }
        userGitBtn.setOnClickListener {
            docRef.get()
                .addOnSuccessListener {
                    if(it!=null){
                        linkgit= it.data?.get("github")?.toString().toString()
                        val uri = Uri.parse(linkgit) // missing 'http://' will cause crashed
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    }
                }
                .addOnFailureListener{
                    Toast.makeText(requireContext(),it.toString(),Toast.LENGTH_SHORT).show()
                }

        }
        /* //URL Opening
        val uri = Uri.parse("http://www.google.com") // missing 'http://' will cause crashed
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)*/
        return fragmentView

    }
    /*.addOnSuccessListener { document ->
        if (document != null) {

            Log.d(TAG, "DocumentSnapshot data: ${document.data}")
        } else {
            Log.d(TAG, "No such document")
        }
    }
    .addOnFailureListener { exception ->
        Log.d(TAG, "get failed with ", exception)
    }*/
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}