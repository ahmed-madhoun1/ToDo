package com.ahmedmadhoun.todo.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ahmedmadhoun.todo.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase> /* When the TaskDatabase is created get instance of it */,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) :
        RoomDatabase.Callback() {
        // This Function Will Executed The First Time We Create The Database
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // db operation
            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insertTask(Task(title = "Wakeup", important = true, completed = true))
                dao.insertTask(Task(title = "play gaming", completed = true))
                dao.insertTask(Task(title = "reading", important = true))
                dao.insertTask(Task(title = "gym"))
                dao.insertTask(Task(title = "coding"))
                dao.insertTask(Task(title = "break"))
            }

        }
    }

}