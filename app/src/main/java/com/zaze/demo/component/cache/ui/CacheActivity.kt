package com.zaze.demo.component.cache.ui


import android.os.Bundle
import com.zaze.common.base.BaseActivity
import com.zaze.demo.R
import com.zaze.demo.component.cache.presenter.CachePresenter
import com.zaze.demo.component.cache.presenter.impl.CachePresenterImpl
import com.zaze.demo.component.cache.view.CacheView
import kotlinx.android.synthetic.main.cache_activity.*

/**
 * Description :
 * @author : zaze
 * @version : 2017-08-31 10:24 1.0
 */
open class CacheActivity : BaseActivity(), CacheView {
    var presenter: CachePresenter? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cache_activity)
        presenter = CachePresenterImpl(this)

        cache_save_btn.setOnClickListener {
            presenter?.saveDataToCache()
        }

        cache_get_btn.setOnClickListener {
            presenter?.getFromCache()
        }

        cache_clean_btn.setOnClickListener {
            presenter?.cleanCache()
        }
    }
}