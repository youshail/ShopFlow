package com.youshail.ecommerce.app.fragments.lognRegister

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.youshail.ecommerce.app.R
import com.youshail.ecommerce.app.activities.ShoppingActivity
import com.youshail.ecommerce.app.databinding.FragmentLoginBinding
import com.youshail.ecommerce.app.databinding.ResetPasswordDialogBinding
import com.youshail.ecommerce.app.dialog.setupBottomSheetDialog
import com.youshail.ecommerce.app.util.Resource
import com.youshail.ecommerce.app.viewmodels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

private const val TAG = "LoginFragment"
@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            buttonLoginAccount.setOnClickListener {
                val email = edEmail.text.toString().trim()
                val password = edPassword.text.toString().trim()
                loginViewModel.login(
                    email,
                    password
                )
            }

            tvDonTHaveAccount.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            tvForgetPassword.setOnClickListener {
                setupBottomSheetDialog { email ->
                    loginViewModel.resetPassword(email)
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            loginViewModel.resetPassword.collect{
                when(it){
                    is Resource.Error -> {
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Success -> {
                        Snackbar.make(requireView(),"Reset link was sent to your email",Snackbar.LENGTH_LONG).show()
                    }

                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            loginViewModel.login.collect{
                when(it){
                    is Resource.Empty -> Unit
                    is Resource.Error -> {
                        binding.buttonLoginAccount.revertAnimation()
                        Toast.makeText(requireContext(),it.message, Toast.LENGTH_LONG).show()
                    }
                    is Resource.Loading -> {
                        binding.buttonLoginAccount.startAnimation()
                    }
                    is Resource.Success -> {
                        binding.buttonLoginAccount.revertAnimation( )
                        Intent(requireActivity(),ShoppingActivity::class.java).also { intent ->
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }

                    }
                }
            }
        }
    }
}