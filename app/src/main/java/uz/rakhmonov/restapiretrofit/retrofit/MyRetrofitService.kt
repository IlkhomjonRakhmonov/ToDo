package uz.rakhmonov.restapiretrofit.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import uz.rakhmonov.restapiretrofit.models.MyReqDelete
import uz.rakhmonov.restapiretrofit.models.MyToDoRequest
import uz.rakhmonov.restapiretrofit.models.MyToDoX


interface MyRetrofitService {
  @GET ("plan")
  fun getAllToDo():Call<List<MyToDoX>>

  @POST("plan/")
  fun addToDo(@Body myToDoRequest: MyToDoRequest):Call<MyToDoX>

  @DELETE("plan/{id}/")
  fun deleteTodo(@Path("id") id:Int):Call<MyReqDelete>

  @PATCH("plan/{id}/")
  fun updateTodo(@Path("id") id:Int, @Body myToDoRequest: MyToDoRequest): Call<MyToDoX>
}