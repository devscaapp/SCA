package com.mca.sca

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

class Batch_Recycler : AppCompatActivity() {

    lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_batch_recycler)

       spinner = findViewById(R.id.planets_spinner)


        /*val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.Batch,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // Set the default value
        val defaultValue = "2021" // Replace with your default value
        val position = adapter.getPosition(defaultValue)
        spinner.setSelection(position)*/
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
               Toast.makeText(this@Batch_Recycler,
                   " You selected ${parent?.getItemAtPosition(position).toString()}",
               Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }


    }
}