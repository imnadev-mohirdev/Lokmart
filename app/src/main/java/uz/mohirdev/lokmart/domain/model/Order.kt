package uz.mohirdev.lokmart.domain.model

import androidx.annotation.ColorRes
import uz.mohirdev.lokmart.R

data class Order(
    val id: Int,
    val items: Int,
    val placed: String,
    val confirmed: String?,
    val cancelled: String?,
    val delivering: String?,
    val delivered: String?,
    var expanded: Boolean = false
) {

    val background: Int
        get() = when {
            delivered != null -> R.color.green
            cancelled != null -> R.color.red
            else -> R.color.on_delivery_background
        }

    val foreground: Int
        get() = when {
            delivered != null || cancelled != null -> R.color.white
            else -> R.color.yellow
        }

    val status: Int
        get() = when {
            cancelled != null -> R.string.status_cancelled
            delivered != null -> R.string.status_completed
            delivering != null -> R.string.status_delivering
            confirmed != null -> R.string.status_confirmed
            else -> R.string.status_placed
        }

    val steps: List<Step>
        get() {
            val list = mutableListOf<Step>()

            list.add(Step(R.string.step_order_placed, placed, R.drawable.ic_step_checked))

            if (cancelled != null) {
                list.add(Step(R.string.step_cancelled, cancelled, R.drawable.ic_cancelled))
                return list
            }

            if (confirmed != null) {
                list.add(Step(R.string.step_confirmed, confirmed, R.drawable.ic_step_checked))
            } else {
                list.add(Step(R.string.step_confirmed, null, R.drawable.ic_step_unchecked))
            }

            if (delivering != null) {
                list.add(Step(R.string.step_delivering, delivering, R.drawable.ic_step_checked, true))
            } else {
                list.add(Step(R.string.step_delivering, null, R.drawable.ic_step_unchecked))
            }

            if (delivered != null) {
                list.add(Step(R.string.step_delivered, delivered, R.drawable.ic_step_checked))
            } else {
                list.add(Step(R.string.step_delivered, null, R.drawable.ic_step_unchecked))
            }

            return list
        }
}