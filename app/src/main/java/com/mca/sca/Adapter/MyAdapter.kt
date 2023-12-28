package com.mca.sca.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mca.sca.Models.Student
import com.mca.sca.R

class MyAdapter(private val  studentList: ArrayList<Student>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(
           R.layout.list_item,
       parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return studentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val currentItem= studentList[position]
        currentItem.id_img?.let { holder.userImage.setImageResource(it) }
        holder.userName.text= currentItem.name
        holder.userState.text=currentItem.state
    }


    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val userImage: ImageView = itemView.findViewById(R.id.UserImage)
        val userName:  TextView = itemView.findViewById(R.id.UserName)
        val userState: TextView = itemView.findViewById(R.id.UserState)
    }


}