package uz.mohirdev.lokmart.domain.repo

import kotlinx.coroutines.flow.Flow
import uz.mohirdev.lokmart.domain.model.Destination

interface AuthRepository {
    suspend fun signUp(username: String, password: String)
    suspend fun signUp(username: String, email: String, password: String)
    fun destinationFlow() : Flow<Destination>
    suspend fun onboarded()
}