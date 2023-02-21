package com.example.dictionary

import android.os.Parcel
import android.os.Parcelable

data class Word(val en_Word: String?, val meaning: String?, val ex: String?, val syn: String?, val ant: String?, var history: Int?, var favorite: Int?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(en_Word)
        parcel.writeString(meaning)
        parcel.writeString(ex)
        parcel.writeString(syn)
        parcel.writeString(ant)
        parcel.writeValue(history)
        parcel.writeValue(favorite)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Word> {
        override fun createFromParcel(parcel: Parcel): Word {
            return Word(parcel)
        }

        override fun newArray(size: Int): Array<Word?> {
            return arrayOfNulls(size)
        }
    }

}