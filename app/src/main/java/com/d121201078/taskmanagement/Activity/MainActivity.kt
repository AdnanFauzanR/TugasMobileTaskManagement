package com.d121201078.taskmanagement.Activity

import android.content.Intent
import android.graphics.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.d121201078.taskmanagement.Adapter.TaskAdapter
import com.d121201078.taskmanagement.Data.Model
import com.d121201078.taskmanagement.R
import com.d121201078.taskmanagement.Data.TaskViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity(), TaskAdapter.TaskClickInterface {
    val adapter = TaskAdapter(this)
    private lateinit var taskViewModel: TaskViewModel
    private lateinit var taskRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        taskRV = findViewById(R.id.taskRv)
        taskRV.adapter = adapter

        taskRV.layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager
            .VERTICAL, false)

        taskViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(application))[TaskViewModel::class.java]
        taskViewModel.getAllTask.observe(this, Observer {
            task -> adapter.setData(task)
        })
        initSwipe()

    }

    override fun onNoteClick(task: Model) {
        val intent = Intent(this@MainActivity, AddTaskActivity::class.java)
        intent.putExtra("type", "Edit")
        intent.putExtra("taskTitle", task.title)
        intent.putExtra("taskDetail", task.detail)
        intent.putExtra("taskPriority", task.priority)
        intent.putExtra("taskDate", getDate(task.date))
        intent.putExtra("taskTime", getTime(task.time))
        intent.putExtra("taskID", task.id)
        startActivity(intent)
        finish()
    }

    fun initSwipe() {
        val simpleItemTouchCallback = object: ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                if (direction == ItemTouchHelper.LEFT) {
                    lifecycleScope.launch {
                        taskViewModel.deleteTask(adapter.getItemId(position))
                    }
                } else if(direction == ItemTouchHelper.RIGHT) {
                    lifecycleScope.launch {
                        taskViewModel.doneTask(adapter.getItemId(position))
                    }
                }
            }

            override fun onChildDrawOver(
                canvas : Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder?,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val itemView = viewHolder!!.itemView
                    val paint = Paint()
                    val icon: Bitmap
                    if (dX > 0 ) {
                        icon = BitmapFactory.decodeResource(resources, R.mipmap.ic_check_white_png)
                        paint.color = Color.parseColor("#388E3C")

                        canvas.drawRect(
                            itemView.left.toFloat(), itemView.top.toFloat(),
                            itemView.left.toFloat() + dX, itemView.bottom.toFloat(), paint
                        )

                        canvas.drawBitmap(
                            icon, itemView.left.toFloat(),
                            itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top
                                .toFloat() - icon.height.toFloat()) / 2, paint
                        )
                    } else {
                        icon = BitmapFactory.decodeResource(resources, R.mipmap.ic_delete_white_png)
                        paint.color = Color.parseColor("#D32F2F")

                        canvas.drawRect(
                            itemView.right.toFloat() + dX, itemView.top.toFloat(),
                            itemView.right.toFloat(), itemView.bottom.toFloat(), paint
                        )

                        canvas.drawBitmap(
                            icon, itemView.right.toFloat() - icon.width,
                            itemView.top.toFloat() + (itemView.bottom.toFloat() - itemView.top
                                .toFloat() - icon.height.toFloat()) / 2, paint
                        )
                    }
                    viewHolder!!.itemView.translationX = dX
                } else {
                    super.onChildDrawOver(
                        canvas,
                        recyclerView,
                        viewHolder,
                        dX,
                        dY,
                        actionState,
                        isCurrentlyActive
                    )
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(taskRv)
    }

    fun addNewTask(view: View) {
        val intent = Intent(this@MainActivity, AddTaskActivity::class.java)
        intent.putExtra("type", "Add")
        startActivity(intent)
    }

    fun getTime(time: Long): String {
        val format = "h:mm a"
        val sdf = SimpleDateFormat(format)
        return sdf.format(Date(time))
    }

    fun getDate(date: Long): String {
        val format = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(format)
        return sdf.format(Date(date))
    }

}