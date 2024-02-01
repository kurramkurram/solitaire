package io.github.kurramkurram.solitaire.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val NO_MORE_CHECKBOX_KEY = booleanPreferencesKey("key_no_more_checkbox")
val NO_MORE_CHECKBOX_MOVIE_KEY = booleanPreferencesKey("key_no_more_checkbox_movie")
val MUSIC_PLAY_CHECK_KEY = booleanPreferencesKey("key_music_play_check")
val APP_CONFIRM = booleanPreferencesKey("key_app_confirm")

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

/**
 * DataStoreへの保存.
 */
suspend fun setPreference(
    context: Context,
    key: Preferences.Key<Boolean>,
    value: Boolean
) = context.dataStore.edit {
    it[key] = value
}

/**
 * DataStoreからの取得.
 */
fun getPreference(
    context: Context,
    key: Preferences.Key<Boolean>,
    defValue: Boolean
): Flow<Boolean> =
    context.dataStore.data.map {
        it[key] ?: defValue
    }
