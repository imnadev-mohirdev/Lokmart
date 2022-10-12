package uz.mohirdev.lokmart.presentation.orders

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
import uz.mohirdev.lokmart.domain.model.Order
import uz.mohirdev.lokmart.domain.model.Status
import uz.mohirdev.lokmart.domain.repo.OrderRepository
import javax.inject.Inject

@HiltViewModel
class OrdersViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    val loading = MutableLiveData(false)
    val error = MutableLiveData(false)
    val empty = MutableLiveData(false)
    val orders = MutableLiveData<PagingData<Order>?>()
    var mStatus: Status = Status.all

    init {
        getOrders()
    }

    fun getOrders() = viewModelScope.launch {
        loading.postValue(true)
        orders.postValue(null)
        error.postValue(false)
        orderRepository.getOrders(mStatus).cachedIn(viewModelScope).collectLatest {
            orders.postValue(it)
        }
    }

    fun setLoadStates(states: CombinedLoadStates, count: Int) {
        val loading = states.source.refresh is LoadState.Loading
        val error = states.source.refresh is LoadState.Error
        val empty = loading.not() && error.not() && count == 0
        this.loading.postValue(loading)
        this.empty.postValue(empty)
        this.error.postValue(error)
    }

    fun setStatus(status: Status) {
        mStatus = status
        getOrders()
    }
}