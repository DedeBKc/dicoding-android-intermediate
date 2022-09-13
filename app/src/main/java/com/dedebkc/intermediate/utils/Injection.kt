package com.dedebkc.intermediate.utils

import android.content.Context
import com.dedebkc.intermediate.BuildConfig
import com.dedebkc.intermediate.data.datastore.SettingPreference
import com.dedebkc.intermediate.data.UserRepository
import com.dedebkc.intermediate.data.database.UserStoryDatabase
import com.dedebkc.intermediate.data.network.ApiConfig
import com.dedebkc.intermediate.ui.mainmenu.dataStore
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object Injection {
    fun provideUserRepository(context: Context): UserRepository {
        val appExecutors = AppExecutors()
        val pref = SettingPreference.getInstance(context.dataStore)

        val loggingInterceptor = if(BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val userStoryDatabase = UserStoryDatabase.getDatabase(context)

        val apiService = ApiConfig.getApiService(client)

        return UserRepository.getInstance(pref, apiService, userStoryDatabase, appExecutors)
    }
}