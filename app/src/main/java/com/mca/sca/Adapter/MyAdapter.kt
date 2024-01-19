package com.mca.sca.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.mca.sca.Models.Student
import com.mca.sca.R
import kotlinx.coroutines.NonDisposableHandle.parent

class MyAdapter(private val  studentList: ArrayList<Student>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
    private var db= Firebase.firestore
    lateinit var storage: FirebaseStorage




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
        //currentItem.id_img?.let { holder.userImage.setImageResource(it) }
        holder.userName.text= currentItem.name
        holder.userState.text=currentItem.state
        holder.userRoll.text = currentItem.rollno

        val storage = Firebase.storage
        val storageRef = storage.reference
        val uid= currentItem.uid

        storageRef.child("/ProfileImage/$uid").downloadUrl
            .addOnSuccessListener {
                Glide.with(holder.itemView).load(it).into(holder.userImg);
            }.addOnFailureListener {
                //Toast.makeText(holder.itemView, "URL not Found", Toast.LENGTH_LONG).show()
            }
    }


    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        //val userImage: ImageView = itemView.findViewById(R.id.UserImage)
        val userName:  TextView = itemView.findViewById(R.id.UserName)
        val userState: TextView = itemView.findViewById(R.id.UserState)
        val userRoll: TextView =itemView.findViewById(R.id.RollNo)
        val userImg: ImageView= itemView.findViewById(R.id.UserImage)
    }


}