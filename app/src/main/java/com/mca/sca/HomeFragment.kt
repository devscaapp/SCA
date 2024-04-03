package com.mca.sca

import android.content.Intent
import android.content.Intent.getIntent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mca.sca.Adapter.EventsAdapter
import com.mca.sca.Models.Event
import com.yarolegovich.slidingrootnav.SlidingRootNav
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class HomeFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView:RecyclerView
    private lateinit var eventList: ArrayList<Event>
    private var db =Firebase.firestore

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
        return  inflater.inflate(R.layout.fragment_home, container, false)

       /* *//*val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)*//*
        val adapter = TabAdapter(this)

        viewPager.adapter = adapter

        // Customize the tab names here
        val tabNames = arrayOf("Upcoming", "Previous")

        // Set up TabLayoutMediator with customized tab names
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabNames[position]
        }.attach()

        return view*/
    }

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

        db= FirebaseFirestore.getInstance()
        recyclerView= view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        var eventType: String? = "0"
        eventList = arrayListOf()

        db= FirebaseFirestore.getInstance()

        val upcomingEvent: ImageView= view.findViewById(R.id.imageView2)
        val previousEvent: ImageView= view.findViewById(R.id.imageView3)

        upcomingEvent.setOnClickListener{
            val intent = Intent(activity, upcomingEvent::class.java)
            activity?.startActivity(intent)

        }

        previousEvent.setOnClickListener{
            val intent = Intent(activity, previousEvent::class.java)
            startActivity(intent)
        }

        //Sliding Navigation Drawer
//        private var slidindRootNav: SlidingRootNav? =null
//
//        slidindRootNav = SlidingRootNavBuilder()
//            .withMenuOpened(false)
//            .withContentClickableWhenMenuOpened(true)
//            .withSavedState(savedInstanceState)
//            .withMenuLayout(R.layout.layout_drawer)
//            .inject()

        db.collection("Event").whereEqualTo("eventType", eventType).orderBy("sr_no").get()
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
                    var adapter = EventsAdapter(eventList)
                    recyclerView.adapter=adapter
                    adapter.setOnItemClickListener(object : EventsAdapter.onItemClickListener{
                        override fun onItemClick(position: Int){

                            val intent = Intent(activity, EventMainPage::class.java)
                            intent.putExtra("evenName", eventList[position].eventName)
                            intent.putExtra("imageUrl", eventList[position].imageUrl)
                            intent.putExtra("details", eventList[position].details)
                            activity?.startActivity(intent)
                        }
                    })
                    Log.d("RecyclerView", "Adapter set with ${eventList.size} items.")
                } else {
                    // Log for debugging
                    Log.d("RecyclerView", "QuerySnapshot is empty.")
                }
            }
            .addOnFailureListener{
                Toast.makeText(requireContext(),it.toString(), Toast.LENGTH_SHORT).show()
            }


    }


}
//  https://www.youtube.com/watch?v=WbpKInkd0YQ    resource followed for this
