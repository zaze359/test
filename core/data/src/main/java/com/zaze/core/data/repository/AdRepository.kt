package com.zaze.core.data.repository

import com.zaze.common.di.CustomDispatchers
import com.zaze.common.di.Dispatcher
import com.zaze.core.data.service.AdService
import com.zaze.core.model.data.AdRules
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Description :
 * @author : zaze
 * @version : 2023-09-10 22:16
 */
class AdRepository @Inject constructor(
    @Dispatcher(CustomDispatchers.IO) private val dispatcher: CoroutineDispatcher,
    private val adService: AdService
) {

    suspend fun getAllAdRules(): List<AdRules> = withContext(dispatcher) {
        val responseData = adService.getAllAdRules()
        if (responseData.isSuccessful) {
            responseData.data
        } else {
            emptyList()
        }
    }
}