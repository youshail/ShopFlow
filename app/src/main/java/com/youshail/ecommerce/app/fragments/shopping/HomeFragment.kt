package com.youshail.ecommerce.app.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.youshail.ecommerce.app.R
import com.youshail.ecommerce.app.adapters.HomeViewPagerAdapter
import com.youshail.ecommerce.app.databinding.FragmentHomeBinding
import com.youshail.ecommerce.app.fragments.categories.AccessoryFragment
import com.youshail.ecommerce.app.fragments.categories.ChairFragment
import com.youshail.ecommerce.app.fragments.categories.CupboardFragment
import com.youshail.ecommerce.app.fragments.categories.FurnitureFragment
import com.youshail.ecommerce.app.fragments.categories.MainCategoryFragment
import com.youshail.ecommerce.app.fragments.categories.TableFragment

class HomeFragment: Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragments = arrayListOf(
            MainCategoryFragment(),
            ChairFragment(),
            CupboardFragment(),
            TableFragment(),
            AccessoryFragment(),
            FurnitureFragment()
        )

        val viewPagerToAdapter = HomeViewPagerAdapter(categoriesFragments,childFragmentManager,lifecycle)

        binding.viewpagerHome.adapter = viewPagerToAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewpagerHome){ tab, position ->
            when(position){
                0 -> tab.text = "Main"
                1 -> tab.text = "Chair"
                2 -> tab.text = "Cupboard"
                3 -> tab.text = "Table"
                4 -> tab.text = "Accessory"
                5 -> tab.text = "Furniture"
            }
        }.attach()

    }
}