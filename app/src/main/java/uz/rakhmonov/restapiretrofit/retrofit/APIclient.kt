package uz.rakhmonov.restapiretrofit.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object APIclient {
    const val BASE_URL="https://hvax.pythonanywhere.com/"

    fun getRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
    fun getApiService():MyRetrofitService{
        return getRetrofit().create(MyRetrofitService::class.java)
    }
}