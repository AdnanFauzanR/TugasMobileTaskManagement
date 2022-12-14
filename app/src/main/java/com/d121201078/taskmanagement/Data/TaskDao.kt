package com.d121201078.taskmanagement.Data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(taskModel: Model): Long

    @Query("Select * from Model where isFinished == 0")
    fun getAllTask(): LiveData<List<Model>>

    @Query("Update model set isFinished = 1 where id=:uid")
    fun doneTask(uid: Long)

    @Update
    suspend fun updateTask(task: Model)

    @Query("Delete from Model where id=:uid")
    fun deleteTask(uid: Long)


}