package uz.mohirdev.lokmart.data.repo

import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import uz.mohirdev.lokmart.data.api.auth.AuthApi
import uz.mohirdev.lokmart.data.api.auth.dto.AuthResponse
import uz.mohirdev.lokmart.data.api.auth.dto.SignInRequest
import uz.mohirdev.lokmart.data.api.auth.dto.SignUpRequest
import uz.mohirdev.lokmart.data.store.OnboardedStore
import uz.mohirdev.lokmart.data.store.TokenStore
import uz.mohirdev.lokmart.data.store.UserStore
import uz.mohirdev.lokmart.domain.model.Destination
import uz.mohirdev.lokmart.domain.repo.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val tokenStore: TokenStore,
    private val userStore: UserStore,
    private val onboardedStore: OnboardedStore
) : AuthRepository {

    override suspend fun signUp(username: String, password: String) {
        val request = SignInRequest(username, password)
        val response = authApi.signIn(request)
        saveUserInfo(response)
    }

    override suspend fun signUp(username: String, email: String, password: String) {
        val request = SignUpRequest(username, email, password)
        val response = authApi.signUp(request)
        saveUserInfo(response)
    }

    override fun destinationFlow() = channelFlow {
        suspend fun sendDestination() {
            when {
                tokenStore.get() != null -> send(Destination.Home)
                onboardedStore.get() == true -> send(Destination.Auth)
                else -> send(Destination.Onboarding)
            }
        }
        launch {
            tokenStore.getFlow().collectLatest {
                sendDestination()
            }
        }
        launch {
            onboardedStore.getFlow().collectLatest {
                sendDestination()
            }
        }
    }.distinctUntilChanged()

    override suspend fun onboarded() = onboardedStore.set(true)

    private suspend fun saveUserInfo(response: AuthResponse) {
        tokenStore.set(response.token)
        userStore.set(response.user)
    }
}