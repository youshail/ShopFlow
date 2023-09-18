package com.youshail.ecommerce.app.fragments.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.youshail.ecommerce.app.R
import com.youshail.ecommerce.app.adapters.SpecialProductsAdapter
import com.youshail.ecommerce.app.databinding.FragmentMainCategoryBinding
import com.youshail.ecommerce.app.util.Resource
import com.youshail.ecommerce.app.viewmodels.MainCategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val  TAG = "Main Category Fragment"


@AndroidEntryPoint
class MainCategoryFragment: Fragment(R.layout.fragment_main_category) {
    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductsAdapter: SpecialProductsAdapter
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

    private fun setUpSpecialProductRV() {
        specialProductsAdapter =  SpecialProductsAdapter()
        binding.rvSpecialProducts.apply {
            adapter = specialProductsAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL,false)
        }
    }
}