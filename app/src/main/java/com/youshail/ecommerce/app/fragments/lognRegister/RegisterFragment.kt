package com.youshail.ecommerce.app.fragments.lognRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.youshail.ecommerce.app.R
import com.youshail.ecommerce.app.data.User
import com.youshail.ecommerce.app.databinding.FragmentRegisterBinding
import com.youshail.ecommerce.app.util.RegisterValidation
import com.youshail.ecommerce.app.util.Resource
import com.youshail.ecommerce.app.viewmodels.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

private const val TAG = "RegisterFragment"
@AndroidEntryPoint
class RegisterFragment: Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private val registerViewModel by viewModels<RegisterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            buttonRegisterAccount.setOnClickListener {
                val user = User(
                    edFirstName.text.toString().trim(),
                    edLastName.text.toString().trim(),
                    edEmail.text.toString().trim(),
                    )
                val password = edPassword.text.toString().trim()

                registerViewModel.createAccountWithEmailAndPassword(user,password)
            }
        }

        lifecycleScope.launchWhenStarted {
            registerViewModel.register.collect {
                when(it){
                    is Resource.Error -> {
                        Log.d(TAG, it.data.toString())
                        binding.buttonRegisterAccount.revertAnimation( )
                    }
                    is Resource.Loading -> {
                        binding.buttonRegisterAccount.startAnimation()
                    }
                    is Resource.Success -> {
                        Log.d("User ->", it.data.toString())
                        binding.buttonRegisterAccount.revertAnimation()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            registerViewModel.validation.collect { validation ->
                if(validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.edEmail.apply {
                            requestFocus()
                            error = validation.email.message
                        }
                    }
                }
                if(validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.edPassword.apply {
                            requestFocus()
                            error = validation.password.message
                        }
                    }
                }

            }
        }
    }

}