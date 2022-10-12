package uz.mohirdev.lokmart.presentation.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.mohirdev.lokmart.data.api.product.dto.Category
import uz.mohirdev.lokmart.data.api.product.dto.Product
import uz.mohirdev.lokmart.domain.model.ProductQuery
import uz.mohirdev.lokmart.domain.repo.ProductRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    val loading = MutableLiveData(false)
    val products = MutableLiveData<PagingData<Product>>()
    val query = MutableLiveData(ProductQuery())
    val recents = MutableLiveData<List<String>>()

    init {
        getRecents()
    }

    private fun getProducts() = viewModelScope.launch {
        productRepository.getProducts(query.value!!).cachedIn(viewModelScope).collect {
            products.postValue(it)
        }
    }

    fun setInitials(category: Category?, wishlist: Boolean) {
        query.postValue(query.value!!.copy(category = category, favorite = wishlist))
        getProducts()
    }

    fun setSearch(search: String) {
        query.postValue(query.value!!.copy(search = search))
        addRecent(search)
        getProducts()
    }

    fun setLoadState(states: CombinedLoadStates) {
        val loading = states.source.refresh is LoadState.Loading
        this.loading.postValue(loading)
    }

    private fun getRecents() = viewModelScope.launch {
        productRepository.getRecents().collect {
            recents.postValue(it)
        }
    }

    fun clearRecents() = viewModelScope.launch {
        productRepository.clearRecents()
    }

    private fun addRecent(search: String) = viewModelScope.launch {
        productRepository.addRecent(search)
    }

    fun setQuery(query: ProductQuery) {
        this.query.value = query
        getProducts()
    }
}