package uz.mohirdev.lokmart.presentation.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import uz.mohirdev.lokmart.R
import uz.mohirdev.lokmart.databinding.ItemCartBinding
import uz.mohirdev.lokmart.domain.model.Cart
import uz.mohirdev.lokmart.util.dp

class CartAdapter(
    private val carts: List<Cart>,
    private val onClick: (cart: Cart) -> Unit,
    private val set: (cart: Cart) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemCartBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cart: Cart) = with(binding) {
            Glide.with(root).load(cart.image).transform(CenterCrop(), RoundedCorners(14.dp)).into(image)
            title.text = cart.title
            category.text = cart.category
            price.text = root.context.getString(R.string.price, cart.price - (cart.discount ?: 0.0))
            old.text = root.context.getString(R.string.price_striked, cart.price)
            old.isVisible = cart.discount != null
            count.text = cart.count.toString()
            root.setOnClickListener {
                onClick(cart)
            }
            plus.setOnClickListener {
                if (cart.count == cart.stock) return@setOnClickListener
                cart.count++
                count.text = cart.count.toString()
                set(cart)
            }
            minus.setOnClickListener {
                cart.count--
                count.text = cart.count.toString()
                set(cart)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(carts[position])
    }

    override fun getItemCount() = carts.size
}