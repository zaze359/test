package com.zaze.demo.component.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag

class DragBehavior : CoordinatorLayout.Behavior<View> {
    constructor() : super()
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    /**
     * 使用 改 Behavior 的View 依赖哪个 View，即监听哪个View的变化。
     * child: 使用了该 Behavior 的View。
     * dependency: 监听哪个View
     *
     */
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
//        ZLog.i(ZTag.TAG_DEBUG, "------ child: $child")
//        ZLog.i(ZTag.TAG_DEBUG, "------ dependency: $dependency")
        return dependency is Draggable
    }

    private var dependencyX = Float.MAX_VALUE
    private var dependencyY = Float.MAX_VALUE

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
//        ZLog.i(
//            ZTag.TAG_DEBUG,
//            "------ onDependentViewChanged: ${dependency.top}, ${dependency.left}"
//        )
        if (dependencyX == Float.MAX_VALUE || dependencyY == Float.MAX_VALUE) {
            dependencyX = dependency.x
            dependencyY = dependency.y
        } else {
            val dX = dependency.x - dependencyX
            val dy = dependency.y - dependencyY
            child.translationX += dX
            child.translationY += dy

            dependencyX = dependency.x
            dependencyY = dependency.y
        }
        return true
    }
}