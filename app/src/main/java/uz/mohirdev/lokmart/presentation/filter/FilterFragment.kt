package uz.mohirdev.lokmart.presentation.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import uz.mohirdev.lokmart.R
import uz.mohirdev.lokmart.data.api.product.dto.Category
import uz.mohirdev.lokmart.databinding.FragmentFilterBinding
import uz.mohirdev.lokmart.databinding.ItemRadioGroupBinding
import uz.mohirdev.lokmart.domain.model.ProductQuery
import uz.mohirdev.lokmart.domain.model.Sort
import uz.mohirdev.lokmart.util.BaseFragment

@AndroidEntryPoint
class FilterFragment : BaseFragment<FragmentFilterBinding>(FragmentFilterBinding::inflate) {

    private val viewModel by viewModels<FilterViewModel>()
    private val args by navArgs<FilterFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        subscribeToLiveData()
    }

    private fun subscribeToLiveData() = with(binding) {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            listOf(categoriesTitle, categoriesGroup, othersDivider).forEach { it.isVisible = true }
            categories.forEach {
                val radioBinding = ItemRadioGroupBinding.inflate(layoutInflater)
                radioBinding.root.text = it.title
                radioBinding.root.tag = it
                radioBinding.root.isChecked = args.filter.category?.id == it.id
                categoriesGroup.addView(radioBinding.root)
            }
        }
        viewModel.events.observe(viewLifecycleOwner) {
            when (it) {
                FilterViewModel.Event.CategoriesError -> {
                    val snackBar = Snackbar.make(
                        binding.root,
                        R.string.fragment_filter_categories_error,
                        Snackbar.LENGTH_INDEFINITE
                    )
                    snackBar.setAction(R.string.retry) {
                        snackBar.dismiss()
                        viewModel.getCategories()
                    }
                    snackBar.show()
                }
            }
        }
    }

    private fun initUI() = with(binding) {
        close.setOnClickListener {
            findNavController().popBackStack()
        }
        val filter = args.filter
        priceSlider.values = filter.range.toList()
        filter.rating?.let {
            (ratingGroup.getChildAt(it) as RadioButton).isChecked = true
        }
        filter.discount?.let {
            (discountGroup.getChildAt(it) as RadioButton).isChecked = true
        }
        discountSort.isChecked = filter.sort.contains(Sort.discount)
        voucherSort.isChecked = filter.sort.contains(Sort.voucher)
        deliverySort.isChecked = filter.sort.contains(Sort.delivery)
        shippingSort.isChecked = filter.sort.contains(Sort.shipping)

        apply.setOnClickListener {
            val sort = mutableListOf<Sort>()
            if(discountSort.isChecked) sort.add(Sort.discount)
            if(shippingSort.isChecked) sort.add(Sort.shipping)
            if(voucherSort.isChecked) sort.add(Sort.voucher)
            if(deliverySort.isChecked) sort.add(Sort.delivery)
            val query = ProductQuery(
                category = categoriesGroup.children.map { it as RadioButton }
                    .firstOrNull { it.isChecked }?.tag as? Category,
                search = args.filter.search,
                range = priceSlider.values[0] to priceSlider.values[1],
                rating = ratingGroup.children
                    .mapIndexed { index, view -> index to (view as RadioButton).isChecked }
                    .firstOrNull { it.second }?.first,
                discount = discountGroup.children
                    .mapIndexed { index, view -> index to (view as RadioButton).isChecked }
                    .firstOrNull { it.second }?.first,
                sort = sort
            )

            val result = bundleOf(RESULT_KEY to query)
            setFragmentResult(REQUEST_KEY, result)
            findNavController().popBackStack()
        }
    }

    companion object {
        const val REQUEST_KEY = "filter_request_key"
        const val RESULT_KEY = "filter_result_key"
    }
}