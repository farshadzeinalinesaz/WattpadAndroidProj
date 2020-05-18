package com.wattpad.wattpadandroidproj.repository.model.bl

import android.content.Context
import androidx.lifecycle.LiveData
import com.wattpad.wattpadandroidproj.repository.model.dao.IStoryDAO
import com.wattpad.wattpadandroidproj.repository.model.db_manager.AppDataBaseManager
import com.wattpad.wattpadandroidproj.repository.model.to.StoryTO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StoryBL(context: Context) {
    private var iStoryDAO: IStoryDAO? = null
    private var stories: LiveData<List<StoryTO>>? = null

    init {
        iStoryDAO = AppDataBaseManager.getDataBase(context)?.getStoryDAO()
        stories = iStoryDAO?.selectAll()
    }

    fun register(storyTO: StoryTO) {
        GlobalScope.launch {
            iStoryDAO?.insert(storyTO)
        }
    }

    fun removeAll() {
        GlobalScope.launch {
            iStoryDAO?.deleteAll()
        }
    }

    fun getStories(): LiveData<List<StoryTO>>? {
        return stories
    }

}