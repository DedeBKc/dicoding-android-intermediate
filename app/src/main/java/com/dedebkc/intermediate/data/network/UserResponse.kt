package com.dedebkc.intermediate.data.network

import com.google.gson.annotations.SerializedName
import com.dedebkc.intermediate.model.StoryModel
import com.dedebkc.intermediate.model.UserModel

data class UserResponse(

    @field:SerializedName("message")
	val message: String? = null,

    @field:SerializedName("loginResult")
	val loginResult: UserModel? = null,

    @field:SerializedName("listStory")
	val listStory: ArrayList<StoryModel>? = null
)
