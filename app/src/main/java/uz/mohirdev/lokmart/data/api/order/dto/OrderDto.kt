package uz.mohirdev.lokmart.data.api.order.dto


import com.google.gson.annotations.SerializedName
import uz.mohirdev.lokmart.domain.model.Order
import java.text.SimpleDateFormat

data class OrderDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("items")
    val items: Int,
    @SerializedName("placed")
    val placed: String,
    @SerializedName("confirmed")
    val confirmed: String?,
    @SerializedName("cancelled")
    val cancelled: String?,
    @SerializedName("delivering")
    val delivering: String?,
    @SerializedName("delivered")
    val delivered: String?
) {

    fun toOrder(
        serverFormat: SimpleDateFormat,
        orderFormat: SimpleDateFormat
    ) = Order(
        id = id,
        items = items,
        placed = orderFormat.format(serverFormat.parse(placed)!!),
        confirmed = confirmed?.let { orderFormat.format(serverFormat.parse(it)!!) },
        cancelled = cancelled?.let { orderFormat.format(serverFormat.parse(it)!!) },
        delivering = delivering?.let { orderFormat.format(serverFormat.parse(it)!!) },
        delivered = delivered?.let { orderFormat.format(serverFormat.parse(it)!!) },
    )
}