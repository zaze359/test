package com.zaze.demo.component.cache.presenter.impl

import com.zaze.common.base.mvp.BaseMvpPresenter
import com.zaze.demo.app.MyApplication
import com.zaze.demo.component.cache.presenter.CachePresenter
import com.zaze.demo.component.cache.view.CacheView
import com.zaze.utils.ZAppUtil
import com.zaze.utils.ZConvertUtil
import com.zaze.utils.cache.MemoryCacheManager

/**
 * Description :
 * @author : zaze
 * @version : 2017-08-31 10:24 1.0
 */
open class CachePresenterImpl(view: CacheView) : BaseMvpPresenter<CacheView>(view), CachePresenter {

    override fun cleanCache() {
        MemoryCacheManager.clearMemoryCache()
    }

    override fun getFromCache() {
        for (i in 1..2) {
            Thread({
                for (j in 1..1000) {
                    MemoryCacheManager.getCache("$i$j")
                }
            }).start()
        }
    }

    override fun saveDataToCache() {
        for (i in 1..2) {
            Thread({
                for (j in 1..1000) {
                    val drawable = ZAppUtil.getAppIcon(MyApplication.getInstance())
                    if (drawable != null) {
                        val bitmap = ZConvertUtil.drawable2Bitmap(drawable)
                        MemoryCacheManager.saveCacheBytes("$i$j", ZConvertUtil.bitmap2Bytes(bitmap))
                    } else {
                    }
                }
            }).start()
        }
    }
}
