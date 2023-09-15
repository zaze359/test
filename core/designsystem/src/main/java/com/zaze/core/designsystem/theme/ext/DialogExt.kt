package com.zaze.core.designsystem.theme.ext

import androidx.appcompat.app.AlertDialog

/**
 * Description :
 * @author : zaze
 * @version : 2023-09-12 23:12
 */
fun AlertDialog.colorButtons(): AlertDialog {
    setOnShowListener {
        getButton(AlertDialog.BUTTON_POSITIVE).accentTextColor()
        getButton(AlertDialog.BUTTON_NEGATIVE).accentTextColor()
        getButton(AlertDialog.BUTTON_NEUTRAL).accentTextColor()
    }
    return this
}
