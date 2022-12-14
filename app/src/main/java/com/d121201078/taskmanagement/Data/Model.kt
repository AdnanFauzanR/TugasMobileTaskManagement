package com.d121201078.taskmanagement.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Model(
    var title: String,
    var detail: String,
    var priority: String,
    var date: Long,
    var time: Long,
    var isFinished: Int = 0,
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
)
