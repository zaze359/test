package com.zaze.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.zaze.utils.log.ZLog

abstract class AbsLogFragment : Fragment {
    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    companion object {
        private const val TAG = "LifeCycle"
    }

    open val showLifeCycle = false

    private val fragmentName by lazy {
        "${this.javaClass.simpleName}@${Integer.toHexString(this.hashCode())}"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (showLifeCycle) ZLog.i(TAG, "$fragmentName onCreate")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (showLifeCycle) ZLog.i(TAG, "$fragmentName onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (showLifeCycle) ZLog.i(TAG, "$fragmentName onViewCreated")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (showLifeCycle) ZLog.i(TAG, "$fragmentName onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (showLifeCycle) ZLog.i(TAG, "$fragmentName onDestroy")
    }
}