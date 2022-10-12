package uz.mohirdev.lokmart.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import uz.mohirdev.lokmart.data.api.product.dto.Category

@Parcelize
data class ProductQuery(
    val category: Category? = null,
    val search: String? = null,
    val range: Pair<Float, Float> = 0f to 10000f,
    val rating: Int? = null,
    val discount: Int? = null,
    val sort: List<Sort> = emptyList(),
    val favorite: Boolean = false
) : Parcelable

enum class Sort {
    discount, voucher, shipping, delivery
}