package com.example.recipeapplication.data

import com.example.recipeapplication.data.database.FavoriteRecipesEntity
import com.example.recipeapplication.data.database.RecipesDao
import com.example.recipeapplication.data.database.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {
    fun readDatabase(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }
    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        recipesDao.insertRecipes(recipesEntity)
    }
    fun readFavoriteRecipes():Flow<List<FavoriteRecipesEntity>>{
       return recipesDao.readFavoriteRecipes()
    }
    suspend fun insertFavoriteRecipes(favoriteRecipesEntity: FavoriteRecipesEntity){
        recipesDao.insertFavoriteRecipe(favoriteRecipesEntity)
    }

    suspend fun deleteAllRecipes(){
        recipesDao.deleteAllFavoriteRecipes()
    }
    suspend fun deleteAFavoriteRecipe(favoriteRecipesEntity: FavoriteRecipesEntity){
        recipesDao.deleteAFavoriteRecipe(favoriteRecipesEntity)
    }
}