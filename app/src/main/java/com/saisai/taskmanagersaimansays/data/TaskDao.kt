package com.saisai.taskmanagersaimansays.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.saisai.taskmanagersaimansays.task.Task

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAllTask()

    @Query("SELECT * FROM task_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE task LIKE :searchQuery OR title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<Task>>

}