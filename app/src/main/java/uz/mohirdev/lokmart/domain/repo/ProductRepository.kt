package uz.mohirdev.lokmart.domain.repo

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uz.mohirdev.lokmart.data.api.product.dto.Category
import uz.mohirdev.lokmart.data.api.product.dto.Detail
import uz.mohirdev.lokmart.data.api.product.dto.HomeResponse
import uz.mohirdev.lokmart.data.api.product.dto.Product
import uz.mohirdev.lokmart.domain.model.Cart
import uz.mohirdev.lokmart.domain.model.ProductQuery

interface ProductRepository {
    suspend fun getHome() : HomeResponse
    suspend fun getCategories() : List<Category>
    fun getProducts(query: ProductQuery) : Flow<PagingData<Product>>
    fun getRecents() : Flow<List<String>>
    suspend fun clearRecents()
    suspend fun addRecent(search: String)
    suspend fun getProduct(id: String) : Detail
    suspend fun toggleWishlist(productId: String, wishlist: Boolean)

    suspend fun setCart(cart: Cart)
    fun getCarts() : Flow<List<Cart>>
    suspend fun clearCart()
}