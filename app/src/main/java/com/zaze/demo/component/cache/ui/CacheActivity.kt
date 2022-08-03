package com.zaze.demo.component.cache.ui


import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.zaze.common.base.BaseActivity
import com.zaze.demo.R
import com.zaze.demo.component.cache.presenter.CachePresenter
import com.zaze.demo.component.cache.presenter.impl.CachePresenterImpl
import com.zaze.demo.component.cache.view.CacheView
import com.zaze.demo.databinding.CacheActivityBinding

/**
 * Description :
 * @author : zaze
 * @version : 2017-08-31 10:24 1.0
 */
open class CacheActivity : BaseActivity(), CacheView {
    var presenter: CachePresenter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<CacheActivityBinding>(this, R.layout.cache_activity)
        presenter = CachePresenterImpl(this)

        binding.cacheSaveBtn.setOnClickListener {
            presenter?.saveDataToCache()
        }

        binding.cacheGetBtn.setOnClickListener {
            presenter?.getFromCache()
        }

        binding.cacheCleanBtn.setOnClickListener {
            presenter?.cleanCache()
        }
    }
}