package com.mca.sca

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ResourcesFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var mcaResources: CardView
    private lateinit var btechResources: CardView

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
        val view = inflater.inflate(R.layout.fragment_resources, container, false)

        mcaResources = view.findViewById(R.id.mcaResources)
        btechResources = view.findViewById(R.id.btechResources)

        // Set click listener for MCA resources card
        mcaResources.setOnClickListener {
            // Start MCAResources activity
            startActivity(Intent(activity, mcaResources::class.java))
        }

        // Set click listener for B.Tech resources card
        btechResources.setOnClickListener {
            // Start BTechResources activity
            startActivity(Intent(activity, btechResources::class.java))
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResourcesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
