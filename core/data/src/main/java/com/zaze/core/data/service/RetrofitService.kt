package com.zaze.core.data.service

import com.google.gson.JsonObject
import com.zaze.core.data.model.ResponseData
import io.reactivex.Observable
import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitService {

    @GET("/api/v1/app/all")
    fun getByCall(): Call<ResponseBody>

    @POST("/api/v1/app/add")
    fun add(@Body appJson: JsonObject): Call<ResponseData<JsonObject>>

    @GET("/api/v1/app/all")
    fun getByRx(): Observable<ResponseData<JsonObject>>

    @GET("/api/v1/app/all")
    fun getByFlow(): Flow<ResponseData<JsonObject>>

    @GET("/api/v1/app/all")
    suspend fun get(): JsonObject


//    @POST("/api/v1/app/add")
//    @FormUrlEncoded
//    fun add(@Field("appName") appName:String,  @Field("packageName") packageName:String): Call<ResponseBody>

//    @POST("/api/v1/app/add")
//    @FormUrlEncoded
//    fun add(@FieldMap map: Map<String, Any> ): Call<ResponseBody>

}