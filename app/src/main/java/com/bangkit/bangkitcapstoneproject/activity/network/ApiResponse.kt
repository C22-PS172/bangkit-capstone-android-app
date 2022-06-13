package com.bangkit.bangkitcapstoneproject.activity.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class ApiResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("result")
    val result: Result,
)

@Parcelize
data class Result(
    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("scientificName")
    val scientificName: String,

    @field:SerializedName("type")
    val type: String
) : Parcelable