package uz.mohirdev.lokmart.presentation.home

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.zhpan.indicator.enums.IndicatorSlideMode
import com.zhpan.indicator.enums.IndicatorStyle
import dagger.hilt.android.AndroidEntryPoint
import uz.mohirdev.lokmart.R
import uz.mohirdev.lokmart.data.api.product.dto.Banner
import uz.mohirdev.lokmart.data.api.product.dto.Category
import uz.mohirdev.lokmart.data.api.product.dto.Product
import uz.mohirdev.lokmart.data.api.product.dto.Section
import uz.mohirdev.lokmart.databinding.FragmentHomeBinding
import uz.mohirdev.lokmart.presentation.home.adapters.BannerAdapter
import uz.mohirdev.lokmart.presentation.home.adapters.HomeCategoryAdapter
import uz.mohirdev.lokmart.presentation.home.adapters.SectionAdapter
import uz.mohirdev.lokmart.util.BaseFragment
import uz.mohirdev.lokmart.util.HorizontalMarginItemDecoration
import uz.mohirdev.lokmart.util.setLightStatusBar
import kotlin.math.abs

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {

    private val viewModel by viewModels<HomeViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToLiveData()
        initUI()
    }

    private fun initUI() = with(binding) {
        setLightStatusBar()
        error.retry.setOnClickListener {
            viewModel.getHome()
        }
        indicator.apply {
            val normalColor = ContextCompat.getColor(requireContext(), R.color.indicator_unchecked)
            val checkedColor = ContextCompat.getColor(requireContext(), R.color.indicator_checked)
            setSliderColor(normalColor, checkedColor)
            setSliderWidth(resources.getDimension(R.dimen.dp_20))
            setSliderHeight(resources.getDimension(R.dimen.dp_4))
            setSlideMode(IndicatorSlideMode.WORM)
            setIndicatorStyle(IndicatorStyle.ROUND_RECT)
            notifyDataChanged()
        }
        banners.offscreenPageLimit = 1

        // Add a PageTransformer that translates the next and previous items horizontally
        // towards the center of the screen, which makes them visible
        val nextItemVisiblePx = resources.getDimension(R.dimen.viewpager_next_item_visible)
        val currentItemHorizontalMarginPx = resources.getDimension(R.dimen.viewpager_current_item_horizontal_margin)
        val pageTranslationX = nextItemVisiblePx + currentItemHorizontalMarginPx
        val pageTransformer = ViewPager2.PageTransformer { page: View, position: Float ->
            page.translationX = -pageTranslationX * position
            // Next line scales the item's height. You can remove it if you don't want this effect
            page.scaleY = 1 - (0.25f * abs(position))
            // If you want a fading effect uncomment the next line:
            // page.alpha = 0.25f + (1 - abs(position))
        }
        banners.setPageTransformer(pageTransformer)

        // The ItemDecoration gives the current (centered) item horizontal margin so that
        // it doesn't occupy the whole screen width. Without it the items overlap
        val itemDecoration = HorizontalMarginItemDecoration(
            requireContext(),
            R.dimen.viewpager_current_item_horizontal_margin
        )
        banners.addItemDecoration(itemDecoration)

        showAll.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.toCategoriesFragment())
        }

        searchContainer.search.setOnFocusChangeListener { _, focused ->
            if (focused.not()) return@setOnFocusChangeListener
            findNavController().navigate(HomeFragmentDirections.toSearchFragment())
        }
    }

    private fun subscribeToLiveData() = with(binding) {
        viewModel.loading.observe(viewLifecycleOwner) {
            loading.root.isVisible = it
        }
        viewModel.error.observe(viewLifecycleOwner) {
            error.root.isVisible = it
        }
        viewModel.home.observe(viewLifecycleOwner) {
            home.isVisible = it != null
            it ?: return@observe

            val name = it.user.firstName ?: it.user.username
            greeting.text = getString(R.string.fragment_home_greeting, name)
            Glide.with(root).load(it.user.avatar).into(avatar)

            banners.adapter = BannerAdapter(it.banners, this@HomeFragment::onBannerClick)
            indicator.setupWithViewPager(banners)
            indicator.apply {
                setPageSize(it.banners.size)
                notifyDataChanged()
            }

            categories.adapter =
                HomeCategoryAdapter(it.categories, this@HomeFragment::onCategoryClick)

            sections.adapter = SectionAdapter(
                it.sections,
                this@HomeFragment::showAll,
                this@HomeFragment::onClickProduct,
                this@HomeFragment::wishlist
            )

            count.text = it.notificationCount.toString()
        }
    }

    private fun onBannerClick(banner: Banner) {

    }

    private fun onCategoryClick(category: Category) {
        findNavController().navigate(HomeFragmentDirections.toProductsFragment(category))
    }

    private fun showAll(section: Section) {

    }

    private fun onClickProduct(product: Product) {
        findNavController().navigate(HomeFragmentDirections.toDetailFragment(product.id))
    }

    private fun wishlist(product: Product) {
        viewModel.toggleWishlist(product)
    }
}