package com.example.taskemployee.retrofit

import retrofit2.http.GET


interface ApiService {
    @GET("ECMServicessait/api/HCMDevice/Get_Sts_EmployeesFromApp_Gt8")
    suspend fun getEmployees(): List<EmployeeResponse>
}