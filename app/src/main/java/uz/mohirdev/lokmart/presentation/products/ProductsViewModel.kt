package uz.mohirdev.lokmart.presentation.products

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mohirdev.lokmart.data.api.product.dto.Category
import uz.mohirdev.lokmart.data.api.product.dto.Product
import uz.mohirdev.lokmart.domain.model.ProductQuery
import uz.mohirdev.lokmart.domain.repo.ProductRepository
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    val loading = MutableLiveData(false)
    val error = MutableLiveData(false)
    val products = MutableLiveData<PagingData<Product>>()
    val category = MutableLiveData<Category>()

    fun setCategory(category: Category) {
        this.category.postValue(category)
        getProducts()
    }

    fun getProducts() = viewModelScope.launch {
        val query = ProductQuery(category = category.value)
        productRepository.getProducts(query).collectLatest {
            products.postValue(it)
        }
    }

    fun setLoadStates(states: CombinedLoadStates) {
        val loading = states.source.refresh is LoadState.Loading
        this.loading.postValue(loading)
    }

    fun toggleWishlist(product: Product) = viewModelScope.launch{
        try {
            productRepository.toggleWishlist(product.id, product.wishlist)
        } catch (e: Exception) {

        }
    }
}