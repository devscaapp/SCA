package com.mca.sca

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import androidx.viewpager2.widget.ViewPager2
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import de.hdodenhof.circleimageview.CircleImageView
class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val tabLayout: TabLayout = view.findViewById(R.id.tabLayout)
        val viewPager: ViewPager2 = view.findViewById(R.id.viewPager)
        val adapter = TabAdapter(this)

        viewPager.adapter = adapter

        // Customize the tab names here
        val tabNames = arrayOf("Upcoming", "Previous")

        // Set up TabLayoutMediator with customized tab names
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabNames[position]
        }.attach()

        return view
    }


}
//  https://www.youtube.com/watch?v=WbpKInkd0YQ    resource followed for this
