package com.zaze.demo.component.okhttp

import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.zaze.common.base.ext.toJsonString
import com.zaze.utils.log.ZLog
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class OkHttpViewModel @Inject constructor(
    val okHttpClient: OkHttpClient,
    val retrofitService: RetrofitService
) :
    ViewModel() {
    companion object {
        val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
        val FORM = "application/x-www-form-urlencoded; charset=UTF-8".toMediaTypeOrNull()
    }

    private fun buildBody(): RequestBody {
        val bodyJson = JsonObject().apply {
            addProperty("appName", "测试11")
            addProperty("packageName", "com.zaze.demo")
            addProperty("versionCode", "1")
            addProperty("versionName", "v1.0")
        }
        return (bodyJson.toJsonString() ?: "").toRequestBody(JSON);
    }

    fun post() {
        val request: Request = Request.Builder()
            .url("http://192.168.56.1:8080/api/v1/app/add")
            .post(buildBody())
            .build()
        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                ZLog.e("okhttp", e.toString())
            }

            override fun onResponse(call: Call, response: Response) {
                ZLog.i("okhttp", "body: ${response.body?.string()}")
            }
        })
    }

    fun get() {
        retrofitService.get().enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(
                call: retrofit2.Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                ZLog.i("retrofit", "body: ${response.body()?.string()}")
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {
                ZLog.e("retrofit", t.toString())
            }
        })
    }

    fun add() {
        retrofitService.add(JsonObject().apply {
            addProperty("appName", "测试11")
            addProperty("packageName", "com.zaze.demo")
            addProperty("versionCode", "1")
            addProperty("versionName", "v1.0")
        }).enqueue(object : retrofit2.Callback<ResponseData<JsonObject>> {
            override fun onResponse(
                call: retrofit2.Call<ResponseData<JsonObject>>,
                response: retrofit2.Response<ResponseData<JsonObject>>
            ) {
                ZLog.i("retrofit", "body: ${response.body()?.data?.toJsonString()}")
            }

            override fun onFailure(call: retrofit2.Call<ResponseData<JsonObject>>, t: Throwable) {
                ZLog.e("retrofit", t.toString())
            }
        })
    }

}