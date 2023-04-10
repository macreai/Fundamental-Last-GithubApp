package com.macreai.githubapp.ui.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.macreai.githubapp.R
import com.macreai.githubapp.databinding.ActivityDetailBinding
import com.macreai.githubapp.ui.ViewModelFactory
import com.macreai.githubapp.ui.detail.followerfollowing.SectionPagerAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var detailViewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel = obtainViewModel(this)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatar = intent.getStringExtra(EXTRA_AVATAR)

        detailViewModel.setUserDetail(username)
        detailViewModel.detailUser.observe(this, Observer{
                binding.apply {
                    tvName.text = it.login
                    tvUsername.text = it.name
                    followerCount.text = "${it.followers}"
                    followingCount.text = "${it.following}"
                    Glide.with(this@DetailActivity)
                        .load(it.avatarUrl)
                        .centerCrop()
                        .into(userPhoto)
                }
        })

        detailViewModel.isLoading.observe(this, Observer{
            showLoading(it)
        })

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        adapterSectionPager(bundle)

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = detailViewModel.getUserByID(id)
            withContext(Dispatchers.Main){
                if (count != null){
                    if (count > 0){
                        binding.favButton.isChecked = true
                        _isChecked = true
                    } else{
                        binding.favButton.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        binding.favButton.setOnClickListener{
            _isChecked = !_isChecked
            if (_isChecked){
                detailViewModel.insert(id, username, avatar)
                Toast.makeText(this, "User Berhasil Ditambahkan ke Favorite", Toast.LENGTH_SHORT).show()
            } else {
                detailViewModel.delete(id, username, avatar)
                Toast.makeText(this, "User Berhasil Dihapus dari Favorite", Toast.LENGTH_SHORT).show()
            }
            binding.favButton.isChecked = _isChecked
        }

        binding.shareButton.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, "Coba cek user github ini!")
            intent.type = "text/plain"
            startActivity(Intent.createChooser(intent, "Share Ke: "))
        }

    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    private fun adapterSectionPager(bundle: Bundle) {
        val sectionPagerAdapter = SectionPagerAdapter(this, bundle)
        binding.viewPager.adapter = sectionPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        supportActionBar?.elevation = 0f
    }

    private fun showLoading(isLoading: Boolean){
        if (isLoading) binding.githubLoading.visibility = View.VISIBLE
        else binding.githubLoading.visibility = View.GONE
    }

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR = "avatar_extra"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_1,
            R.string.tab_2
        )
    }
}