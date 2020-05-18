package com.wattpad.wattpadandroidproj.repository.model.to

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "tbl_stories")
data class StoryTO(
    @ColumnInfo(name = "id") var id: String,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "cover") var cover: String,
    @Embedded @SerializedName(value = "user") var storyUserTO: StoryUserTO
) {
    @PrimaryKey(autoGenerate = true)
    var dbId: Int = -1
}