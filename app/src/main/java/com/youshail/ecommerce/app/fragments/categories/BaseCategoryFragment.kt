package com.youshail.ecommerce.app.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.youshail.ecommerce.app.R
import com.youshail.ecommerce.app.adapters.BestProductsAdapter
import com.youshail.ecommerce.app.databinding.FragmentBaseCategoryBinding
import com.youshail.ecommerce.app.util.showBottomNavigationView
import com.youshail.ecommerce.app.viewmodels.CategoryViewModel



open class BaseCategoryFragment: Fragment(R.layout.fragment_base_category) {

    private lateinit var binding: FragmentBaseCategoryBinding
    protected val offerAdapter: BestProductsAdapter by lazy { BestProductsAdapter() }
    protected val  bestProductsAdapter: BestProductsAdapter by lazy { BestProductsAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpOfferRv()
        setOnClickOffer()
        setUpBestProductsRv()
        setOnClickBestProducts()


        binding.rvOffer.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(!recyclerView.canScrollVertically(1) && dx != 0){
                    onOfferPagingRequest()
                }
            }
        })

        binding.nestedScrollBaseCategory.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener{ view, _, scrollX, _, _ ->
            if (view.getChildAt(0).bottom <= view.height + scrollX){
                onBestProductsPagingRequest()
            }
        })
    }

    private fun setOnClickOffer() {
        offerAdapter.onClick = {
            val b = Bundle().apply { putParcelable("itemProduct",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }
    }

    private fun setOnClickBestProducts() {
        bestProductsAdapter.onClick = {
            val b = Bundle().apply { putParcelable("itemProduct",it) }
            findNavController().navigate(R.id.action_homeFragment_to_productDetailsFragment,b)
        }
    }
    open fun onOfferPagingRequest() {

    }

    open fun onBestProductsPagingRequest(){

    }


    fun showOfferLoading(){
        binding.offerProductsProgressBar.visibility = View.VISIBLE
    }

    fun hideOfferLoading(){
        binding.offerProductsProgressBar.visibility = View.GONE
    }

    fun showBestProductsLoading(){
        binding.bestProductsProgressBar.visibility = View.VISIBLE
    }

    fun hideBestProductsLoading(){
        binding.bestProductsProgressBar.visibility = View.GONE
    }

    private fun setUpBestProductsRv() {
        binding.rvBestProducts.apply {
            adapter = bestProductsAdapter
            layoutManager = GridLayoutManager(requireContext(),2,GridLayoutManager.VERTICAL,false)
        }
    }

    private fun setUpOfferRv() {
        binding.rvOffer.apply {
            adapter = offerAdapter
            layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }


}