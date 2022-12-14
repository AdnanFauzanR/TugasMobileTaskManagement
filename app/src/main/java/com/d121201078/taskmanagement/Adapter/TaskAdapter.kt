package com.d121201078.taskmanagement.Adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.d121201078.taskmanagement.Activity.AddTaskActivity
import com.d121201078.taskmanagement.Activity.MainActivity
import com.d121201078.taskmanagement.Data.Model
import com.d121201078.taskmanagement.R
import kotlinx.android.synthetic.main.task_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class TaskAdapter(val taskClickInterface: TaskClickInterface): RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var taskList = ArrayList<Model>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.task_item, parent, false)
        )
    }

    override fun getItemCount() = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(taskList[position], taskClickInterface)
    }

    override fun getItemId(position: Int): Long {
        return taskList[position].id
    }



    class TaskViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(task: Model, taskClickInterface: TaskClickInterface) {
            val colors = itemView.resources.getIntArray(R.array.colors)
            with(itemView) {
                taskTitle.text = task.title
                taskDetail.text = task.detail
                taskPriority.text = task.priority
                updateTime(task.time)
                updateDate(task.date)
                when(task.priority) {
                    context!!.resources.getStringArray(R.array.taskPriority)[0] ->
                        viewColorTag.setBackgroundColor(colors[0])
                    context!!.resources.getStringArray(R.array.taskPriority)[1] ->
                        viewColorTag.setBackgroundColor(colors[1])
                    context!!.resources.getStringArray(R.array.taskPriority)[2] ->
                        viewColorTag.setBackgroundColor(colors[2])
                    context!!.resources.getStringArray(R.array.taskPriority)[3] ->
                        viewColorTag.setBackgroundColor(colors[3])
                }
                taskViewLayout.setOnClickListener {
                    taskClickInterface.onNoteClick(task)
                }
            }
        }
        private fun updateTime(time: Long) {
            val format = "h:mm a"
            val sdf = SimpleDateFormat(format)
            itemView.taskTime.text = sdf.format(Date(time))
        }

        private fun updateDate(time: Long) {
            val format = "EEE, d MMM yyyy"
            val sdf = SimpleDateFormat(format)
            itemView.taskDate.text = sdf.format(Date(time))
        }
    }

    fun setData(task: List<Model>) {
        this.taskList.clear()
        this.taskList.addAll(task)
        notifyDataSetChanged()
    }

    interface TaskClickInterface {
        fun onNoteClick(task: Model)
    }

}