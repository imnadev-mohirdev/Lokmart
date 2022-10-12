package uz.mohirdev.lokmart.presentation.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.mohirdev.lokmart.data.api.product.dto.Category
import uz.mohirdev.lokmart.domain.repo.ProductRepository
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    val loading = MutableLiveData(false)
    val error = MutableLiveData(false)
    val categories = MutableLiveData<List<Category>>()

    init {
        getCategories()
    }

    fun getCategories() = viewModelScope.launch {
        loading.postValue(true)
        error.postValue(false)
        try {
            val response = productRepository.getCategories()
            categories.postValue(response)
        } catch (e: Exception) {
            error.postValue(true)
        } finally {
            loading.postValue(false)
        }
    }
}