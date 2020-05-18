package com.wattpad.wattpadandroidproj.repository.remote

import com.wattpad.wattpadandroidproj.repository.model.to.StoryList
import com.wattpad.wattpadandroidproj.repository.remote.core.ServerApiManager
import com.wattpad.wattpadandroidproj.repository.remote.impl.IStoryApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit

class StoryApiManager() {
    private var iStoryApi: IStoryApi? = null

    init {
        val retrofit: Retrofit? = ServerApiManager().getRetrofit()
        iStoryApi = retrofit?.create(IStoryApi::class.java)
    }

    fun getAllStories(offset: Int, limit: Int, callback: Callback<StoryList>?) {
        if (callback == null) {
            return
        }
        val call: Call<StoryList>? = iStoryApi?.getAllStories(offset, limit)
        call?.enqueue(callback)
    }
}