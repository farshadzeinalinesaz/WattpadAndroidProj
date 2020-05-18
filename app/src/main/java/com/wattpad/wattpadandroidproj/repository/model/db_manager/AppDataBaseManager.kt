package com.wattpad.wattpadandroidproj.repository.model.db_manager

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wattpad.wattpadandroidproj.repository.model.dao.IStoryDAO
import com.wattpad.wattpadandroidproj.repository.model.to.StoryTO

@Database(entities = [StoryTO::class], version = 1, exportSchema = false)
abstract class AppDataBaseManager : RoomDatabase() {
    abstract fun getStoryDAO(): IStoryDAO

    companion object {

        @Volatile
        private var INSTANCE: AppDataBaseManager? = null

        fun getDataBase(context: Context): AppDataBaseManager? {
            if (INSTANCE != null) {
                return INSTANCE
            }
            synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDataBaseManager::class.java,
                    "wattpad_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
            }
            return INSTANCE
        }
    }
}