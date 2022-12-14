package com.d121201078.taskmanagement.Activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.d121201078.taskmanagement.R
import androidx.lifecycle.lifecycleScope
import com.d121201078.taskmanagement.Data.Model
import com.d121201078.taskmanagement.Data.TaskViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.task_item.*
import java.util.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

class
AddTaskActivity: AppCompatActivity(){
    lateinit var calendar: Calendar
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener
    lateinit var adapter: ArrayAdapter<CharSequence>
    lateinit var taskViewModel: TaskViewModel
    lateinit var addTaskTime: TextInputLayout
    lateinit var addTaskDate: TextInputLayout
    lateinit var addTaskTitle: TextInputEditText
    lateinit var addTaskDetail: TextInputEditText
    lateinit var dateButton: TextInputEditText
    lateinit var timeButton: TextInputEditText
    lateinit var addTaskButton: Button
    lateinit var addTaskPriority: Spinner
    lateinit var type: String

    var date = 0L
    var time = 0L
    var taskID = 0L


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_task_activity)

        addTaskTitle = findViewById(R.id.addTaskTitle)
        addTaskDetail = findViewById(R.id.addTaskDetail)
        addTaskPriority = findViewById(R.id.spinnerPriority)
        dateButton = findViewById(R.id.dateButton)
        timeButton = findViewById(R.id.timeButton)
        addTaskButton = findViewById(R.id.addTaskButton)
        addTaskDate = findViewById(R.id.addTaskDate)
        addTaskTime = findViewById(R.id.addTaskTime)
        type = intent.getStringExtra("type").toString()

        taskViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(application))[TaskViewModel::class.java]


        if (type.equals("Edit")) {
            val taskTitle = intent.getStringExtra("taskTitle").toString()
            val taskDetail = intent.getStringExtra("taskDetail").toString()
            val taskTime = intent.getStringExtra("taskTime").toString()
            val taskDate = intent.getStringExtra("taskDate").toString()
            taskID = intent.getLongExtra("taskID", 0)
            addTaskTitle.setText(taskTitle)
            addTaskDetail.setText(taskDetail)
            dateButton.setText(taskDate)
            timeButton.setText(taskTime)
            addTaskButton.setText("Simpan")
        }

        dateButton.setOnClickListener {
            setDateListener()
        }
        timeButton.setOnClickListener {
            setTimeListener()
        }
        addTaskButton.setOnClickListener {
            addTask(taskID)
        }

        setUpSpinner()
    }

    private fun setUpSpinner() {
        adapter = ArrayAdapter.createFromResource(this, R.array.taskPriority, android.R
            .layout.simple_list_item_1)
        addTaskPriority.adapter = adapter
    }

    private fun addTask(id: Long) {
        val priority = addTaskPriority.selectedItem.toString()
        val title = addTaskTitle.text.toString()
        val detail = addTaskDetail.text.toString()
        type = intent.getStringExtra("type").toString()

        if(title.isEmpty()) {
            Toast.makeText(this@AddTaskActivity, "Judul belum terisi", Toast.LENGTH_LONG).show()
            addTaskTitle.requestFocus()
            return
        }

        if(type.equals("Edit")) {
            val updateTask = Model(title, detail, priority, date, time, id = id)
            lifecycleScope.launch {
                taskViewModel.updateTask(updateTask)
                Toast.makeText(this@AddTaskActivity, "Tugas Diubah", Toast.LENGTH_LONG).show()
            }
        }else {
            lifecycleScope.launch{
                val addTask = Model(title, detail, priority, date, time)
                taskViewModel.insertTask(addTask)
                Toast.makeText(this@AddTaskActivity, "Tugas Ditambahkan", Toast.LENGTH_LONG).show()
            }
        }
        startActivity(Intent(applicationContext, MainActivity::class.java))
        this.finish()

    }

    private fun setTimeListener() {
        calendar = Calendar.getInstance()
        timeSetListener =
            TimePickerDialog.OnTimeSetListener() {
                _: TimePicker, hourOfDay: Int, min: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, min)
                updateTime()
            }

        val timePickerDialog = TimePickerDialog(
            this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE), false
        ).show()
    }

    private fun updateTime() {
        val format = "h:mm a"
        val sdf = SimpleDateFormat(format)
        time = calendar.time.time
        timeButton.setText(sdf.format(calendar.time))
    }

    private fun setDateListener() {
        calendar = Calendar.getInstance()
        dateSetListener =
            DatePickerDialog.OnDateSetListener {
                    _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDate()
            }

        val datePickerDialog = DatePickerDialog(
            this, dateSetListener, calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.datePicker.minDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun updateDate() {
        val format = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(format)
        date = calendar.time.time
        dateButton.setText(sdf.format(calendar.time))

        addTaskTime.visibility = View.VISIBLE

    }

}

