package com.youshail.ecommerce.app.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.youshail.ecommerce.app.R
import com.youshail.ecommerce.app.adapters.ColorsAdapter
import com.youshail.ecommerce.app.adapters.SizesAdapter
import com.youshail.ecommerce.app.adapters.ViewPagerImageAdapter
import com.youshail.ecommerce.app.data.CartProduct
import com.youshail.ecommerce.app.data.Product
import com.youshail.ecommerce.app.databinding.FragmentProductDetailsBinding
import com.youshail.ecommerce.app.util.Resource
import com.youshail.ecommerce.app.util.hideBottomNavigationView
import com.youshail.ecommerce.app.viewmodels.DetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class ProductDetailsFragment : Fragment(R.layout.fragment_product_details) {

    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding: FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy { ViewPagerImageAdapter() }
    private val sizesAdapter by lazy { SizesAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }
    private var selectedColor: Int? = null
    private var selectedSize: String? = null
    private val detailsViewModel by viewModels<DetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigationView()
        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = args.itemProduct
        setUpSizesRV()
        setUpColorsRV()
        setUpPagerImageRV()

        setNavigateUp()

        setDetailProduct(product)

        sizesAdapter.onItemClick={
            selectedSize = it
        }
        colorsAdapter.onItemClick = {
            selectedColor = it
        }
        val cartProduct = CartProduct(product,1,selectedColor,selectedSize)
        addUpdateProductInCart(cartProduct)


    }

    private fun addUpdateProductInCart(cartProduct: CartProduct) {
        binding.buttonAddToCart.setOnClickListener {
            detailsViewModel.addUpdateProductInCart(cartProduct)
        }

        lifecycleScope.launchWhenStarted {
            detailsViewModel.addToCard.collectLatest {
                when(it){
                    is Resource.Error -> {
                        binding.buttonAddToCart.stopAnimation()
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        binding.buttonAddToCart.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.buttonAddToCart.revertAnimation()
                        binding.buttonAddToCart.setBackgroundColor(resources.getColor(R.color.black))
                    }
                    else -> Unit
                }
            }
        }

    }

    private fun setDetailProduct(product: Product) {

        binding.apply {
            productPrice.text = "${product.price.toString()} DH"
            tvProductsName.text = product.name
            tvProductDescription.text = product.description

            viewPagerAdapter.differ.submitList(product.images)
            if (product.colors.isNullOrEmpty())
                tvProductColors.visibility = View.GONE

            if (product.sizes.isNullOrEmpty())
                tvProductSize.visibility = View.GONE

            product.colors?.let {
                colorsAdapter.differ.submitList(it)
            }
            product.sizes?.let {
                sizesAdapter.differ.submitList(it)
            }

        }
    }

    private fun setNavigateUp() {
        binding.imageClose.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun setUpPagerImageRV() {
        binding.apply {
            viewPagerProductImages.adapter= viewPagerAdapter
        }
    }

    private fun setUpColorsRV() {
        binding.rvColors.apply {
            adapter = colorsAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun setUpSizesRV() {
        binding.rvSizes.apply {
            adapter = sizesAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

}













