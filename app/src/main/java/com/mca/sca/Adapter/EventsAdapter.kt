package com.mca.sca.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mca.sca.Models.codewalkerevent
import com.mca.sca.R

class EventsAdapter(private val eventList: ArrayList<codewalkerevent>): RecyclerView.Adapter<EventsAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val name : TextView = itemView.findViewById(R.id.name)
        val image : ImageView = itemView.findViewById(R.id.codewalkerzimg)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.codewalkerz_listitem,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = eventList[position].eventName
        val imageUrl: String? = eventList[position].imageUrl
        Glide.with(holder.itemView).load(imageUrl).into(holder.image);
    }
}