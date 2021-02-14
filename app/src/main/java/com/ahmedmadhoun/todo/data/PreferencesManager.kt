package com.ahmedmadhoun.todo.data

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

enum class SortOrder { BY_DATE_CREATED, BY_TITLE }

data class FilterPreferences(val sortOrder: SortOrder, val hideCompleted: Boolean)

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore("preferences_data_store")

    val preferencesFlow = dataStore.data.map { preferences ->
        val sortOrder = SortOrder.valueOf(
            preferences[PreferencesKey.SORT_ORDER] ?: SortOrder.BY_DATE_CREATED.name
        )
        val hideCompleted = preferences[PreferencesKey.HIDE_COMPLETED] ?: false

        FilterPreferences(sortOrder, hideCompleted)
    }

    suspend fun updateSortOrder(sortOrder: SortOrder) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateHideCompleted(hideCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.HIDE_COMPLETED] = hideCompleted
        }
    }

    private object PreferencesKey {
        val SORT_ORDER = preferencesKey<String>("sort_order")
        val HIDE_COMPLETED = preferencesKey<Boolean>("hide_completed")
    }
}
