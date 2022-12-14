package com.dedebkc.intermediate.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.*
import com.dedebkc.intermediate.data.database.UserStoryDatabase
import com.dedebkc.intermediate.data.datastore.SettingPreference
import com.dedebkc.intermediate.data.network.ApiService
import com.dedebkc.intermediate.data.network.UserResponse
import com.dedebkc.intermediate.data.paging.StoryRemoteMediator
import com.dedebkc.intermediate.model.StoryModel
import com.dedebkc.intermediate.utils.ApiInterceptor
import com.dedebkc.intermediate.utils.AppExecutors
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserRepository(
    private val pref: SettingPreference,
    private val apiService: ApiService,
    private val userStoryDatabase: UserStoryDatabase,
    val appExecutors: AppExecutors
) {
    /**
     * Access data from DataStore (SettingPreference)
     */

    fun getUserToken() : LiveData<String> = pref.getUserToken().asLiveData()
    suspend fun saveUserToken(value: String) = pref.saveUserToken(value)

    fun getUserName() : LiveData<String> = pref.getUserName().asLiveData()
    suspend fun saveUserName(value: String) = pref.saveUserName(value)

    fun getUserEmail() : LiveData<String> = pref.getUserEmail().asLiveData()
    suspend fun saveUserEmail(value: String) = pref.saveUserEmail(value)

    fun getIsFirstTime() : LiveData<Boolean> = pref.isFirstTime().asLiveData()
    suspend fun saveIsFirstTime(value: Boolean) = pref.saveIsFirstTime(value)

    suspend fun clearCache() = pref.clearCache()

    /**
     * Access data from API (Retrofit)
     */

    fun userLogin(email: String, password: String) : Call<UserResponse>  {
        val user: Map<String, String> = mapOf(
            "email" to email,
            "password" to password
        )

        return apiService.userLogin(user)
    }

    fun userRegister(name: String, email: String, password: String) : Call<UserResponse>  {
        val user: Map<String, String> = mapOf(
            "name" to name,
            "email" to email,
            "password" to password
        )

        return apiService.userRegister(user)
    }

    private fun userStories(token: String): ApiService {
        val client = OkHttpClient.Builder()
            .addInterceptor(ApiInterceptor(token))
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    fun getUserStoryMapView(token: String) : Call<UserResponse> {
        return userStories(token).getUserStories(1)
    }

    @OptIn(ExperimentalPagingApi::class)
    fun getUserStoryList(token: String) : LiveData<PagingData<StoryModel>> {
        Log.e("getUserStoryList", "run getUserStoryList")
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            remoteMediator = StoryRemoteMediator(
                userStoryDatabase,
                apiService = userStories(token)
            ),
            pagingSourceFactory = { userStoryDatabase.userStoryDao().getAllUserStories() }
        ).liveData
    }

    fun uploadStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        token: String,
        lat: Float? = null,
        lon: Float? = null): Call<UserResponse> = userStories(token).postUserStory(photo, description, lat, lon)

    companion object {
        @Volatile
        private var instance: UserRepository? = null

        @JvmStatic
        fun getInstance(
            pref: SettingPreference,
            apiService: ApiService,
            userStoryDatabase: UserStoryDatabase,
            appExecutors: AppExecutors
        ) : UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(pref, apiService, userStoryDatabase, appExecutors)
            }.also { instance = it }
    }

}