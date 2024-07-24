package com.mca.sca

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mca.sca.Adapter.MyAdapter
import com.mca.sca.Models.Student

class BatchFragment : Fragment() {
    private lateinit var recycleView: RecyclerView
    private lateinit var userList: ArrayList<Student>
    private var db = Firebase.firestore
    private lateinit var batchSpinner: Spinner
    private lateinit var yearSpinner: Spinner
    private lateinit var viewBtn: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_batch, container, false)
        val fragmentContainer = fragmentView.findViewById<FrameLayout>(R.id.fragment_container)

        recycleView = fragmentView.findViewById(R.id.RecyclerList)
        recycleView.layoutManager = LinearLayoutManager(requireContext())
        batchSpinner = fragmentView.findViewById(R.id.batch_spinner)
        yearSpinner = fragmentView.findViewById(R.id.year_spinner)
        viewBtn = fragmentView.findViewById(R.id.view_button)
        userList = arrayListOf()
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Fetch current user's batch and year
        val currentUser = auth.currentUser
        if (currentUser != null) {
            fetchUserBatchAndYear(currentUser.uid)
        } else {
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
        }

        viewBtn.setOnClickListener {
            val batchData = batchSpinner.selectedItem.toString()
            val yearData = yearSpinner.selectedItem.toString()

            // Clear list
            userList.clear()
            // Fetch data from Firestore
            fetchBatchDetails(batchData, yearData)
        }

        return fragmentView
    }

    private fun fetchUserBatchAndYear(userId: String) {
        val docRef = db.collection("Users").document(userId)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val batchData = document.getString("batch") ?: "B.Tech"
                    val yearData = document.getString("year") ?: "2023"

                    // Set Spinner values
                    setSpinnerValue(batchSpinner, batchData)
                    setSpinnerValue(yearSpinner, yearData)

                    // Fetch and display batch details
                    fetchBatchDetails(batchData, yearData)
                } else {
                    Toast.makeText(requireContext(), "User data not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to fetch user data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setSpinnerValue(spinner: Spinner, value: String) {
        val adapter = spinner.adapter
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i) == value) {
                spinner.setSelection(i)
                break
            }
        }
    }

    private fun fetchBatchDetails(batch: String, year: String) {
        db.collection("Users").whereEqualTo("batch", batch).whereEqualTo("year", year).get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    for (data in it.documents) {
                        val stu: Student? = data.toObject(Student::class.java)
                        if (stu != null) {
                            // Convert name and state to uppercase
                            stu.name = stu.name?.uppercase()
                            stu.city = stu.city?.uppercase()
                            stu.state = stu.state?.uppercase()
                            userList.add(stu)
                        }
                    }
                    // Sort by roll number in ascending order
                    userList.sortBy { student -> student.rollno?.toIntOrNull() }
                    recycleView.adapter = MyAdapter(userList)
                    recycleView.adapter?.notifyDataSetChanged()
                } else {
                    // Set empty adapter when no data is available
                    recycleView.adapter = MyAdapter(ArrayList())
                    recycleView.adapter?.notifyDataSetChanged()
                    Toast.makeText(requireContext(), "No data available for this batch", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}
