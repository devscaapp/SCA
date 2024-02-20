package com.mca.sca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mca.sca.Adapter.EventsAdapter
import com.mca.sca.Adapter.PastEventsAdapter
import com.mca.sca.Models.Event

class previousEvents : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventList: ArrayList<Event>
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_previous_events)


        db= FirebaseFirestore.getInstance()
        recyclerView= findViewById(R.id.recyclerViewPastEvent)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)


        eventList = arrayListOf()

        db= FirebaseFirestore.getInstance()

        db.collection("Event").document("ppsHwPZMgo2iauSGeJPI").collection("previous").get()
            .addOnSuccessListener{ querySnapshot ->
                if(!querySnapshot.isEmpty())
                {
                    for(data in querySnapshot.documents){
                        val event: Event? = data.toObject(Event::class.java)
                        if(event!=null)
                        {
                            eventList.add(event)
                        }
                    }
                    recyclerView.adapter= PastEventsAdapter(eventList)
                    Log.d("RecyclerView", "Adapter set with ${eventList.size} items.")
                } else {
                    // Log for debugging
                    Log.d("RecyclerView", "QuerySnapshot is empty.")
                }
            }
            .addOnFailureListener{
                Toast.makeText(this,it.toString(), Toast.LENGTH_SHORT).show()
            }
    }
}