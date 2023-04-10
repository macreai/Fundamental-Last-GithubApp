package com.macreai.githubapp.ui.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.macreai.githubapp.R
import com.macreai.githubapp.data.remote.response.ItemsItem
import com.macreai.githubapp.data.repo.SettingPreferences
import com.macreai.githubapp.databinding.ActivityMainBinding
import com.macreai.githubapp.ui.SettingModeFactory
import com.macreai.githubapp.ui.UserAdapter
import com.macreai.githubapp.ui.detail.DetailActivity
import com.macreai.githubapp.ui.favorite.FavoriteActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.listUser.observe(this, Observer{
            showRecyclerView(it)
        })

        mainViewModel.isLoading.observe(this, Observer{
            showLoading(it)
        })

        mainViewModel.isNotFound.observe(this, Observer {
            showNotFound(it)
        })

        setThemeMode()
    }


    private fun setThemeMode() {

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, SettingModeFactory(pref)).get(
            SettingViewModel::class.java
        )

        settingViewModel.getThemeSettings().observe(this, Observer {
            if (it){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.mode.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.mode.isChecked = false
            }
        })

        binding.mode.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val mainViewModel by viewModels<MainViewModel>()

        menuInflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView

        searchView.clearFocus()
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_user)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.findUsers(query)
                Toast.makeText(
                    this@MainActivity,
                    "Pencarian untuk $query",
                    Toast.LENGTH_SHORT)
                    .show()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isEmpty()) mainViewModel.findUsers("arda")
                return true
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.bookmark -> {
                val intentToFav = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intentToFav)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showRecyclerView(itemsItems: List<ItemsItem>) {

        adapter = UserAdapter(itemsItems)
        adapter.setData(itemsItems)
        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter

        }

        //item click
        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ItemsItem) {
                val intentToDetail = Intent(this@MainActivity, DetailActivity::class.java)
                intentToDetail.putExtra(DetailActivity.EXTRA_USERNAME, data.login)
                intentToDetail.putExtra(DetailActivity.EXTRA_ID, data.id)
                intentToDetail.putExtra(DetailActivity.EXTRA_AVATAR, data.avatarUrl)
                startActivity(intentToDetail)
            }

        })
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) binding.githubLoading.visibility = View.VISIBLE
        else binding.githubLoading.visibility = View.GONE
    }

    private fun showNotFound(isNotFound: Boolean){
        if (isNotFound) binding.notFound.visibility = View.VISIBLE
        else binding.notFound.visibility = View.GONE
    }


}