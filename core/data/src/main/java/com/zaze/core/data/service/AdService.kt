package com.zaze.core.data.service

import com.zaze.core.data.model.ResponseData
import com.zaze.core.model.data.AdRules
import retrofit2.http.GET

/**
 * Description :
 * @author : zaze
 * @version : 2023-09-10 22:24
 */
interface AdService {
    @GET("/api/v1/ad/rules/zaze")
    suspend fun getAllAdRules(): ResponseData<List<AdRules>>
}