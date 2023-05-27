package com.example.recipeapplication.data.database

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.recipeapplication.models.FoodRecipe
import com.example.recipeapplication.models.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {
    var gson = Gson()
    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe):String{
        return gson.toJson(foodRecipe)

    }

    @TypeConverter
    fun stringToFoodRecipe(data:String):FoodRecipe{
        val listType = object : TypeToken<FoodRecipe>(){}.type
        return gson.fromJson(data,listType)
    }

    @TypeConverter
    fun resultToString(result: Result):String{
        return gson.toJson(result)
    }

    @TypeConverter
    fun stringToResult(data:String):Result{
        val listType = object : TypeToken<Result>(){}.type
        return gson.fromJson(data,listType)
    }
}