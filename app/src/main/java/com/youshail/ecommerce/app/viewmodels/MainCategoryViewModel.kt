package com.youshail.ecommerce.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.youshail.ecommerce.app.data.Product
import com.youshail.ecommerce.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Empty())

    val specialProduct: StateFlow<Resource<List<Product>>> = _specialProducts

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Empty())
    val bestProduct : StateFlow<Resource<List<Product>>> = _bestProducts

    private val _bestDeals = MutableStateFlow<Resource<List<Product>>>(Resource.Empty())
    val bestDeals = _bestDeals.asStateFlow()

    init {
        fetchSpecialProduct()
        fetchBestProducts()
        fetchBestDeals()
    }
    private fun fetchSpecialProduct() {
        viewModelScope.launch {
            _specialProducts.emit(Resource.Loading())
        }
        firestore.collection("Products")
            .whereEqualTo("category", "Special products")
            .get()
            .addOnSuccessListener { result ->
                val specialProduct = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Success(specialProduct))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _specialProducts.emit(Resource.Error(it.message.toString()))
                }
            }

    }

    private fun fetchBestProducts(){
        viewModelScope.launch {
            _bestProducts.emit(Resource.Loading())
        }

        firestore.collection("Products")
            .get()
            .addOnSuccessListener { result ->
                val bestProduct = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Success(bestProduct))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestProducts.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun fetchBestDeals(){

        viewModelScope.launch {
            _bestDeals.emit(Resource.Loading())
        }

        firestore.collection("Products")
            .whereEqualTo("category","Best deals")
            .get()
            .addOnSuccessListener { result ->
                val bestDeals = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestDeals.emit(Resource.Success(bestDeals))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestDeals.emit(Resource.Error(it.message.toString()))
                }
            }
    }









}