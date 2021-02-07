package com.m2comm.st_unitas

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET

interface KakaoImgService {

    @GET("v2/search/image")
    fun requestList(@Field("sort") sort: String,
                    @Field("page") page : String ,
                    @Field("query") query : String,
                    @Field("size") size : Int,
    ) : Call<ImgDTO>

}