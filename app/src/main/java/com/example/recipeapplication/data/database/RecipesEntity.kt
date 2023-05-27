package com.example.recipeapplication.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipeapplication.models.FoodRecipe
import com.example.recipeapplication.util.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0

}