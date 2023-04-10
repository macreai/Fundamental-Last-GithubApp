package com.macreai.githubapp.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.macreai.githubapp.data.repo.FavRepository

class FavoriteViewModel(application: Application): AndroidViewModel(application) {
    private val favRepository: FavRepository = FavRepository(application)

    fun getAllFavUsers() = favRepository.getAllFavUser()
}