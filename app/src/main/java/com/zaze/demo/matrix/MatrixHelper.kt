package com.zaze.demo.matrix

import android.app.Application

/**
 * Description :
 * @author : zaze
 * @version : 2022-05-17 23:09
 */
object MatrixHelper {
    fun initMatrix(application: Application) {
//        // build matrix
//        val builder = Matrix.Builder(application)
//        // add general pluginListener
//        builder.pluginListener(TestPluginListener(application))
//        // dynamic config
//        val dynamicConfig = DynamicConfigImplDemo()
//        // init plugin
//        val ioCanaryPlugin = IOCanaryPlugin(
//            IOConfig.Builder()
//                .dynamicConfig(dynamicConfig)
//                .build()
//        )
//        //add to matrix
//        builder.plugin(ioCanaryPlugin)
//
//        //init matrix
//        Matrix.init(builder.build())
//
//        // start plugin
//        ioCanaryPlugin.start()
    }

    private fun battery() {
//        val config = BatteryMonitorConfig.Builder()
//            .enable(JiffiesMonitorFeature::class.java)
//            .enableStatPidProc(true)
//            .greyJiffiesTime(30 * 1000L)
//            .setCallback(BatteryPrinter())
//            .build()
//
//        val plugin = BatteryMonitorPlugin(config)
    }
}