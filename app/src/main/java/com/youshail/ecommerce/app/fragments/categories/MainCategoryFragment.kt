package com.youshail.ecommerce.app.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.youshail.ecommerce.app.R
import com.youshail.ecommerce.app.adapters.BestDealsAdapter
import com.youshail.ecommerce.app.adapters.BestProductsAdapter
import com.youshail.ecommerce.app.adapters.SpecialProductsAdapter
import com.youshail.ecommerce.app.databinding.FragmentMainCategoryBinding
import com.youshail.ecommerce.app.util.Resource
import com.youshail.ecommerce.app.util.showBottomNavigationView
import com.youshail.ecommerce.app.viewmodels.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

private const val  TAG = "Main Category Fragment"


@AndroidEntryPoint
class MainCategoryFragment: Fragment(R.layout.fragment_main_category) {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: SpecialProductsAdapter
    private lateinit var bestProductsAdapter: BestProductsAdapter
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private val mainCategoryViewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainCategoryBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         setUpSpecialProductRV()
         fetchSpecialProduct()
        setOnClickSpecialProduct()

         setUpBestProductRV()
         fetchBestProducts()
        setOnClickBestProduct()

         setUpBestDealsRV()
         fetchBestDeals()
        setOnClickBestDeals()
    }

    private fun setOnClickBestDeals() {
        bestDealsAdapter.onClick = {
           val b = Bundle().apply { putParcelable("itemProduct",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }
    }

    private fun setOnClickBestProduct() {
        bestProductsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("itemProduct",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }
    }

    private fun setOnClickSpecialProduct() {
        specialProductsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("itemProduct",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }
    }


    private fun setUpBestDealsRV() {
        bestDealsAdapter = BestDealsAdapter()
        binding.rvBestDealsProducts.apply {
            adapter = bestDealsAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun fetchBestDeals() {
        lifecycleScope.launchWhenStarted {
            mainCategoryViewModel.bestDeals.collectLatest {
                when(it){
                    is Resource.Error -> {
                        Log.e(TAG,it.message.toString())
                        hideLoading()
                    }
                    is Resource.Loading -> {
                        showLoading()
                    }
                    is Resource.Success -> {
                        bestDealsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setUpBestProductRV() {
        bestProductsAdapter = BestProductsAdapter()
        binding.rvBestProducts.apply {
            adapter = bestProductsAdapter
            layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL, false)
        }
    }

    private fun fetchBestProducts() {
        lifecycleScope.launchWhenStarted {
            mainCategoryViewModel.bestProduct.collectLatest {
                when(it){
                    is Resource.Error -> {
                        Log.e(TAG,it.message.toString())
                        binding.bestProductsProgressbar.visibility = View.GONE
                    }
                    is Resource.Loading -> {
                        binding.bestProductsProgressbar.visibility = View.VISIBLE
                    }
                    is Resource.Success ->{
                        bestProductsAdapter.differ.submitList(it.data)
                        binding.bestProductsProgressbar.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }

        binding.nestedScrollMainCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{ view,_,scrollX,_,_ ->
            if (view.getChildAt(0).bottom <= view.height + scrollX){
                mainCategoryViewModel.fetchBestProducts()
            }
        })
    }


    private fun setUpSpecialProductRV() {
        specialProductsAdapter =  SpecialProductsAdapter()
        binding.rvSpecialProducts.apply {
            adapter = specialProductsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        }
    }

    private fun fetchSpecialProduct() {
        lifecycleScope.launchWhenStarted {
            mainCategoryViewModel.specialProduct.collectLatest{
                when(it){
                    is Resource.Error -> {
                        Log.e(TAG,it.message.toString())
                        Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                        hideLoading()
                    }
                    is Resource.Success -> {
                        specialProductsAdapter.differ.submitList(it.data)
                        hideLoading()
                    }
                    is Resource.Loading -> {
                        showLoading()
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun showLoading() {
        binding.mainCategoryProgressbar.visibility = View.VISIBLE
    }
    private fun hideLoading(){
        binding.mainCategoryProgressbar.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }


}