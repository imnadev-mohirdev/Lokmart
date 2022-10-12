package uz.mohirdev.lokmart.data.api.auth.dto

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("user")
    val user: UserDto,
    @SerializedName("token")
    val token: String
)
