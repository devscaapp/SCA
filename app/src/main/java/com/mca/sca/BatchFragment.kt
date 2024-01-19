package com.mca.sca

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.mca.sca.Adapter.MyAdapter
import com.mca.sca.Models.Student

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BatchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
/*
class BatchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        return inflater.inflate(R.layout.fragment_batch, container, false)
    }

    companion object {
        */
/**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BatchFragment.
         *//*

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BatchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}*/

class BatchFragment : Fragment() {
    private lateinit var recycleView:RecyclerView
    private lateinit var userList: ArrayList<Student>
    private var db =Firebase.firestore
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_batch, container, false)
        val fragmentContainer = fragmentView.findViewById<FrameLayout>(R.id.fragment_container)

        // Inflate the activity layout and add it to the fragment container
        val activityLayout = inflater.inflate(R.layout.activity_batch_recycler, fragmentContainer, false)
        fragmentContainer.addView(activityLayout)

        recycleView = fragmentView.findViewById(R.id.RecyclerList)
        recycleView.layoutManager=LinearLayoutManager(requireContext())

        userList = arrayListOf()
        db= FirebaseFirestore.getInstance()

        db.collection("Users").get()
            .addOnSuccessListener {
            if(!it.isEmpty)
            {
                for(data in it.documents)
                {
                    val stu: Student? = data.toObject(Student::class.java)
                    if(stu!=null)
                    {
                        userList.add(stu)
                    }
                }
                recycleView.adapter = MyAdapter(userList)
            }
            }
            .addOnFailureListener{
                Toast.makeText(requireContext(),it.toString(),Toast.LENGTH_SHORT).show()
            }

        return fragmentView
    }
}

