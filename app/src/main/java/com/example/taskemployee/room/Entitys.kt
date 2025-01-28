package com.example.taskemployee.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Employee")
data class Employee(
    @PrimaryKey
    val employeeNo: Int,  // Make sure this is the correct primary key
    val name: String?,
    val email: String?
)


