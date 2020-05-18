package com.wattpad.wattpadandroidproj.repository.model.to

import com.google.gson.annotations.SerializedName

class StoryList {
    @SerializedName(value = "stories")
    var stories: ArrayList<StoryTO>? = null
}