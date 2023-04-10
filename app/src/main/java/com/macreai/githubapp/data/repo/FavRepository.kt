package com.macreai.githubapp.data.repo

import android.app.Application
import androidx.lifecycle.LiveData
import com.macreai.githubapp.data.local.entity.FavEntity
import com.macreai.githubapp.data.local.room.FavDao
import com.macreai.githubapp.data.local.room.FavDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavRepository(application: Application) {

    private val favDao: FavDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavDatabase.getInstance(application)
        favDao = db.favDao()
    }

    fun getAllFavUser(): LiveData<List<FavEntity>> = favDao.getAllFav()

    fun insert(id: Int, login: String?, avatarUrl: String?){
        executorService.execute{
            var user = FavEntity(
                id,
                login,
                avatarUrl
            )
            favDao.insert(user)
        }
    }

    fun getUserById(id: Int): Int = favDao.getUserById(id)

    fun delete(id: Int, login: String?, avatarUrl: String?){
        executorService.execute{
            var user = FavEntity(
                id,
                login,
                avatarUrl
            )
            favDao.delete(user)
        }
    }
}