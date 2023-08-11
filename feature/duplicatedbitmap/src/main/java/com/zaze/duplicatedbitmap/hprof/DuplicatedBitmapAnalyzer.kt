package com.zaze.duplicatedbitmap.hprof

import android.util.Log
import com.squareup.haha.perflib.ArrayInstance
import com.squareup.haha.perflib.ClassInstance
import com.squareup.haha.perflib.HahaHelper
import com.squareup.haha.perflib.Instance
import com.squareup.haha.perflib.StackTrace
import java.io.File
import java.lang.reflect.Field
import java.util.Arrays


class DuplicatedBitmapAnalyzer {
    private var mMStackField: Field? = null
    private var mMLengthField: Field? = null
    private var mMValueOffsetField: Field? = null

    fun analyzer(hprofFile: File): Int {
        // 打开hprof文件
        val heapSnapshot: HeapSnapshot = HeapSnapshot(hprofFile)
        // 获得snapshot
        val snapshot = heapSnapshot.snapshot
        // 获得Bitmap Class
        val bitmapClass = snapshot.findClass("android.graphics.Bitmap")
        // 保存 bitmap.mBuffer 和 bitmap实例 的映射关系。
        val byteArrayToBitmapMap = HashMap<ArrayInstance, Instance>()
        // 获得heap, 只需要分析 app 和 default heap 即可
        snapshot.heaps.filter {
            "default" == it.name || "app" == it.name
        }.map {
            // 拿到 heap 中所有的 Bitmap 实例
            bitmapClass.getHeapInstances(it.id)
        }.filter {
            !it.isNullOrEmpty()
        }.forEach { bitmapInstances ->
//            val buffer = IntArray(bitmapInstances.size)
            bitmapInstances.filter {// 过滤 GC 不可达的。
                it.distanceToGcRoot != Integer.MAX_VALUE
            }.forEach { bitmapInstance ->
                // 从Bitmap实例中获得buffer数组
                var mBuffer = HahaHelper.fieldValue<ArrayInstance>(
                    (bitmapInstance as ClassInstance).values,
                    "mBuffer"
                )
                mBuffer?.let {
                    if (byteArrayToBitmapMap.containsKey(it)) {
                        // 已存在，拷贝数据并重新创建一个对象。
                        mBuffer = cloneArrayInstance(it)
                    }
                    byteArrayToBitmapMap[mBuffer] = bitmapInstance
                } ?: Log.i("DuplicatedBitmap", "Skiped a no-data bitmap")

            }
        }

        if (byteArrayToBitmapMap.isEmpty()) {
            return -1
        }
        // 计算 mBuffer hashcode ，判断重复
        val resultMap = HashMap<Int, MutableList<Instance>>()
        byteArrayToBitmapMap.keys.forEach { buffer ->
            val hashCode = Arrays.hashCode(buffer.values)
            if (!resultMap.containsKey(hashCode)) {
                resultMap[hashCode] = mutableListOf()
            }
            byteArrayToBitmapMap[buffer]?.let {
                resultMap[hashCode]?.add(it)
            }
        }
        resultMap.filter {
            it.value.size > 1
        }.forEach { entry ->
            val bitmapInstances = entry.value
            Log.i("DuplicatedBitmap", "duplcate count: ${bitmapInstances.size}")
            bitmapInstances.forEachIndexed { index, instance ->
                Log.i(
                    "DuplicatedBitmap",
                    "stacks: ${getTraceString(getTraceFromInstance(instance))}"
                )
                if (index == 0) {
                    Log.i(
                        "DuplicatedBitmap",
                        "width: ${
                            HahaHelper.fieldValue<Int>(
                                (instance as ClassInstance).values,
                                "mWidth"
                            )
                        }"
                    )
                    Log.i(
                        "DuplicatedBitmap",
                        "height: ${HahaHelper.fieldValue<Int>(instance.values, "mHeight")}"
                    )
                    Log.i("DuplicatedBitmap", "size: ${instance.size}")
                }
            }
        }
        return 0
    }


    private fun cloneArrayInstance(orig: ArrayInstance): ArrayInstance? {
        return try {
            if (mMStackField == null) {
                mMStackField = Instance::class.java.getDeclaredField("mStack")
                mMStackField?.isAccessible = true
            }
            val stack = mMStackField?.get(orig) as? StackTrace
            if (mMLengthField == null) {
                mMLengthField = ArrayInstance::class.java.getDeclaredField("mLength")
                mMLengthField?.isAccessible = true
            }
            val length = mMLengthField?.getInt(orig) ?: 0
            if (mMValueOffsetField == null) {
                mMValueOffsetField = ArrayInstance::class.java.getDeclaredField("mValuesOffset")
                mMValueOffsetField?.isAccessible = true
            }
            val valueOffset: Long = mMValueOffsetField?.getLong(orig) ?: 0
            //
            val result = ArrayInstance(orig.id, stack, orig.arrayType, length, valueOffset)
            result.heap = orig.heap
            result
        } catch (thr: Throwable) {
            thr.printStackTrace()
            null
        }
    }

    fun getTraceFromInstance(instance: Instance?): ArrayList<Instance> {
        var nextInstance = instance
        val arrayList = ArrayList<Instance>()
        while (nextInstance != null && nextInstance.distanceToGcRoot != 0 && nextInstance.distanceToGcRoot != Int.MAX_VALUE) {
            arrayList.add(nextInstance)
            nextInstance = nextInstance.nextInstanceToGcRoot
        }
        return arrayList
    }

    fun getTraceString(instances: List<Instance>): String {
        val sb = StringBuilder()
        for (instance in instances) {
            sb.append(instance.classObj.className)
            sb.append("\n")
        }
        return sb.toString()
    }

}