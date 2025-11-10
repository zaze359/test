package com.zaze.demo.debug
import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zaze.utils.log.ZLog
import com.zaze.utils.log.ZTag
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ReorderableHorizontalGridDemo() {
    val items = remember { mutableStateListOf(
        "Item 1", "Item 2", "Item 3", "Item 4", "Item 5",
        "Item 6", "Item 7", "Item 8", "Item 9", "Item 10"
    ) }

    val scope = rememberCoroutineScope()
    var draggedItemIndex by remember { mutableStateOf(-1) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    var itemSize by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    // 网格配置
//    val gridCells = GridCells.Adaptive(minSize = 100.dp)
    val gridCells = GridCells.Fixed(5)
    val spacing = 8.dp
    Box(modifier = Modifier.fillMaxSize()) {
        LazyHorizontalGrid(
            rows = gridCells,
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            verticalArrangement = Arrangement.spacedBy(spacing),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(items) { index, item ->
                val isDragging = index == draggedItemIndex

                // 计算拖动时的动画效果
                val scale by animateFloatAsState(if (isDragging) 1.05f else 1f)
                val elevation by animateDpAsState(if (isDragging) 12.dp else 4.dp)
                val backgroundColor by animateColorAsState(
                    if (isDragging) MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
                    else MaterialTheme.colorScheme.surface
                )

                // 测量项目大小
                Box(
                    modifier = Modifier
                        .aspectRatio(1f) // 正方形项目
                        .onGloballyPositioned { coordinates ->
                            if (itemSize == 0.dp) {
                                itemSize = with(density) { coordinates.size.width.toDp() }
                            }
                        }
                        .then(
                            if (isDragging) {
                                Modifier
                                    .offset { IntOffset(dragOffset.x.toInt(), dragOffset.y.toInt()) }
                                    .graphicsLayer(scaleX = scale, scaleY = scale)
                            } else Modifier
                        )
                        .shadow(elevation, RoundedCornerShape(12.dp))
                        .clip(RoundedCornerShape(12.dp))
                        .background(backgroundColor)
                        .gridDraggableItem(
                            index = index,
                            isDragging = isDragging,
                            onDragStarted = { draggedItemIndex = index },
                            onDragDelta = { delta -> dragOffset += delta },
                            onDragEnd = {
                                // 计算目标位置
                                val targetIndex = calculateTargetIndex2(
                                    dragOffset = dragOffset,
                                    itemSize = itemSize + spacing,
                                    currentIndex = index,
                                    totalItems = items.size,
                                    gridCells = gridCells,
                                    spacing = spacing,
                                    cellsPerRow = 5
                                )
                                ZLog.i(ZTag.TAG,"onDragEnd: index: ${index}; targetIndex: ${targetIndex}")
                                // 如果有新的目标位置，交换项目
                                if (targetIndex != -1 && targetIndex != index) {
                                    scope.launch {
                                        ZLog.i(ZTag.TAG,"交换列表中的项目: index: ${index}; targetIndex: ${targetIndex}")
                                        items.swap(index, targetIndex)

                                    }
                                }

                                // 重置拖动状态
                                draggedItemIndex = -1
                                dragOffset = Offset.Zero
                            }
                        )
                ) {
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




// 计算目标位置索引
private fun calculateTargetIndex2(
    dragOffset: Offset,
    itemSize: Dp,
    currentIndex: Int,
    totalItems: Int,
    gridCells: GridCells,
    spacing: Dp,
    cellsPerRow:Int
): Int {
    val itemSizePx = itemSize.value
    val spacingPx = spacing.value

    // 计算每个网格的宽度（包括间距）
    val cellWidth = itemSizePx + spacingPx

    // 计算横向移动的项目数
    val itemsMovedHorizontally = (dragOffset.x / cellWidth).roundToInt()

    // 计算当前行的项目数
//    val screenWidth = LocalConfiguration.current.screenWidthDp
//    val density = LocalDensity.current
//    val cellsPerRow = with(density) {
//        when (gridCells) {
//            is GridCells.Fixed -> gridCells.count
//            is GridCells.Adaptive -> {
//                val minSize = gridCells.minSize.value
//                (screenWidth / (minSize + spacingPx)).toInt().coerceAtLeast(1)
//            }
//        }
//    }

    // 计算当前项目所在的行和列
    val currentRow = currentIndex / cellsPerRow
    val currentColumn = currentIndex % cellsPerRow

    // 计算目标列和行
    var targetColumn = currentColumn + itemsMovedHorizontally

    // 处理换行逻辑
    var targetRow = currentRow
    while (targetColumn < 0) {
        targetColumn += cellsPerRow
        targetRow--
    }

    while (targetColumn >= cellsPerRow) {
        targetColumn -= cellsPerRow
        targetRow++
    }

    // 计算目标索引
    val targetIndex = targetRow * cellsPerRow + targetColumn

    // 边界检查
    return if (targetIndex in 0 until totalItems) targetIndex else -1
}

// 扩展函数：实现可拖动的项目
@SuppressLint("ModifierFactoryUnreferencedReceiver")
@Composable
private fun Modifier.gridDraggableItem(
    index: Int,
    isDragging: Boolean,
    onDragStarted: () -> Unit,
    onDragDelta: (Offset) -> Unit,
    onDragEnd: () -> Unit
) = composed {
    var dragStartOffset by remember { mutableStateOf(Offset.Zero) }

    pointerInput(Unit) {
        awaitPointerEventScope {
            while (true) {
                val down = awaitFirstDown(requireUnconsumed = false)
                if (!isDragging) {
                    onDragStarted()
                    dragStartOffset = down.position

                    // 等待拖动事件
                    var currentDrag: PointerInputChange? = null
                    do {
                        currentDrag = awaitPointerEvent().changes.firstOrNull()
                        currentDrag?.let {
                            if (!it.isConsumed) {
                                val dragDelta = it.position - dragStartOffset
                                onDragDelta(dragDelta)
                                it.consume()
                            }
                        }
                    } while (currentDrag != null && currentDrag.pressed)

                    onDragEnd()
                }
            }
        }
    }
}

// 扩展函数：交换列表中两个位置的元素
private fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    if (index1 in indices && index2 in indices) {
        val temp = this[index1]
        this[index1] = this[index2]
        this[index2] = temp
    }
}