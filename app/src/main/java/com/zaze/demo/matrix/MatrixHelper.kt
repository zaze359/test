package com.zaze.demo.matrix

import android.app.Application
import com.tencent.matrix.Matrix
import com.tencent.matrix.iocanary.IOCanaryPlugin
import com.tencent.matrix.iocanary.config.IOConfig
import com.zaze.demo.matrix.config.DynamicConfigImplDemo

/**
 * Description :
 * @author : zaze
 * @version : 2022-05-17 23:09
 */
object MatrixHelper {
    fun initMatrix(application: Application) {
        // build matrix
        val builder = Matrix.Builder(application)
        // add general pluginListener
        builder.pluginListener(TestPluginListener(application))
        // dynamic config
        val dynamicConfig = DynamicConfigImplDemo()
        // init plugin
        val ioCanaryPlugin = IOCanaryPlugin(
            IOConfig.Builder()
                .dynamicConfig(dynamicConfig)
                .build()
        )
        //add to matrix
        builder.plugin(ioCanaryPlugin)

        //init matrix
        Matrix.init(builder.build())

        // start plugin
        ioCanaryPlugin.start()
    }
}