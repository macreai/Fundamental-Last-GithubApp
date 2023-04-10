package com.macreai.githubapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.macreai.githubapp.data.local.entity.FavEntity

@Dao
interface FavDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(user: FavEntity)

    @Delete
    fun delete(user: FavEntity)

    @Query("SELECT * FROM user")
    fun getAllFav(): LiveData<List<FavEntity>>

    @Query("SELECT count(*) FROM user WHERE user.id = :id")
    fun getUserById(id: Int): Int
}