package com.example.recipeapplication.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.recipeapplication.models.Result


@Entity(tableName = "favorite_recipes_table")
class FavoriteRecipesEntity(
    @PrimaryKey(autoGenerate = true)
    var id :Int,
    var result: Result
)