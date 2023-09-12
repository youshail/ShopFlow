package com.youshail.ecommerce.app.fragments.lognRegister

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.youshail.ecommerce.app.R
import com.youshail.ecommerce.app.databinding.FragmentAcountOptionsBinding

class AccountOptionsFragment: Fragment(R.layout.fragment_acount_options) {

    private lateinit var binding: FragmentAcountOptionsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAcountOptionsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonRegisterOption.setOnClickListener {
            findNavController().navigate(R.id.action_accountOptionsFragment_to_registerFragment2)
        }

        binding.buttonLoginAccountOption.setOnClickListener {
            findNavController().navigate(R.id.action_accountOptionsFragment_to_loginFragment2)
        }
    }
}