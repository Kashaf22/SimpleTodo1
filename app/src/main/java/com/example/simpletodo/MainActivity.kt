package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {
    var ListOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
                // 1. Remove the item from the list
                ListOfTasks.removeAt(position)
                // 2. Notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        //1. Detecting when the user clicks on the button
//        findViewById<Button>(R.id.button).setOnClickListener {
//            //Code executed when the user clicks on the button
//            Log.i("Caren", "User Clicked on button")
//        }

        loadItems()

        //Look up recycler view in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(ListOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Setup the button and input field, so that the use can enter a task and add it to the list

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //Get a reference to the button
        //and then set an onclickListener
        findViewById<Button>(R.id.button).setOnClickListener {
        // 1. Grab the text the user has inputted into @id/addTaskField
        val userInputtedTask = inputTextField.text.toString()
        // 2. Add the String to our list of tasks: ListOfTasks
        ListOfTasks.add(userInputtedTask)

        // Notify the adapter that our data has been updated
            adapter.notifyItemInserted(ListOfTasks.size - 1)

        // 3. Reset Text field
        inputTextField.setText("")

            saveItems()
        }
    }

    // save the data that the user has inputted
    //Save data by writing and reading from a file

    //Get the file we need
    fun getDataFile() : File {

        //every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }

    //Load the items by reading every line in the data file
    fun loadItems() {
        try {
            ListOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException){
            ioException.printStackTrace()
        }
    }

    //Save items by writing them into our data file
    fun saveItems() {
        try {
        FileUtils.writeLines(getDataFile(), ListOfTasks)
    } catch (ioException: IOException) {
        ioException.printStackTrace()
        }
    }
}