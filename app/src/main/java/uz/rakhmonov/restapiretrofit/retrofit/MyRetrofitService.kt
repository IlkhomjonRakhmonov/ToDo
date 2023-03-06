package uz.rakhmonov.restapiretrofit.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import uz.rakhmonov.restapiretrofit.models.MyToDoRequest
import uz.rakhmonov.restapiretrofit.models.MyToDoX


interface MyRetrofitService {
  @GET ("plan")
  fun getAllToDo():Call<List<MyToDoX>>

  @POST("plan/")
  fun addToDo(@Body myToDoRequest: MyToDoRequest):Call<MyToDoX>
}