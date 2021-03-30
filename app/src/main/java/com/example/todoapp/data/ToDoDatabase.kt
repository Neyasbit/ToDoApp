package com.example.todoapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.todoapp.data.models.Converter
import com.example.todoapp.data.models.ToDoModel

@Dao
interface ToDoDao {
    @get:Query("SELECT * FROM ToDoModel order by id ASC")
    val allData: LiveData<List<ToDoModel>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(toDoModel: ToDoModel)

    @Update
    suspend fun updateModel(toDoModel: ToDoModel)

    @Delete
    suspend fun deleteModel(toDoModel: ToDoModel)

    @Query("DELETE FROM ToDoModel")
    suspend fun deleteAllModels()

    @Query("SELECT * FROM ToDoModel WHERE title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<ToDoModel>>

    @get:Query("SELECT * FROM ToDoModel order by case when priority LIKE 'H%' THEN 1 when priority like 'M%' then 2 when priority like 'L%' then 3 END")
    val sortByHighPriority: LiveData<List<ToDoModel>>

    @get:Query("SELECT * FROM ToDoModel order by case when priority like 'L%' then 1 when priority like 'M%' then 2 when priority like 'H%' then 3 END")
    val sortByLowPriority: LiveData<List<ToDoModel>>
}

@Database(entities = [ToDoModel::class], version = 1, exportSchema = false)
@TypeConverters(Converter::class)
abstract class ToDoDatabase : RoomDatabase() {
    abstract val getToDoDao: ToDoDao
}

private lateinit var INSTANCE: ToDoDatabase

fun getDataBase(context: Context): ToDoDatabase {
    synchronized(ToDoDatabase::class) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room
                .databaseBuilder(
                    context.applicationContext,
                    ToDoDatabase::class.java,
                    "todo_database"
                ).build()
        }
    }
    return INSTANCE
}


