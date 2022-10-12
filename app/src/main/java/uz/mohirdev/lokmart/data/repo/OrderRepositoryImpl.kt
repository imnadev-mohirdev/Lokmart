package uz.mohirdev.lokmart.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.*
import org.json.JSONObject
import uz.mohirdev.lokmart.data.api.order.OrderApi
import uz.mohirdev.lokmart.data.api.order.dto.CartDto
import uz.mohirdev.lokmart.data.api.order.dto.CartRequest
import uz.mohirdev.lokmart.data.api.order.dto.Track
import uz.mohirdev.lokmart.data.api.order.paging.OrderPagingSource
import uz.mohirdev.lokmart.data.socket.WebsocketClient
import uz.mohirdev.lokmart.data.store.CartStore
import uz.mohirdev.lokmart.domain.model.Status
import uz.mohirdev.lokmart.domain.repo.OrderRepository
import uz.mohirdev.lokmart.util.DirectionsJSONParser
import uz.mohirdev.lokmart.util.toLatLng
import java.net.URI
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderApi: OrderApi,
    private val cartStore: CartStore
) : OrderRepository {

    override fun getBilling(promo: String?) = channelFlow {
        cartStore.getFlow().distinctUntilChanged().collectLatest { carts ->
            carts ?: return@collectLatest
            val request = CartRequest(
                cart = carts.map { CartDto(id = it.id, count = it.count) },
                promoCode = promo
            )
            val billing = orderApi.getBilling(request)
            send(billing)
        }
    }

    override suspend fun createOrder(promo: String?) {
        val carts = cartStore.get() ?: return
        val request = CartRequest(
            cart = carts.map { CartDto(id = it.id, count = it.count) },
            promoCode = promo
        )
        orderApi.createOrder(request)
        cartStore.clear()
    }

    override fun getOrders(status: Status) = Pager(
        config = PagingConfig(pageSize = 10, prefetchDistance = 20, enablePlaceholders = false),
        initialKey = 0,
        pagingSourceFactory = { OrderPagingSource(status, orderApi) }
    ).flow

    override suspend fun getTrack(id: Int) = orderApi.getTrack(id)

    private fun getDriverLocation(server: String) = callbackFlow {
        val client = WebsocketClient(URI(server)) {
            trySend(it)
        }
        client.connect()

        awaitClose {
            client.close()
        }
    }

    override fun getDirections(track: Track) = channelFlow {
        getDriverLocation(track.server).collectLatest {
            val element = orderApi.getDirections(
                "${track.from.lat}, ${track.from.lon}",
                "${track.to.lat}, ${track.to.lon}",
                it
            )
            val parser = DirectionsJSONParser()
            send(it.toLatLng() to parser.parse(JSONObject(element.toString())))
        }
    }


}