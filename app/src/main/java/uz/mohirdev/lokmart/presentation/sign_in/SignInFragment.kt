package uz.mohirdev.lokmart.presentation.sign_in

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.mohirdev.lokmart.R
import uz.mohirdev.lokmart.databinding.FragmentSignInBinding
import uz.mohirdev.lokmart.util.BaseFragment
import uz.mohirdev.lokmart.util.clearLightStatusBar
import uz.mohirdev.lokmart.util.toast

@AndroidEntryPoint
class SignInFragment : BaseFragment<FragmentSignInBinding>(FragmentSignInBinding::inflate) {

    private val viewModel by viewModels<SignInViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() = with(binding) {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            progress.isVisible = isLoading
            signIn.text = if (isLoading) null else getString(R.string.fragment_sign_in_button)
        }
        viewModel.events.observe(viewLifecycleOwner) { event ->
            when(event) {
                SignInViewModel.Event.ConnectionError -> toast(R.string.connection_error)
                SignInViewModel.Event.Error -> toast(R.string.error)
                SignInViewModel.Event.InvalidCredentials -> toast(R.string.invalid_credentials)
                SignInViewModel.Event.NavigateToHome -> toast(R.string.app_name)
            }
        }
    }

    private fun initUI() = with(binding) {
        clearLightStatusBar()
        signIn.setOnClickListener {
            viewModel.signIn(username.text.toString(), password.text.toString())
        }

        signUp.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.toSignUpFragment())
        }
    }
}