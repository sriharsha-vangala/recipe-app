package com.example.recipeapplication.data

import com.example.recipeapplication.data.network.FoodRecipesApi
import com.example.recipeapplication.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {
    suspend fun getRecipes(queries:Map<String,String>):Response<FoodRecipe>{
          return foodRecipesApi.getRecipes(queries)
    }

    suspend fun searchRecipes(searchQuery: Map<String, String>):Response<FoodRecipe>{
        return foodRecipesApi.searchRecipes(searchQuery)
    }

}