package com.example.taskemployee

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.example.taskemployee.retrofit.EmployeeRepository
import com.example.taskemployee.retrofit.EmployeeViewModel
import com.example.taskemployee.retrofit.EmployeeViewModelFactory
import com.example.taskemployee.retrofit.RetrofitInstance
import com.example.taskemployee.room.AppDatabase.Companion.getDatabase
import com.example.taskemployee.room.Employee

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database=getDatabase(this)
        val apiService= RetrofitInstance.apiService
        val repository = EmployeeRepository(database.employeeDao(), apiService)
        val viewModel = ViewModelProvider(this, EmployeeViewModelFactory(repository))[EmployeeViewModel::class.java]

        enableEdgeToEdge()

        setContent {
            MainScreen(viewModel)

        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: EmployeeViewModel) {

    var filter by remember { mutableStateOf("") }
    val context = LocalContext.current
    val employees by viewModel.employees.collectAsState()
    Scaffold(topBar = {
        TopAppBar(title = {
            Text(text = "Employee Details")
        })
    },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = paddingValues.calculateTopPadding()),
                horizontalAlignment = Alignment.CenterHorizontally
            )
            {

                if (viewModel.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Button(onClick = { viewModel.fetchEmployees() })
                    {
                        Text("GET Users")
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))


                    TextField(
                        value = filter,
                        onValueChange = { filter = it },
                        label = { Text("Filter by Employee No") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )

                Button(onClick = {
                    if (filter.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Filter cannot be empty",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        viewModel.loadFilteredEmployees(filter)
                    }
                })
                {
                    Text("filter user")
                }


                CameraView()

                Spacer(modifier = Modifier.height(10.dp))

                if (employees.isEmpty()) {
                    Text("No employees found")
                } else {
                    EmployeeList(employees)
                }
            }
        })

}

@Composable
fun EmployeeList(employees: List<Employee>) {
    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
        items(employees) { employee ->
            Card(modifier = Modifier
                .padding(3.dp).wrapContentSize())
            {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Employee No: ${employee.employeeNo}")
                    Text(text = "Name: ${employee.name}")
                    Text(text = "Email: ${employee.email}")
                }
            }
        }

    }


}


























//fun Context.createImageFile(): File {
//    // Create an image file name
//    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//    val imageFileName = "JPEG_" + timeStamp + "_"
//    val image = File.createTempFile(
//        imageFileName, /* prefix */
//        ".jpg", /* suffix */
//        externalCacheDir      /* directory */
//    )
//    return image
//}