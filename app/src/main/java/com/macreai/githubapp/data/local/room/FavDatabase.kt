package com.macreai.githubapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.macreai.githubapp.data.local.entity.FavEntity

@Database(entities = [FavEntity::class], version = 1, exportSchema = false)
abstract class FavDatabase: RoomDatabase() {
    abstract fun favDao(): FavDao

    companion object{
        @Volatile
        private var instance: FavDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): FavDatabase =
            instance ?: synchronized(this){
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    FavDatabase::class.java, "Favorite.db"
                ).build()
            }
    }
}