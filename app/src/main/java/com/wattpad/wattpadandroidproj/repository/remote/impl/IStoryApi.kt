package com.wattpad.wattpadandroidproj.repository.remote.impl

import com.wattpad.wattpadandroidproj.repository.model.to.StoryList
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface IStoryApi {
    @GET(value = "url?fields=some_params(param1,param2,param3,param3)&filter=flag")
    fun getAllStories(@Query(value = "offset") offset: Int, @Query(value = "limit") limit: Int): Call<StoryList>?
}
