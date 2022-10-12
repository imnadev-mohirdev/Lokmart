package uz.mohirdev.lokmart.presentation.wishlist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.mohirdev.lokmart.data.api.product.dto.Product
import uz.mohirdev.lokmart.databinding.FragmentWishlistBinding
import uz.mohirdev.lokmart.presentation.products.ProductsAdapter
import uz.mohirdev.lokmart.util.BaseFragment

@AndroidEntryPoint
class WishlistFragment : BaseFragment<FragmentWishlistBinding>(FragmentWishlistBinding::inflate) {

    private val viewModel by viewModels<WishlistViewModel>()
    private val adapter by lazy { ProductsAdapter(this::onClick, this::like) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter.addLoadStateListener {
            viewModel.setLoadStates(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        subscribeToLiveData()
    }

    private fun initUI() = with(binding) {
        error.retry.setOnClickListener {
            viewModel.getProducts()
        }

        products.adapter = adapter

        search.setOnClickListener {
            findNavController().navigate(WishlistFragmentDirections.toSearchFragment())
        }

        swipe.setOnRefreshListener {
            viewModel.getProducts()
        }
    }

    private fun subscribeToLiveData() = with(binding) {
        viewModel.loading.observe(viewLifecycleOwner) {
            loading.root.isVisible = it && swipe.isRefreshing.not()
            if (it.not()) { swipe.isRefreshing = false }
        }
        viewModel.error.observe(viewLifecycleOwner) {
            error.root.isVisible = it
        }
        viewModel.products.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                adapter.submitData(it)
            }
        }
    }

    private fun onClick(product: Product) {
        findNavController().navigate(WishlistFragmentDirections.toDetailFragment(product.id))
    }

    private fun like(product: Product) {

    }
}