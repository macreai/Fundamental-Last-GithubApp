package com.macreai.githubapp.util

import androidx.recyclerview.widget.DiffUtil
import com.macreai.githubapp.data.remote.response.ItemsItem

class MyDiffUtil(
    private val oldList: List<ItemsItem>,
    private val newList: List<ItemsItem>
): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size

    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when{
            oldList[oldItemPosition].id != newList[newItemPosition].id -> false
            else -> true
        }
    }
}