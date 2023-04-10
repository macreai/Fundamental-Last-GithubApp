package com.macreai.githubapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.macreai.githubapp.R
import com.macreai.githubapp.data.remote.response.ItemsItem
import com.macreai.githubapp.databinding.ItemRowUserBinding
import com.macreai.githubapp.util.MyDiffUtil

class UserAdapter(private var listUser: List<ItemsItem>): RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback{
        fun onItemClicked(data: ItemsItem)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
    class ViewHolder(private val binding: ItemRowUserBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(user: ItemsItem){
            binding.apply {
                Glide.with(itemView)
                    .load(user.avatarUrl)
                    .centerCrop()
                    .into(userPhoto)
                tvName.text = user.login
            }
            val animation = AnimationUtils.loadAnimation(itemView.context, R.anim.anim)
            binding.root.startAnimation(animation)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemRowUserBinding.inflate(LayoutInflater.from(parent.context),parent,false))

    override fun getItemCount() = listUser.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUser[position])

        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listUser[holder.adapterPosition])
        }
    }

    fun setData(newList: List<ItemsItem>){
        val diffUtil = MyDiffUtil(listUser, newList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        listUser = newList
        diffResults.dispatchUpdatesTo(this)
    }

}