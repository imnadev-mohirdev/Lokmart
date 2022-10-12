package uz.mohirdev.lokmart.presentation.orders

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.PagingData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.mohirdev.lokmart.databinding.FragmentOrdersBinding
import uz.mohirdev.lokmart.domain.model.Order
import uz.mohirdev.lokmart.util.BaseFragment

@AndroidEntryPoint
class OrdersFragment : BaseFragment<FragmentOrdersBinding>(FragmentOrdersBinding::inflate) {

    private val viewModel by viewModels<OrdersViewModel>()
    private val adapter by lazy { OrderAdapter(this::track) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter.addLoadStateListener {
            viewModel.setLoadStates(it, adapter.itemCount)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() = with(binding) {
        viewModel.loading.observe(viewLifecycleOwner) {
            progress.root.isVisible = it
        }
        viewModel.error.observe(viewLifecycleOwner) {
            error.root.isVisible = it
        }
        viewModel.empty.observe(viewLifecycleOwner) {
            empty.isVisible = it
        }
        viewModel.orders.observe(viewLifecycleOwner) {
            viewLifecycleOwner.lifecycleScope.launch {
                if (it == null) {
                    adapter.submitData(PagingData.empty())
                    return@launch
                }
                adapter.submitData(it)
            }
        }
    }

    private fun initUI() = with(binding) {
        statuses.adapter = StatusAdapter(viewModel.mStatus, viewModel::setStatus)
        error.retry.setOnClickListener {
            viewModel.getOrders()
        }
        orders.adapter = adapter
    }

    private fun track(order: Order) {
        findNavController().navigate(OrdersFragmentDirections.toMapFragment(order.id))
    }
}