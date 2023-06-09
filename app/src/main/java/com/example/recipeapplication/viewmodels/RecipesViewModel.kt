package com.example.recipeapplication.viewmodels

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.recipeapplication.data.DataStoreRepository
import com.example.recipeapplication.util.Constants.Companion.API_KEY
import com.example.recipeapplication.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.recipeapplication.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.example.recipeapplication.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.recipeapplication.util.Constants.Companion.QUERY_API_KEY
import com.example.recipeapplication.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.example.recipeapplication.util.Constants.Companion.QUERY_NUMBER
import com.example.recipeapplication.util.Constants.Companion.QUERY_SEARCH
import com.example.recipeapplication.util.Constants.Companion.QUERY_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var mealType = DEFAULT_MEAL_TYPE

    var networkStatus = false
    var backOnline = false

    val readMealType = dataStoreRepository.readMealType
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveMealType(mealType: String, mealTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            println("Recipe ViewModel")
            println(mealType)
            dataStoreRepository.saveMealType(mealType, mealTypeId)
        }

    private fun saveBackOnline(backOnline: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        viewModelScope.launch {
            readMealType.collect { value ->
                mealType = value.selectedMealType
            }
        }
        println(mealType)
        println("Recipes View Model")
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealType
       // queries[QUERY_DIET] = "vegan"
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        println(queries)
        return queries
    }

    fun applySearchQuery(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    fun showNetworkStatus() {
        if (!networkStatus) {
            Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_SHORT).show()
            saveBackOnline(true)
        } else if (networkStatus) {
            if (backOnline) {
                Toast.makeText(getApplication(), "Back Online ", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }

}