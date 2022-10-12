package uz.mohirdev.lokmart.presentation.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uz.mohirdev.lokmart.data.api.order.dto.Track
import uz.mohirdev.lokmart.domain.repo.OrderRepository
import uz.mohirdev.lokmart.util.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val orderRepository: OrderRepository
) : ViewModel() {

    val track = MutableLiveData<Track>()
    val error = SingleLiveEvent<Unit>()
    val route = MutableLiveData<List<PolylineOptions>>()
    val driver = MutableLiveData<LatLng>()

    fun getTrack(id: Int) = viewModelScope.launch {
        try {
            val result = orderRepository.getTrack(id)
            getDirections(result)
            track.postValue(result)
        } catch (e: Exception) {
            error.call()
        }
    }

    private fun getDirections(track: Track): Job = viewModelScope.launch {
        orderRepository.getDirections(track).collectLatest { pair ->
            driver.postValue(pair.first!!)

            try {
                val lineOptionsList = mutableListOf<PolylineOptions>()

                pair.second.forEach { path ->
                    val lineOptions = PolylineOptions()
                    val points = ArrayList<LatLng>()

                    for (j in path.indices) {
                        val point = path[j]
                        val lat = point["lat"]?.toDouble() ?: continue
                        val lng = point["lng"]?.toDouble() ?: continue
                        val position = LatLng(lat, lng)
                        points.add(position)
                    }

                    lineOptions.addAll(points)
                    lineOptionsList.add(lineOptions)
                }
                route.postValue(lineOptionsList)
            } catch (e: Exception) {
                e.printStackTrace()
                error.call()
            }
        }
    }
}