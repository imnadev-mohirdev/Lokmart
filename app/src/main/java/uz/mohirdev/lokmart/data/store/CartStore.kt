package uz.mohirdev.lokmart.data.store

import uz.mohirdev.lokmart.domain.model.Cart
import javax.inject.Inject

class CartStore @Inject constructor() : BaseStore<Array<Cart>>("cart", Array<Cart>::class.java)