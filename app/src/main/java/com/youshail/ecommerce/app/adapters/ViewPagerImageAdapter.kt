package com.youshail.ecommerce.app.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.youshail.ecommerce.app.databinding.ViewpagerImageItemBinding

class ViewPagerImageAdapter :
    RecyclerView.Adapter<ViewPagerImageAdapter.ViewPagerImageViewHolder>() {


    inner class ViewPagerImageViewHolder(private val binding: ViewpagerImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(imagePath: String) {
            Glide.with(itemView.context)
                .load(imagePath)
                .into(binding.imageProductDetails)
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerImageViewHolder {
        return ViewPagerImageViewHolder(
            ViewpagerImageItemBinding.inflate(
                LayoutInflater.from(parent.context),parent,false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewPagerImageViewHolder, position: Int) {
        val imagePath = differ.currentList[position]
        holder.bind(imagePath)
    }


}