package com.dedebkc.intermediate.data.datastore

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dedebkc.intermediate.ui.mapmenu.MapStyle
import com.dedebkc.intermediate.ui.mapmenu.MapType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreference private constructor(private val dataStore: DataStore<Preferences>) {
    private val THEME_MODE_KEY = booleanPreferencesKey("theme_mode")
    private val USER_TOKEN_KEY = stringPreferencesKey("user_token")
    private val USER_EMAIL_KEY = stringPreferencesKey("user_email")
    private val USER_NAME_KEY = stringPreferencesKey("user_name")
    private val FIRST_TIME_KEY = booleanPreferencesKey("first_time")
    private val MAP_TYPE_KEY = stringPreferencesKey("map_type")
    private val MAP_STYLE_KEY = stringPreferencesKey("map_style")

    /**
     * User Token
     */

    fun getUserToken(): Flow<String> = dataStore.data.map {
        it[USER_TOKEN_KEY] ?: DEFAULT_VALUE
    }

    suspend fun saveUserToken(token: String) {
        dataStore.edit {
            it[USER_TOKEN_KEY] = token
            Log.e("SettingPreference", "Token saved! saveUserToken: $token")
        }
    }

    /**
     * User Email
     */

    fun getUserEmail(): Flow<String> = dataStore.data.map {
        it[USER_EMAIL_KEY] ?: DEFAULT_VALUE
    }

    suspend fun saveUserEmail(email: String) {
        dataStore.edit {
            it[USER_EMAIL_KEY] = email
        }
    }

    /**
     * User Name
     */

    fun getUserName(): Flow<String> = dataStore.data.map {
        it[USER_NAME_KEY] ?: DEFAULT_VALUE
    }

    suspend fun saveUserName(name: String) {
        dataStore.edit {
            it[USER_NAME_KEY] = name
        }
    }

    /**
     * First Time
     */

    fun isFirstTime(): Flow<Boolean> = dataStore.data.map {
        it[FIRST_TIME_KEY] ?: true
    }

    suspend fun saveIsFirstTime(firstTime: Boolean) {
        dataStore.edit {
            it[FIRST_TIME_KEY] = firstTime
        }
    }

    /**
     * Map Type
     */

    fun getMapType(): Flow<MapType> = dataStore.data.map {
        when (it[MAP_TYPE_KEY]) {
            MapType.NORMAL.name -> MapType.NORMAL
            MapType.SATELLITE.name -> MapType.SATELLITE
            MapType.TERRAIN.name -> MapType.TERRAIN
            else -> MapType.NORMAL
        }
    }

    suspend fun saveMapType(mapType: MapType) {
        dataStore.edit {
            it[MAP_TYPE_KEY] = when (mapType) {
                MapType.NORMAL -> MapType.NORMAL.name
                MapType.SATELLITE -> MapType.SATELLITE.name
                MapType.TERRAIN -> MapType.TERRAIN.name
            }
        }
    }

    /**
     * Map Style
     */

    fun getMapStyle(): Flow<MapStyle> = dataStore.data.map {
        when (it[MAP_STYLE_KEY]) {
            MapStyle.NORMAL.name -> MapStyle.NORMAL
            MapStyle.NIGHT.name -> MapStyle.NIGHT
            MapStyle.SILVER.name -> MapStyle.SILVER
            else -> MapStyle.NORMAL
        }
    }

    suspend fun saveMapStyle(mapStyle: MapStyle) {
        dataStore.edit {
            it[MAP_STYLE_KEY] = when (mapStyle) {
                MapStyle.NORMAL -> MapStyle.NORMAL.name
                MapStyle.NIGHT -> MapStyle.NIGHT.name
                MapStyle.SILVER -> MapStyle.SILVER.name
            }
        }
    }

    suspend fun clearCache() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingPreference? = null
        const val DEFAULT_VALUE = "not_set_yet"

        fun getInstance(dataStore: DataStore<Preferences>) : SettingPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}