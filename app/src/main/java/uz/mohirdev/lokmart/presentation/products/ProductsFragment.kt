package uz.mohirdev.lokmart.presentation.products

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.mohirdev.lokmart.data.api.product.dto.Product
import uz.mohirdev.lokmart.databinding.FragmentProductsBinding
import uz.mohirdev.lokmart.presentation.home.HomeFragmentDirections
import uz.mohirdev.lokmart.util.BaseFragment

@AndroidEntryPoint
class ProductsFragment : BaseFragment<FragmentProductsBinding>(FragmentProductsBinding::inflate) {

    private val viewModel by viewModels<ProductsViewModel>()
    private val args by navArgs<ProductsFragmentArgs>()

    private val adapter by lazy { ProductsAdapter(this::onClick, this::wishlist) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setCategory(args.category)

        adapter.addLoadStateListener {
            viewModel.setLoadStates(it)
        }
    }

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
        viewModel.products.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                adapter.submitData(it)
            }
        }
        viewModel.category.observe(viewLifecycleOwner) {
            title.text = it.title
        }
    }

    private fun initUI() = with(binding) {
        back.setOnClickListener {
            findNavController().popBackStack()
        }
        error.retry.setOnClickListener {
            viewModel.getProducts()
        }
        products.adapter = adapter

        searchContainer.search.setOnFocusChangeListener { _, focused ->
            if (focused.not()) return@setOnFocusChangeListener
            findNavController().navigate(HomeFragmentDirections.toSearchFragment())
        }
    }

    private fun onClick(product: Product) {
        findNavController().navigate(ProductsFragmentDirections.toDetailFragment(product.id))
    }

    private fun wishlist(product: Product) {
        viewModel.toggleWishlist(product)
    }
}