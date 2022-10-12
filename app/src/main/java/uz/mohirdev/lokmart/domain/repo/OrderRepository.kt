package uz.mohirdev.lokmart.domain.repo

import androidx.paging.PagingData
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import uz.mohirdev.lokmart.data.api.order.dto.Billing
import uz.mohirdev.lokmart.data.api.order.dto.Track
import uz.mohirdev.lokmart.domain.model.Order
import uz.mohirdev.lokmart.domain.model.Status
import java.util.HashMap

interface OrderRepository {

    fun getBilling(promo: String? = null) : Flow<Billing>

    suspend fun createOrder(promo: String? = null)

    fun getOrders(status: Status) : Flow<PagingData<Order>>

    suspend fun getTrack(id: Int) : Track

    fun getDirections(track: Track) : Flow<Pair<LatLng, List<List<HashMap<String, String>>>>>
}