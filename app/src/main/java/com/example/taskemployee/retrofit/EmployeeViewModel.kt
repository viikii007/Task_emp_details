package com.example.taskemployee.retrofit

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.taskemployee.room.Employee
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class EmployeeViewModel(private val repository: EmployeeRepository) : ViewModel() {
    var isLoading by mutableStateOf(false)
    private val _employees = MutableStateFlow<List<Employee>>(emptyList())
    val employees: StateFlow<List<Employee>> = _employees

    fun fetchEmployees() {
        isLoading = true
        viewModelScope.launch {
            try {
                val employeeList = repository.fetchAndSaveEmployees()
                _employees.value = employeeList
            } catch (e: Exception) {
                Log.d("viewmodel", "error ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }



    fun loadFilteredEmployees(filter: String) {
        viewModelScope.launch {
            val formattedFilter = filter.toIntOrNull() ?: return@launch
            val filteredData=repository.getFilteredEmployees(formattedFilter)
            _employees.value = filteredData
        }
    }

}

class EmployeeViewModelFactory(private val repository: EmployeeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmployeeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EmployeeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


