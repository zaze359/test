package com.zaze.logic.core.model

import kotlinx.serialization.Serializable

/**
 * 跨平台设备信息模型，使用 kotlinx.serialization 而非 Android Parcelable。
 * 作为从 core:model (DeviceStatus 等) 迁入 shareLogic 的示范。
 */
@Serializable
data class DeviceInfo(
    val name: String,
    val sdkInt: Int,
    val isEmulator: Boolean,
) {
    fun describe(): String = "Device(name=$name, sdk=$sdkInt, emulator=$isEmulator)"
}
