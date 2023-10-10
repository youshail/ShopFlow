package com.youshail.ecommerce.app.fragments.shopping

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.youshail.ecommerce.app.R
import com.youshail.ecommerce.app.adapters.CartProductAdapter
import com.youshail.ecommerce.app.databinding.FragmentCartBinding
import com.youshail.ecommerce.app.firebase.FirebaseCommon
import com.youshail.ecommerce.app.util.Resource
import com.youshail.ecommerce.app.util.VerticalItemDecoration
import com.youshail.ecommerce.app.util.showBottomNavigationView
import com.youshail.ecommerce.app.viewmodels.CartViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class CartFragment : Fragment(R.layout.fragment_cart) {

    private lateinit var binding: FragmentCartBinding
    private val cartAdapter by lazy { CartProductAdapter() }
    private val cartViewModel by viewModels<CartViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpCartRv()



        lifecycleScope.launchWhenStarted {
            cartViewModel.productPrice.collectLatest { price ->
                price.let {
                    val totalPrice = it
                    delay(500)
                    binding.tvTotalPrice.text = "$totalPrice DH"
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            cartViewModel.deleteDialog.collectLatest {
                val alertDialog = AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete item from cart")
                    setMessage("Do you want to delete this item from your cart?")
                    setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    setPositiveButton("Yes"){ dialog,_ ->
                        cartViewModel.deleteCartProduct(it)
                        dialog.dismiss()
                    }
                }
                alertDialog.create()
                alertDialog.show()
            }
        }

        lifecycleScope.launchWhenStarted {
            cartViewModel.cartProducts.collectLatest {
                when (it) {
                    is Resource.Empty -> Unit
                    is Resource.Error -> {
                        binding.progressbarCart.visibility = View.INVISIBLE
                        Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    }

                    is Resource.Loading -> {
                        binding.progressbarCart.visibility = View.VISIBLE
                    }

                    is Resource.Success -> {
                        binding.progressbarCart.visibility = View.INVISIBLE
                        if (it.data!!.isEmpty()) {
                            showEmptyCart()
                            hideOtherViews()
                        } else {
                            hideEmptyCart()
                            showOtherViews()
                            cartAdapter.differ.submitList(it.data)

                        }
                    }
                }
            }
        }

        cartAdapter.onProductClick = {
            var b = Bundle().apply {
                putParcelable("itemProduct",it.product)
            }
            findNavController().navigate(R.id.action_cartFragment_to_productDetailsFragment,b)
        }

        cartAdapter.onPlusClick = {
            cartViewModel.changeQuantity(it,FirebaseCommon.QuantityChanging.INCREASE)
        }

        cartAdapter.onMinusClick = {
            cartViewModel.changeQuantity(it,FirebaseCommon.QuantityChanging.DECREASE)
        }


        binding.imageCloseCart.setOnClickListener {
            findNavController().navigateUp()
        }

    }

    private fun hideOtherViews() {
        binding.apply {
            rvCart.visibility = View.GONE
            totalBoxContainer.visibility = View.GONE
            buttonCheckout.visibility = View.GONE
        }
    }

    private fun showOtherViews() {
        binding.apply {
            rvCart.visibility = View.VISIBLE
            totalBoxContainer.visibility = View.VISIBLE
            buttonCheckout.visibility = View.VISIBLE
        }
    }

    private fun hideEmptyCart() {
        binding.apply {
            layoutCartEmpty.visibility = View.GONE
        }
    }

    private fun showEmptyCart() {
        binding.apply {
            layoutCartEmpty.visibility = View.VISIBLE
        }
    }

    private fun setUpCartRv() {
        binding.rvCart.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = cartAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigationView()
    }
}