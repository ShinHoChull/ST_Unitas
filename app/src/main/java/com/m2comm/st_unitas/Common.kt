package com.m2comm.st_unitas

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Common {

    companion object {
        val API_KEY = "aaa"
    }

    object RetrofitClient {

        private var instance: Retrofit? = null
        private val gson = GsonBuilder().setLenient().create()
        // 서버 주소
        private const val BASE_URL = "https://dapi.kakao.com"

        fun getInstance(): Retrofit {
            if (instance == null) {
                instance = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }

            return instance!!
        }
    }
}