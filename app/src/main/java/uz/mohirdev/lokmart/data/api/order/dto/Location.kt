package uz.mohirdev.lokmart.data.api.order.dto


import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lon")
    val lon: Double
) {

    val latLng get() = LatLng(lat, lon)
}