package com.zaze.demo.debug


import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ReorderableVerticalGridDemo() {
    val items = remember {
        mutableStateListOf(
            "Item 1", "Item 2", "Item 3", "Item 4", "Item 5",
            "Item 6", "Item 7", "Item 8", "Item 9", "Item 10"
        )
    }
    ReorderableVerticalGrid(
        items = items,
        onItemsReordered = {},
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReorderableVerticalGrid(
    items: List<String>,
    onItemsReordered: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
    cellSize: Dp = 100.dp,
    spacing: Dp = 8.dp,
    columns: Int = 4
) {
    // 使用MutableStateList确保列表变化时自动刷新UI
    val mutableItems = remember(items) { mutableStateListOf<String>().apply { addAll(items) } }
    val scope = rememberCoroutineScope()

    // 拖动状态
    var draggedIndex by remember { mutableStateOf(-1) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var itemPositions by remember { mutableStateOf(emptyList<Pair<PointRange, PointRange>>()) }
//    var itemSize by remember { mutableStateOf(0.dp) }

    // 跟踪当前占位符位置
    var placeholderIndex by remember { mutableStateOf(-1) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(spacing),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing),
        modifier = modifier
    ) {
        itemsIndexed(mutableItems) { index, item ->
            val isDragging = index == draggedIndex
            val isPlaceholder = index == placeholderIndex
            // 测量项目大小
            Box(
                modifier = Modifier
                    .size(cellSize)
                    .onGloballyPositioned { coordinates ->
//                        if (itemSize == 0.dp) {
//                            itemSize = with(LocalDensity.current) {
//                                coordinates.size.width.toDp()
//                            }
//                        }
                        // 收集项目位置信息
                        val position = coordinates.positionInRoot()
                        val itemWidth = coordinates.size.width
                        val itemHeight = coordinates.size.height
                        ZLog.i(
                            ZTag.TAG,
                            "onGloballyPositioned(${item}), position: ${position}; itemWidth: $itemWidth; $itemHeight"
                        )
                        // 更新项目位置范围 (left, top, right, bottom)
                        itemPositions = itemPositions.toMutableList().apply {
                            // 更新项目位置范围 (left, top, right, bottom)
                            val newRange = PointRange(
                                position.x.toInt(),
                                position.y.toInt()
                            ) to PointRange(
                                (position.x + itemWidth).toInt(),
                                (position.y + itemHeight).toInt()
                            )
                            ZLog.i(ZTag.TAG, "onGloballyPositioned($index) newRange: ${newRange};")
                            if (size <= index) {
                                add(newRange)
                            } else {
                                set(index, newRange)
                            }
                        }
                    }
                    .then(
                        if (isDragging) {
                            Modifier
                                .offset { IntOffset(dragOffset.x.toInt(), dragOffset.y.toInt()) }
                                .graphicsLayer(
                                    scaleX = 1.1f,
                                    scaleY = 1.1f,
                                    shadowElevation = 12f
                                )
                        } else if (isPlaceholder) {
                            Modifier
                                .background(Color.Transparent)
                        } else {
                            Modifier
                        }
                    )
                    .shadow(
                        elevation = if (isDragging) 12.dp else 4.dp,
                        shape = MaterialTheme.shapes.medium
                    )
                    .clip(MaterialTheme.shapes.medium)
                    .background(
                        if (isDragging) MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
                        else MaterialTheme.colorScheme.surface
                    )
                    .pointerInput(index) {
                        detectDragGestures(
                            onDragStart = {
                                draggedIndex = index
                                placeholderIndex = index
                                ZLog.i(
                                    ZTag.TAG,
                                    "onDragStart 记录位置 draggedIndex: ${draggedIndex}; placeholderIndex: $placeholderIndex"
                                )
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                dragOffset += dragAmount
                                // 计算拖动位置对应的目标索引

                                val currentPosition = itemPositions.getOrNull(index) ?: return@detectDragGestures
                                val targetIndex =
                                    findTargetIndex(dragOffset, index, itemPositions, columns)
                                val targetPosition = itemPositions.getOrNull(targetIndex) ?: return@detectDragGestures
                                ZLog.i(
                                    ZTag.TAG,
                                    "onDrag targetIndex: ${targetIndex}; placeholderIndex: $placeholderIndex; dragAmount: $dragAmount;"
                                )
                                // 如果目标索引变化且有效，交换项目
                                if (targetIndex != placeholderIndex) {
                                    scope.launch {
                                        ZLog.i(
                                            ZTag.TAG,
                                            "交换列表中的项目: $placeholderIndex >> ${targetIndex}"
                                        )
                                        // 更新占位符位置
                                        placeholderIndex = targetIndex
                                        // 交换列表中的项目
                                        mutableItems.swap(index, targetIndex)
                                        // 更新拖动索引以反映新位置
                                        draggedIndex = targetIndex
                                        val itemOffset = Offset(
                                            x = (targetPosition.first.x - currentPosition.first.x).toFloat(),
                                            y = (targetPosition.first.y - currentPosition.first.y).toFloat()
                                        )
//                                        dragOffset -= itemOffset
                                        // 通知外部列表已重新排序
                                        onItemsReordered(mutableItems.toList())
                                    }
                                }
                            },
                            onDragEnd = {
                                ZLog.i(ZTag.TAG, "onDragEnd clear")
                                draggedIndex = -1
                                placeholderIndex = -1
                                dragOffset = Offset.Zero
                            }
                        )
                    }
            ) {
                if (!isPlaceholder) {
                    Text(
                        text = item,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(8.dp)
                    )
                }
            }
        }
    }
}

// 查找拖动位置对应的目标索引
private fun findTargetIndex(
    dragOffset: Offset,
    currentIndex: Int,
    itemPositions: List<Pair<PointRange, PointRange>>,
    columns: Int
): Int {
    ZLog.i(
        ZTag.TAG,
        "findTargetIndex: dragOffset: ${dragOffset}; currentIndex: $currentIndex; columns: $columns; $itemPositions"
    )
    if (itemPositions.isEmpty() || currentIndex >= itemPositions.size) return -1

    // 获取当前拖动项目的位置
    val currentPosition = itemPositions[currentIndex]
    val centerX =
        currentPosition.first.x + (currentPosition.second.x - currentPosition.first.x) / 2
    val centerY =
        currentPosition.first.y + (currentPosition.second.y - currentPosition.first.y) / 2
    // 计算拖动后的中心位置
    val draggedCenterX = (centerX + dragOffset.x).toInt()
    val draggedCenterY = (centerY + dragOffset.y).toInt()

    // 查找拖动后的中心位置所在的项目
    for (i in itemPositions.indices) {
        if (i == currentIndex) continue

        val position = itemPositions[i]
        if (draggedCenterX in position.first.x..position.second.x &&
            draggedCenterY in position.first.y..position.second.y
        ) {
            ZLog.i(
                ZTag.TAG,
                "findTargetIndex: position: ${position}; "
            )
            return i
//
//            // 检查是否应该交换（基于行和列的位置）
//            val currentRow = currentIndex / columns
//            val currentCol = currentIndex % columns
//            val targetRow = i / columns
//            val targetCol = i % columns
//
//            // 只允许相邻行/列的交换，避免不自然的跳跃
//            if (abs(currentRow - targetRow) <= 1 && abs(currentCol - targetCol) <= 1) {
//                return i
//            }
        }
    }

    return -1
}

// 扩展函数：交换列表中两个位置的元素
private fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    if (index1 in indices && index2 in indices) {
        val temp = this[index1]
        this[index1] = this[index2]
        this[index2] = temp
    }
}

// 用于表示矩形范围的坐标点
private data class PointRange(val x: Int, val y: Int)

// 预览
@Preview
@Composable
fun ReorderableVerticalGridPreview() {
    MaterialTheme {
        val items = remember { List(12) { "Item ${it + 1}" } }
        ReorderableVerticalGrid(
            items = items,
            onItemsReordered = { /* 更新持久化数据 */ },
            columns = 3
        )
    }
}