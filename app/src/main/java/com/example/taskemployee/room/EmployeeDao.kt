package com.example.taskemployee.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EmployeeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployees(employees: List<Employee>)

    @Query("SELECT * FROM Employee WHERE employeeNo=:filter")
    suspend fun getEmployees(filter: Int): List<Employee>

}

