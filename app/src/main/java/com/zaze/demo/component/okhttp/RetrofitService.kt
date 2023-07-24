package com.zaze.demo.component.okhttp

import com.google.gson.JsonObject
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitService {

    @GET("/api/v1/app/all")
    fun get(): Call<ResponseBody>

    @POST("/api/v1/app/add")
    fun add(@Body appJson: JsonObject): Call<ResponseData<JsonObject>>

//    @POST("/api/v1/app/add")
//    @FormUrlEncoded
//    fun add(@Field("appName") appName:String,  @Field("packageName") packageName:String): Call<ResponseBody>

//    @POST("/api/v1/app/add")
//    @FormUrlEncoded
//    fun add(@FieldMap map: Map<String, Any> ): Call<ResponseBody>

}