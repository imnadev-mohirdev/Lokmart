package uz.mohirdev.lokmart.presentation.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import uz.mohirdev.lokmart.databinding.ItemStepBinding
import uz.mohirdev.lokmart.domain.model.Order
import uz.mohirdev.lokmart.domain.model.Step

class StepAdapter(
    private val steps: List<Step>, private val track: () -> Unit
) : RecyclerView.Adapter<StepAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemStepBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(step: Step, dividerVisible: Boolean) = with(binding) {
            icon.setImageResource(step.icon)
            title.text = root.context.getString(step.title)
            date.text = step.date
            divider.isVisible = dividerVisible
            root.setOnClickListener {
                if (step.trackable) {
                    track()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemStepBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(steps[position], dividerVisible = position < steps.size - 1)
    }

    override fun getItemCount() = steps.size
}