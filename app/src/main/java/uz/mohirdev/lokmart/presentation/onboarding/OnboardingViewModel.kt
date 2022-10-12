package uz.mohirdev.lokmart.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.mohirdev.lokmart.domain.repo.AuthRepository
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    fun onboarded() = viewModelScope.launch {
        authRepository.onboarded()
    }
}