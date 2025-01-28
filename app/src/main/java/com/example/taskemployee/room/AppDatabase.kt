package com.example.taskemployee.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Employee::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao

    companion object{

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "employeeDB")
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}




