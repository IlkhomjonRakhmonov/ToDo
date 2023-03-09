package uz.rakhmonov.restapiretrofit.retrofit

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import uz.rakhmonov.restapiretrofit.models.MyToDoX


interface MyRetrofitService {
  @GET ("plan")
  fun getAllToDo():Call<List<MyToDoX>>

  @POST("plan/")
  fun addToDo(@Body myToDoX: MyToDoX):Call<MyToDoX>

  @DELETE("plan/{id}/")
  fun deleteTodo(@Path("id") id:Int):Call<Int>

 @PATCH("plan/{id}/")
 fun updateToDo(@Path("id") id: Int, @Body myToDoX: MyToDoX ):Call<MyToDoX>

    //     @PATCH("plan/{id}/")
//     fun updateTodo(@Path("id") id:Int, @Body myToDoX: MyToDoX): Call<MyToDoX>
}