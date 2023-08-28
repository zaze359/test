package com.zaze.common.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.zaze.common.base.ext.AppCompatActivityExtKt;

public abstract class AbsViewModelActivity extends AbsPermissionsActivity {

    /**
     * kotlin 中重写此方法报错， 'getDefaultViewModelProviderFactory' overrides nothing.
     * 先通过在 java 中重写处理。
     * ..
     * 替换并 代理默认的ViewModelProvider.Factory, 构建自定义的 AbsAndroidViewModel。
     * ...
     * 同时也保证能够支持 HiltViewModel
     * @return ViewModelProvider.Factory
     */
    @NonNull
    @Override
    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        return AppCompatActivityExtKt.obtainViewModelFactory(this, super.getDefaultViewModelProviderFactory());
    }
}
