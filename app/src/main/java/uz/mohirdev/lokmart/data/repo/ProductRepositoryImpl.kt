package uz.mohirdev.lokmart.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.flow.map
import uz.mohirdev.lokmart.data.api.product.ProductApi
import uz.mohirdev.lokmart.data.api.product.dto.HomeResponse
import uz.mohirdev.lokmart.data.api.product.paging.ProductPagingSource
import uz.mohirdev.lokmart.data.store.CartStore
import uz.mohirdev.lokmart.data.store.RecentsStore
import uz.mohirdev.lokmart.data.store.UserStore
import uz.mohirdev.lokmart.domain.model.Cart
import uz.mohirdev.lokmart.domain.model.ProductQuery
import uz.mohirdev.lokmart.domain.repo.ProductRepository
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val productApi: ProductApi,
    private val userStore: UserStore,
    private val recentsStore: RecentsStore,
    private val cartStore: CartStore
) : ProductRepository {

    override suspend fun getHome(): HomeResponse {
        val response = productApi.getHome()
        userStore.set(response.user)
        return response
    }

    override suspend fun getCategories() = productApi.getCategories()

    override fun getProducts(query: ProductQuery) = Pager(
        config = PagingConfig(
            pageSize = 10,
            prefetchDistance = 10,
            enablePlaceholders = false,
            initialLoadSize = 20
        ),
        initialKey = 0,
        pagingSourceFactory = {
            ProductPagingSource(productApi, query)
        }
    ).flow

    override fun getRecents() = recentsStore.getFlow().map { it?.toList() ?: emptyList() }

    override suspend fun clearRecents() = recentsStore.clear()

    override suspend fun addRecent(search: String) {
        val recents = (recentsStore.get() ?: emptyArray()).toMutableList()
        recents.remove(search)
        recents.add(0, search)
        recentsStore.set(recents.toTypedArray())
    }

    override suspend fun getProduct(id: String) = productApi.getProduct(id)

    override suspend fun toggleWishlist(productId: String, wishlist: Boolean) {
        productApi.toggleWishlist(productId, wishlist)
    }

    override suspend fun setCart(cart: Cart) {
        val carts = (cartStore.get() ?: emptyArray())
            .toList()
            .filterNot { it.id == cart.id }
            .toMutableList()
        if (cart.count > 0) {
            carts.add(cart)
        }
        cartStore.set(carts.toTypedArray())
    }

    override fun getCarts() = cartStore.getFlow().map { it?.toList() ?: emptyList() }

    override suspend fun clearCart() = cartStore.clear()
}