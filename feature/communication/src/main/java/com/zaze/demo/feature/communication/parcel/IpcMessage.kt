package com.zaze.demo.feature.communication.parcel

import android.os.Parcel
import android.os.Parcelable

class IpcMessage(
    var id: Int = 0,
    var message: String? = null
) : Parcelable {

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        message = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(message)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IpcMessage> {
        override fun createFromParcel(parcel: Parcel): IpcMessage {
            return IpcMessage(parcel)
        }

        override fun newArray(size: Int): Array<IpcMessage?> {
            return arrayOfNulls(size)
        }
    }

    override fun toString(): String {
        return "IpcMessage(id=$id, message=$message)"
    }
}