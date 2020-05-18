package com.wattpad.wattpadandroidproj.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wattpad.wattpadandroidproj.repository.model.bl.StoryBL
import com.wattpad.wattpadandroidproj.repository.model.to.StoryList
import com.wattpad.wattpadandroidproj.repository.model.to.StoryTO
import com.wattpad.wattpadandroidproj.repository.remote.StoryApiManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryViewModel(application: Application) : AndroidViewModel(application) {
    private val storyBL: StoryBL by lazy { StoryBL(application) }
    private val storyApiManager: StoryApiManager by lazy { StoryApiManager() }
    private var originalStories: LiveData<List<StoryTO>>? = null
    private var filterStories: MutableLiveData<ArrayList<StoryTO>>? =
        MutableLiveData<ArrayList<StoryTO>>()

    private var offset: Int = 0
    private var limit: Int = 10


    fun filterStories(text: String) {
        val tempFilterArrayList: ArrayList<StoryTO> = ArrayList<StoryTO>()
        for (story in originalStories?.value!!) {
            if (story.title.contains(text)) {
                tempFilterArrayList.add(story)
            }
        }
        filterStories?.value = tempFilterArrayList
    }

    fun getStories(): LiveData<List<StoryTO>>? {
        originalStories = storyBL.getStories()
        return originalStories
    }

    fun getFilteredStories(): MutableLiveData<ArrayList<StoryTO>>? {
        return filterStories
    }

    private fun registerStoriesInDB(stories: List<StoryTO>?) {
        storyBL.removeAll()
        stories?.let {
            for (storyTO in it) {
                storyBL.register(storyTO)
            }
        }
    }

    fun requestAllRemoteStories() {
        storyApiManager.getAllStories(offset, limit, object : Callback<StoryList> {
            override fun onResponse(call: Call<StoryList>?, response: Response<StoryList>?) {
                if (response?.code() == 200) {
                    registerStoriesInDB(response.body().stories)
                }
            }

            override fun onFailure(call: Call<StoryList>?, t: Throwable?) {
            }
        })
    }
}