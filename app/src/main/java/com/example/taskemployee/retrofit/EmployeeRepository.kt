package com.example.taskemployee.retrofit

import android.util.Log
import com.example.taskemployee.room.Employee
import com.example.taskemployee.room.EmployeeDao

class EmployeeRepository(private val employeeDao: EmployeeDao, private val apiService: ApiService) {

    suspend fun fetchAndSaveEmployees(): List<Employee> {
        val employees = apiService.getEmployees().map { data ->
            Employee(
                employeeNo = data.EmployeeId,
                name = data.EmployeeName,
                email = data.Email
            )
        }

        if (employees.isNotEmpty()) {
            employeeDao.insertEmployees(employees)
        } else {
            Log.d("repo", "employee list empty")
        }

        return employees
    }


    suspend fun getFilteredEmployees(filter: Int): List<Employee> {
        return employeeDao.getEmployees(filter)
    }
}