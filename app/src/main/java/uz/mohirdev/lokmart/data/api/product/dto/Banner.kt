package uz.mohirdev.lokmart.data.api.product.dto


import com.google.gson.annotations.SerializedName

data class Banner(
    @SerializedName("category")
    val category: Category,
    @SerializedName("image")
    val image: String,
    @SerializedName("product")
    val product: Product
)