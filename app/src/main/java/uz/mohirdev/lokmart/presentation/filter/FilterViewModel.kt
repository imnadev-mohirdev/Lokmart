package uz.mohirdev.lokmart.presentation.filter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uz.mohirdev.lokmart.data.api.product.dto.Category
import uz.mohirdev.lokmart.domain.repo.ProductRepository
import uz.mohirdev.lokmart.util.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class FilterViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    val categories = MutableLiveData<List<Category>>()
    val events = SingleLiveEvent<Event>()

    init {
        getCategories()
    }

    fun getCategories() = viewModelScope.launch {
        try {
            val result = productRepository.getCategories()
            categories.postValue(result)
        } catch (e: Exception) {
            events.postValue(Event.CategoriesError)
        }
    }

    sealed class Event {
        object CategoriesError : Event()
    }
}