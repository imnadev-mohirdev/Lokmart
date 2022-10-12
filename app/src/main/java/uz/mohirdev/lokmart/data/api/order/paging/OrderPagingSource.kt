package uz.mohirdev.lokmart.data.api.order.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import uz.mohirdev.lokmart.data.api.order.OrderApi
import uz.mohirdev.lokmart.domain.model.Order
import uz.mohirdev.lokmart.domain.model.Status
import java.text.SimpleDateFormat
import java.util.*

class OrderPagingSource(
    private val status: Status,
    private val api: OrderApi
) : PagingSource<Int, Order>(){

    val serverFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    val orderFormat get() = SimpleDateFormat("h:MM a, d, MMMM yyyy", Locale.getDefault())

    override fun getRefreshKey(state: PagingState<Int, Order>): Int? = null

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Order> {
        val page = params.key ?: 0

        return try {
            val orders = api.getOrder(status, page, params.loadSize)
            LoadResult.Page(
                data = orders.map { it.toOrder(serverFormat, orderFormat) },
                prevKey = (page - 1).takeIf { it > 0 },
                nextKey = (page + 1).takeIf { orders.isNotEmpty() }
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}