package uz.mohirdev.lokmart.presentation.cart

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mohirdev.lokmart.data.api.order.dto.Billing
import uz.mohirdev.lokmart.domain.model.Cart
import uz.mohirdev.lokmart.domain.repo.OrderRepository
import uz.mohirdev.lokmart.domain.repo.ProductRepository
import uz.mohirdev.lokmart.util.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    val carts = MutableLiveData<List<Cart>>()
    val events = SingleLiveEvent<Event>()
    val billing = MutableLiveData<Billing>()
    val billingLoading = MutableLiveData(false)
    val loading = MutableLiveData(false)

    init {
        getCarts()
        getBilling()
    }

    private fun getCarts() = viewModelScope.launch {
        productRepository.getCarts().collectLatest {
            carts.postValue(it)
        }
    }

    fun set(cart: Cart) = viewModelScope.launch {
        productRepository.setCart(cart)
    }

    fun clear() = viewModelScope.launch {
        productRepository.clearCart()
    }


    private var job: Job? = null
    fun getBilling(promo: String? = null) {
        job?.cancel()
        job = viewModelScope.launch {
            billingLoading.postValue(true)
            orderRepository.getBilling(promo).catch {
                it.printStackTrace()
                events.postValue(Event.BillingError)
                billingLoading.postValue(false)
            }.collectLatest {
                billingLoading.postValue(false)
                billing.postValue(it)
            }
        }
    }

    fun createOrder(promo: String? = null) =viewModelScope.launch{
        loading.postValue(true)
        try {
            orderRepository.createOrder(promo)
            events.postValue(Event.OrderCreated)
        } catch (e: Exception) {
            events.postValue(Event.OrderError)
        } finally {
            loading.postValue(false)
        }
    }

    sealed class Event {
        object BillingError : Event()
        object OrderError : Event()
        object OrderCreated : Event()
    }
}