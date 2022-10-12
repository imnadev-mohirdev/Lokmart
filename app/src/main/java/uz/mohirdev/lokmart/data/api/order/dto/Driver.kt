package uz.mohirdev.lokmart.data.api.order.dto


import com.google.gson.annotations.SerializedName

data class Driver(
    @SerializedName("id")
    val id: Int,
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("number")
    val number: String
)