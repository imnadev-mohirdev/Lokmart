package uz.mohirdev.lokmart.data.api.product

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import uz.mohirdev.lokmart.data.api.product.dto.Category
import uz.mohirdev.lokmart.data.api.product.dto.Detail
import uz.mohirdev.lokmart.data.api.product.dto.HomeResponse
import uz.mohirdev.lokmart.data.api.product.dto.Product

interface ProductApi {

    @GET("home")
    suspend fun getHome() : HomeResponse

    @GET("categories")
    suspend fun getCategories() : List<Category>

    @GET("products")
    suspend fun getProducts(
        @Query("category_id") categoryId: String?,
        @Query("search") search: String?,
        @Query("from") from: Float?,
        @Query("to") to: Float?,
        @Query("rating") rating: Int?,
        @Query("discount") discount: Int?,
        @Query("sort") sort: String?,
        @Query("favorite") favorite: Boolean?,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : List<Product>

    @GET("products/{id}")
    suspend fun getProduct(
        @Path("id") id: String
    ) : Detail

    @GET("products/{id}/toggle-wishlist")
    suspend fun toggleWishlist(
        @Path("id") id: String,
        @Query("wishlist") wishlist: Boolean
    )
}