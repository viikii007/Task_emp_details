package com.example.taskemployee.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Employee")
data class Employee(
    @PrimaryKey
    val employeeNo: Int,
    val name: String?,
    val email: String?
)

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val uri: String,
    val timestamp: Long
)
