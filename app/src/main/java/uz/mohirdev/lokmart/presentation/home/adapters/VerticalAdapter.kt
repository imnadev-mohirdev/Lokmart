package uz.mohirdev.lokmart.presentation.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.mohirdev.lokmart.data.api.product.dto.Product
import uz.mohirdev.lokmart.databinding.ItemProductBinding
import uz.mohirdev.lokmart.presentation.products.ProductViewHolder

class VerticalAdapter(
    private val products: List<Product>,
    private val onClick: (product: Product) -> Unit,
    private val like: (product: Product) -> Unit
) : RecyclerView.Adapter<ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ProductViewHolder(
        ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position], onClick, like)
    }

    override fun getItemCount() = products.size
}