package uz.mohirdev.lokmart.data.api.auth

import retrofit2.http.Body
import retrofit2.http.POST
import uz.mohirdev.lokmart.data.api.auth.dto.SignInRequest
import uz.mohirdev.lokmart.data.api.auth.dto.AuthResponse
import uz.mohirdev.lokmart.data.api.auth.dto.SignUpRequest

interface AuthApi {

    @POST("auth/sign-in")
    suspend fun signIn(@Body request: SignInRequest) : AuthResponse

    @POST("auth/sign-up")
    suspend fun signUp(@Body request: SignUpRequest) : AuthResponse
}