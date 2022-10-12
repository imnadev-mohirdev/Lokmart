package uz.mohirdev.lokmart.domain.model

sealed class Destination {
    object Home : Destination()
    object Onboarding : Destination()
    object Auth : Destination()
}
