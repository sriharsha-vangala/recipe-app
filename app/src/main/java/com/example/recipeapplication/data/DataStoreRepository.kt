package com.example.recipeapplication.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.recipeapplication.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.recipeapplication.util.Constants.Companion.PREFERENCES_BACK_ONLINE
import com.example.recipeapplication.util.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.example.recipeapplication.util.Constants.Companion.PREFERENCES_MEAL_TYPE_ID
import com.example.recipeapplication.util.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {
        val selectedMealType = stringPreferencesKey(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = intPreferencesKey(PREFERENCES_MEAL_TYPE_ID)
        val backOnline = booleanPreferencesKey(PREFERENCES_BACK_ONLINE)
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

    suspend fun saveMealType(mealType: String, mealTypeId: Int) {
        println("DataStore Repository")
        println(mealType)
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.selectedMealType] = mealType
            preferences[PreferenceKeys.selectedMealTypeId] = mealTypeId
        }
    }

    suspend fun saveBackOnline(backOnline: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.backOnline] = backOnline
        }
    }


    val readMealType: Flow<MealType> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val selectedMealType = preferences[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId] ?: 0
            MealType(selectedMealType, selectedMealTypeId)
        }

    val readBackOnline: Flow<Boolean> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val backOnline = preferences[PreferenceKeys.backOnline] ?: false
            backOnline
        }
}


data class MealType(
    val selectedMealType: String,
    val selectedMealTypeId: Int
)