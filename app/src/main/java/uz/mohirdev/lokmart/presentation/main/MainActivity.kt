package uz.mohirdev.lokmart.presentation.main

import android.os.Bundle
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.forEachIndexed
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionManager
import dagger.hilt.android.AndroidEntryPoint
import uz.mohirdev.lokmart.MainDirections
import uz.mohirdev.lokmart.R
import uz.mohirdev.lokmart.databinding.ActivityMainBinding
import uz.mohirdev.lokmart.domain.model.Destination


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

    private val navController get() = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        subscribeToLiveData()
    }

    private fun initUI() = with(binding) {
        navigation.setupWithNavController(navController)

        navigation.setOnItemSelectedListener {

            var index: Int = 0
            navigation.menu.forEachIndexed { itemIndex, item ->
                if (it.itemId != item.itemId) return@forEachIndexed
                index = itemIndex
            }

            ConstraintSet().apply {
                clone(indicatorContainer)
                setHorizontalBias(indicator.id, index * 0.25F)

                val transition: Transition = ChangeBounds()
                transition.interpolator = DecelerateInterpolator(1.0f)
                transition.duration = 500

                TransitionManager.beginDelayedTransition(indicatorContainer, transition)

                applyTo(indicatorContainer)
            }

            NavigationUI.onNavDestinationSelected(it, navController)

            return@setOnItemSelectedListener false
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            navigation.isVisible = listOf(
                R.id.onboardingFragment,
                R.id.signInFragment,
                R.id.signUpFragment,
                R.id.detailFragment,
                R.id.mapFragment
            ).all { it != destination.id }
        }
    }

    private fun subscribeToLiveData() {
        viewModel.events.observe(this) {
            when (it) {
                is MainViewModel.Event.NavigateTo -> navigateTo(it.destination)
            }
        }
    }

    private fun navigateTo(destination: Destination) {
        if (navController.currentDestination?.id == R.id.detailFragment) return
        when (destination) {
            Destination.Auth -> navController.navigate(MainDirections.toSignInFragment())
            Destination.Home -> navController.navigate(MainDirections.toHomeFragment())
            Destination.Onboarding -> navController.navigate(MainDirections.toOnboardingFragment())
        }
    }
}