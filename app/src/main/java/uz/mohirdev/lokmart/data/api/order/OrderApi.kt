package uz.mohirdev.lokmart.data.api.order

import com.google.gson.JsonElement
import org.json.JSONObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import uz.mohirdev.lokmart.data.api.order.dto.Billing
import uz.mohirdev.lokmart.data.api.order.dto.CartRequest
import uz.mohirdev.lokmart.data.api.order.dto.OrderDto
import uz.mohirdev.lokmart.data.api.order.dto.Track
import uz.mohirdev.lokmart.domain.model.Status

interface OrderApi {

    @POST("orders/get-billing")
    suspend fun getBilling(@Body request: CartRequest) : Billing

    @POST("orders")
    suspend fun createOrder(@Body request: CartRequest)

    @GET("orders")
    suspend fun getOrder(
        @Query("status") status : Status,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : List<OrderDto>

    @GET("orders/{id}/track")
    suspend fun getTrack(
        @Path("id") id: Int
    ) : Track

    @GET("https://maps.googleapis.com/maps/api/directions/json")
    suspend fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("waypoints") waypoints: String,
        @Query("key") key: String = "AIzaSyAJVPX01ZaZi1NlLBmL-sFBOci-1fo3Eas"
    ) : JsonElement
}