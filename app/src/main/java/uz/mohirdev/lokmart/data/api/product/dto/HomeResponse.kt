package uz.mohirdev.lokmart.data.api.product.dto


import com.google.gson.annotations.SerializedName
import uz.mohirdev.lokmart.data.api.auth.dto.UserDto

data class HomeResponse(
    @SerializedName("banners")
    val banners: List<Banner>,
    @SerializedName("categories")
    val categories: List<Category>,
    @SerializedName("sections")
    val sections: List<Section>,
    @SerializedName("user")
    val user: UserDto,
    @SerializedName("notification_count")
    val notificationCount : Int
)