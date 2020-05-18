package com.wattpad.wattpadandroidproj.repository.model.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.wattpad.wattpadandroidproj.repository.model.to.StoryTO

@Dao
interface IStoryDAO {
    @Insert
    fun insert(storyTO: StoryTO)

    @Query("DELETE FROM TBL_STORIES")
    fun deleteAll()

    @Query("SELECT * FROM TBL_STORIES AS STORY WHERE STORY.id=:id")
    fun select(id: Int): StoryTO

    @Query("SELECT * FROM TBL_STORIES")
    fun selectAll(): LiveData<List<StoryTO>>
}