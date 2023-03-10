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

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

suspend fun setPreference(
    context: Context,
    key: Preferences.Key<Boolean>,
    value: Boolean
) = context.dataStore.edit {
    it[key] = value
}

fun getPreference(
    context: Context,
    key: Preferences.Key<Boolean>,
    defValue: Boolean
): Flow<Boolean> =
    context.dataStore.data.map {
        it[key] ?: defValue
    }
