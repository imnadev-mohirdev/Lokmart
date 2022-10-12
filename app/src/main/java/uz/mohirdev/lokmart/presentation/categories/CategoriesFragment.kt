package uz.mohirdev.lokmart.presentation.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import uz.mohirdev.lokmart.data.api.product.dto.Category
import uz.mohirdev.lokmart.databinding.FragmentCategoriesBinding
import uz.mohirdev.lokmart.presentation.home.HomeFragmentDirections
import uz.mohirdev.lokmart.util.BaseFragment

@AndroidEntryPoint
class CategoriesFragment : BaseFragment<FragmentCategoriesBinding>(FragmentCategoriesBinding::inflate) {

    private val viewModel by viewModels<CategoriesViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() = with(binding) {
        viewModel.loading.observe(viewLifecycleOwner) {
            loading.root.isVisible = it
        }
        viewModel.error.observe(viewLifecycleOwner) {
            error.root.isVisible = it
        }
        viewModel.categories.observe(viewLifecycleOwner) {
            categories.adapter = CategoriesAdapter(it, this@CategoriesFragment::onCategoryClick)
        }
    }

    private fun initUI() = with(binding) {
        back.setOnClickListener {
            findNavController().popBackStack()
        }
        error.retry.setOnClickListener {
            viewModel.getCategories()
        }
    }

    private fun onCategoryClick(category: Category) {
        findNavController().navigate(CategoriesFragmentDirections.toProductsFragment(category))
    }
}