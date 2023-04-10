package com.macreai.githubapp.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.macreai.githubapp.R
import com.macreai.githubapp.data.local.entity.FavEntity
import com.macreai.githubapp.data.remote.response.ItemsItem
import com.macreai.githubapp.databinding.ActivityFavoriteBinding
import com.macreai.githubapp.ui.UserAdapter
import com.macreai.githubapp.ui.ViewModelFactory
import com.macreai.githubapp.ui.detail.DetailActivity

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: UserAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        favoriteViewModel = obtainViewModel(this)

        favoriteViewModel.getAllFavUsers().observe(this, Observer {
            Log.d("TAG", "onCreate: ")
            if (it != null){
                val list = mapList(it)
                showRecyclerView(list)
            }
        })
    }

    private fun mapList(users: List<FavEntity>): ArrayList<ItemsItem> {
        val listUser = ArrayList<ItemsItem>()
        for (user in users){
           val userMapped = ItemsItem(
               user.id,
               user.login,
               user.avatarUrl
           )
            listUser.add(userMapped)
        }
        return listUser
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(FavoriteViewModel::class.java)
    }

    private fun showRecyclerView(itemsItems: List<ItemsItem>) {

        adapter = UserAdapter(itemsItems)
        adapter.setData(itemsItems)
        binding.apply {
            rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFavorite.setHasFixedSize(true)
            rvFavorite.adapter = adapter

        }

        //item click
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ItemsItem) {
                val intentToDetail = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intentToDetail.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
                intentToDetail.putExtra(DetailActivity.EXTRA_ID, data.id)
                intentToDetail.putExtra(DetailActivity.EXTRA_AVATAR, data.avatarUrl)
                startActivity(intentToDetail)
                finish()
            }

        })
    }
}