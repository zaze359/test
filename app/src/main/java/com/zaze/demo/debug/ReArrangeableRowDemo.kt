package com.zaze.demo.debug

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.Icon
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.zIndex
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.coroutines.launch

@Composable
fun ReArrangeableRowDemo() {
    // 示例数据
    val items = remember { mutableStateListOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5") }
    val scope = rememberCoroutineScope()
    var draggedIndex by remember { mutableStateOf(-1) }
    var targetIndex by remember { mutableStateOf(-1) }
    var offsetX by remember { mutableStateOf(0f) }
    val itemWidth = 120.dp
//    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    LazyRow(
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(items) { index, item ->
            // 计算当前项的动画偏移
            val animatedOffset by animateDpAsState(
                targetValue = if (index == draggedIndex) offsetX.dp else 0.dp
            )

            // 计算背景色动画
            val backgroundColor by animateColorAsState(
                targetValue = if (index == draggedIndex) Color.LightGray else Color.White
            )

            // 计算拖动时的z轴高度
            val elevation by animateDpAsState(
                targetValue = if (index == draggedIndex) 0.dp else 0.dp
            )
            Box(
                modifier = Modifier
                    .width(itemWidth)
                    .height(itemWidth)
                    .offset(x = animatedOffset)
                    .background(backgroundColor)
                    .shadow(elevation)
                    .zIndex(if (index == draggedIndex) 1F else 0F)
                    .draggable(
                        orientation = Orientation.Horizontal,
                        state = rememberDraggableState { delta ->
                            // 更新拖动偏移
                            offsetX += delta
                            // 计算目标位置
                            val newTargetIndex = calculateTargetIndex(
                                offsetX = offsetX,
                                itemWidth = itemWidth,
                                currentIndex = draggedIndex,
                                totalItems = items.size
                            )
                            ZLog.i(
                                ZTag.TAG,
                                "onDelta: ${delta}, offsetX: $offsetX, newTargetIndex: ${newTargetIndex}; draggedIndex: $draggedIndex"
                            )
                            // 如果目标位置变化，交换项目
                            if (newTargetIndex != -1 && newTargetIndex != draggedIndex && newTargetIndex != targetIndex) {
                                targetIndex = newTargetIndex
                                scope.launch {
                                    // 交换列表中的项目
                                    if (draggedIndex != -1) {
                                        ZLog.i(
                                            ZTag.TAG,
                                            "交换列表中的项目: ${delta}, offsetX: $offsetX, newTargetIndex: ${newTargetIndex}; draggedIndex: $draggedIndex"
                                        )
                                        items.swap(draggedIndex, targetIndex)
                                        // 更新拖动索引以反映新位置
                                        if (delta > 0) {
                                            offsetX -= itemWidth.value
                                        } else {
                                            offsetX += itemWidth.value
                                        }
                                        draggedIndex = targetIndex
                                    }
                                }
                            }
                        },
                        onDragStarted = {
                            // 开始拖动
                            draggedIndex = index
                            targetIndex = index
                            offsetX = 0f
                            ZLog.i(
                                ZTag.TAG,
                                "开始拖动 draggedIndex: ${draggedIndex}, targetIndex: $targetIndex"
                            )

                        },
                        onDragStopped = {
                            // 停止拖动，重置状态
                            draggedIndex = -1
                            targetIndex = -1
                            offsetX = 0f
                            ZLog.i(ZTag.TAG, "停止拖动，重置状态")
                        }
                    )
                    .pointerInput(Unit) {
                        // 处理点击事件等
                    }
            ) {
                // 列表项内容
                Text(
                    text = item,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(16.dp)
                )

                // 拖动图标
                Icon(
                    imageVector = Icons.Default.DragHandle,
                    contentDescription = "Drag handle",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(16.dp)
                )
            }
        }
    }
}

// 计算目标位置索引
private fun calculateTargetIndex(
    offsetX: Float,
    itemWidth: Dp,
    currentIndex: Int,
    totalItems: Int
): Int {
    if (currentIndex == -1) return -1
    ZLog.i(ZTag.TAG, "交换列表中的项目: itemWidth: ${itemWidth}; ${itemWidth.value}")

    // 计算偏移对应的项目数
    val itemWidthPx = itemWidth.value
    val itemsMoved = (offsetX / itemWidthPx).toInt()

    // 计算新的目标索引
    var newIndex = currentIndex + itemsMoved

    // 边界检查
    newIndex = newIndex.coerceIn(0, totalItems - 1)

    return newIndex
}

// 扩展函数：交换列表中两个位置的元素
private fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    if (index1 >= 0 && index1 < size && index2 >= 0 && index2 < size) {
        val temp = this[index1]
        this[index1] = this[index2]
        this[index2] = temp
    }
}