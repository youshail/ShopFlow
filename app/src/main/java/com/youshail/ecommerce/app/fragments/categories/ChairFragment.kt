package com.youshail.ecommerce.app.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.FirebaseFirestore
import com.youshail.ecommerce.app.data.Category
import com.youshail.ecommerce.app.util.Resource
import com.youshail.ecommerce.app.viewmodels.CategoryViewModel
import com.youshail.ecommerce.app.viewmodels.factory.BaseCategoryViewModelFactoryFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

private const val TAG="Chair Fragment"

@AndroidEntryPoint
class ChairFragment: BaseCategoryFragment() {

    @Inject
    lateinit var firestore: FirebaseFirestore

    private val categoryViewModel by viewModels<CategoryViewModel> {
        BaseCategoryViewModelFactoryFactory(firestore,Category.Chair)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fetchOfferProducts()
        fetchBestProduct()
    }

    private fun fetchBestProduct() {
        lifecycleScope.launchWhenStarted {
            categoryViewModel.bestProducts.collectLatest { results ->
                when(results){
                    is Resource.Error -> {
                        Log.e(TAG,results.message.toString())
                        Toast.makeText(requireContext(),results.message.toString(),Toast.LENGTH_SHORT).show()
                        hideBestProductsLoading()
                    }
                    is Resource.Loading -> {
                        showBestProductsLoading()
                    }
                    is Resource.Success -> {
                        bestProductsAdapter.differ.submitList(results.data)
                        hideBestProductsLoading()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun fetchOfferProducts() {
        lifecycleScope.launchWhenStarted {
            categoryViewModel.offerProducts.collectLatest { results ->
                when(results){
                    is Resource.Error -> {
                        Log.e(TAG,results.message.toString())
                        Toast.makeText(requireContext(),results.message.toString(),Toast.LENGTH_SHORT).show()
                        hideOfferLoading()
                    }
                    is Resource.Loading -> {
                        showOfferLoading()
                    }
                    is Resource.Success -> {
                        offerAdapter.differ.submitList(results.data)
                        hideOfferLoading()
                    }
                    else -> Unit
                }
            }
        }
    }

    override fun onBestProductsPagingRequest() {
        super.onBestProductsPagingRequest()
    }

    override fun onOfferPagingRequest() {
        super.onOfferPagingRequest()
    }
}