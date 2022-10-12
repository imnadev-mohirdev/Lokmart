package uz.mohirdev.lokmart.presentation.wishlist

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mohirdev.lokmart.data.api.product.dto.Product
import uz.mohirdev.lokmart.domain.model.ProductQuery
import uz.mohirdev.lokmart.domain.repo.ProductRepository
import javax.inject.Inject

@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    val loading = MutableLiveData(false)
    val error = MutableLiveData(false)
    val products = MutableLiveData<PagingData<Product>>()

    init {
        getProducts()
    }

    fun getProducts() = viewModelScope.launch {
        val query = ProductQuery(favorite = true)
        productRepository.getProducts(query).cachedIn(viewModelScope).collectLatest {
            products.postValue(it)
        }
    }

    fun setLoadStates(states: CombinedLoadStates) {
        val loading = states.source.refresh is LoadState.Loading
        this.loading.postValue(loading)
    }
}