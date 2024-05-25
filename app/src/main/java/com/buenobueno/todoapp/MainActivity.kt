package com.buenobueno.todoapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.buenobueno.todoapp.adapters.RecyclerAdapter
import com.buenobueno.todoapp.database.TodoTask
import com.buenobueno.todoapp.database.TodoTaskService
import com.buenobueno.todoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var todoTaskService: TodoTaskService? = null
    private var adapter: RecyclerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        todoTaskService = TodoTaskService.getInstance(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        adapter = RecyclerAdapter(ArrayList())
        adapter!!.setOnTodoTaskClickListener(object : RecyclerAdapter.OnTodoTaskClickListener {
            override fun onTaskClick(todoTask: TodoTask?) {
                Toast.makeText(applicationContext, "${todoTask?.title}", Toast.LENGTH_SHORT).show()
            }

        })

        binding.recyclerView.setAdapter(adapter)
        binding.recyclerView.setLayoutManager(LinearLayoutManager(this))

        binding.fab.setOnClickListener { view ->
            Thread {
                val title = binding.titleEditText.text.toString()
                val newTask = TodoTask(title)
                todoTaskService!!.insertTodoTask(
                    newTask,
                    object : TodoTaskService.OperationCallBack {
                        override fun onOperationCompleted() {
                            runOnUiThread {
                                binding.titleEditText.setText("")
                            }
                            fetchData()
                            println("This works!")
                        }

                        override fun onError(e: Exception?) {
                            // implement
                        }

                    })
            }.start()
        }

        setupItemTouchHelper()
        fetchData()
    }

    @Suppress("UNCHECKED_CAST")
    fun fetchData() {
        todoTaskService!!.readAllTodoTasks { tasks: List<TodoTask?>? ->
            runOnUiThread {
                adapter!!.setData(
                    tasks as MutableList<TodoTask>
                )
            }
        }
    }

    private fun setupItemTouchHelper() {
        // Setup ItemTouchHelper for swipe to delete
        val itemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // We don't want move functionality
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition // Get swiped item position
                val taskToDelete = adapter!!.getTaskAtPosition(position) // Get the user to delete

                todoTaskService!!.deleteTodoTask(
                    taskToDelete,
                    object : TodoTaskService.OperationCallBack {
                        override fun onOperationCompleted() {
                            runOnUiThread {
                                adapter!!.removeTaskAtPosition(position)
                            }
                        }

                        override fun onError(e: Exception?) {
                            // handle error
                        }
                    })
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        return when (id) {
            R.id.action_completed_tasks -> {
                Toast.makeText(this, "Completed Tasks clicked!", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.action_settings -> {
                Toast.makeText(this, "Settings clicked!", Toast.LENGTH_SHORT).show()
                true
            }

            R.id.action_about -> {
                Toast.makeText(this, "About clicked!", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}