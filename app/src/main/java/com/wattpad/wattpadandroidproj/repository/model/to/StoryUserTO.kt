package com.wattpad.wattpadandroidproj.repository.model.to

import androidx.room.ColumnInfo

data class StoryUserTO(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "fullname") var fullname: String,
    @ColumnInfo(name = "avatar") var avatar: String
) {

}