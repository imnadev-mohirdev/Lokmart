package uz.mohirdev.lokmart.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView.OnEditorActionListener
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.mohirdev.lokmart.data.api.product.dto.Product
import uz.mohirdev.lokmart.databinding.FragmentSearchBinding
import uz.mohirdev.lokmart.domain.model.ProductQuery
import uz.mohirdev.lokmart.presentation.filter.FilterFragment
import uz.mohirdev.lokmart.presentation.search.SearchFragmentDirections.toFilterFragment
import uz.mohirdev.lokmart.presentation.search.adapters.RecentsAdapter
import uz.mohirdev.lokmart.presentation.search.adapters.SearchProductsAdapter
import uz.mohirdev.lokmart.util.BaseFragment
import uz.mohirdev.lokmart.util.hideKeyboard
import uz.mohirdev.lokmart.util.showKeyboard


@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val viewModel by viewModels<SearchViewModel>()
    private val args by navArgs<SearchFragmentArgs>()
    private val adapter by lazy { SearchProductsAdapter(this::onProductClick, this::like) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.setInitials(args.category, args.wishlist)

        adapter.addLoadStateListener {
            viewModel.setLoadState(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() = with(binding) {
        viewModel.loading.observe(viewLifecycleOwner) {
            loadingLayout.root.isVisible = it
        }
        viewModel.products.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                adapter.submitData(it)
            }
        }
        viewModel.recents.observe(viewLifecycleOwner) {
            recents.adapter = RecentsAdapter(it, this@SearchFragment::onRecentClick)
            isRecentsVisible(searchContainer.search.hasFocus())
        }
    }

    private fun initUI() = with(binding) {
        searchContainer.search.requestFocus()
        showKeyboard()

        close.setOnClickListener {
            findNavController().popBackStack()
        }

        products.adapter = adapter

        searchContainer.search.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.setSearch(searchContainer.search.text.toString())

                viewLifecycleOwner.lifecycleScope.launch {
                    adapter.submitData(PagingData.empty())
                }

                hideKeyboard()
                searchContainer.search.clearFocus()
                return@OnEditorActionListener true
            }
            false
        })

        searchContainer.search.setOnFocusChangeListener { _, focused ->
            isRecentsVisible(focused)
        }

        clear.setOnClickListener {
            viewModel.clearRecents()
        }

        searchContainer.filter.setOnClickListener {
            val query = viewModel.query.value ?: ProductQuery()
            findNavController().navigate(toFilterFragment(query))
        }

        setFragmentResultListener(FilterFragment.REQUEST_KEY) { _, result ->
            val query =  result.getParcelable<ProductQuery>(FilterFragment.RESULT_KEY)
            viewModel.setQuery(query ?: return@setFragmentResultListener)
            searchContainer.search.clearFocus()
            hideKeyboard()
            isRecentsVisible(false)
        }
    }

    private fun FragmentSearchBinding.isRecentsVisible(focused: Boolean) {
        listOf(recents, recentTitle, clear).forEach {
            it.isVisible = viewModel.recents.value.isNullOrEmpty().not() && focused
        }
    }

    private fun onProductClick(product: Product) {
        findNavController().navigate(SearchFragmentDirections.toDetailFragment(product.id))
    }

    private fun like(product: Product) {

    }

    private fun onRecentClick(recent: String) {
        viewModel.setSearch(recent)
        binding.searchContainer.search.setText(recent)
    }
}