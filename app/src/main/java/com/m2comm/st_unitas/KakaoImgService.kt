package com.m2comm.st_unitas

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoImgService {

    @GET("v2/search/image")
    fun requestList(@Query("sort") sort: String = "accuracy",
                    @Query("page") page : Int = 1,
                    @Query("query") query : String,
                    @Query("size") size : Int = 10,
    ) : Call<ParentImgDTO>

    companion object {

        private const val BASE_URL = "https://dapi.kakao.com"
        private const val ACCESS_TOKEN = "6aeeb90417aa39c14db78982923d6464"

        fun create(): KakaoImgService {
            //val httpLoggingInterceptor = HttpLoggingInterceptor()
//            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

            val headerInterceptor = Interceptor {
                val request = it.request()
                    .newBuilder()
                    .addHeader("Authorization", "KakaoAK "+ACCESS_TOKEN)
                    .build()
                return@Interceptor it.proceed(request)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(headerInterceptor)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(KakaoImgService::class.java)
        }

    }
}
