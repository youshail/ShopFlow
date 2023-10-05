package com.youshail.ecommerce.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.youshail.ecommerce.app.data.CartProduct
import com.youshail.ecommerce.app.firebase.FirebaseCommon
import com.youshail.ecommerce.app.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firebaseCommon: FirebaseCommon
): ViewModel(){

    private val _addToCard = MutableStateFlow<Resource<CartProduct>>(Resource.Empty())
    val addToCard = _addToCard.asStateFlow()

    fun addUpdateProductInCart(cartProduct: CartProduct){
        viewModelScope.launch {
            _addToCard.emit(Resource.Loading())
        }

        firestore.collection("user").document(auth.uid!!)
            .collection("cart")
            .whereEqualTo("product.id",cartProduct.product.id)
            .get()
            .addOnSuccessListener { result ->
                result.documents.let { cartProducts ->
                    if (cartProducts.isEmpty()){ // Add new Product
                        addNewProduct(cartProduct)
                    }else{
                        val cProduct = cartProducts.first().toObject(CartProduct::class.java)
                        if (cProduct == cartProduct){ //Increase the quantity
                            val documentId = cartProducts.first().id
                            increaseQuantity(documentId, cartProduct)
                        }else{ // Add new Product
                            addNewProduct(cartProduct)
                        }
                    }
                }

            }.addOnFailureListener {
                viewModelScope.launch {
                    _addToCard.emit(Resource.Error(it.message.toString()))
                }
            }
    }

    private fun increaseQuantity(documentId: String, cartProduct: CartProduct) {
        firebaseCommon.increaseQuantity(documentId) { _,e ->
            viewModelScope.launch {
                if (e==null)
                    _addToCard.emit(Resource.Success(cartProduct))
                else
                    _addToCard.emit(Resource.Error(e.message.toString()))
            }
        }
    }

    private fun addNewProduct(cartProduct: CartProduct) {
        firebaseCommon.addProductToCart(cartProduct){ addedProduct , e ->
            viewModelScope.launch {
                if (e == null)
                    _addToCard.emit(Resource.Success(addedProduct!!))
                else
                    _addToCard.emit(Resource.Error(e.message.toString()))
            }
        }
    }
}