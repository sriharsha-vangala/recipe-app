package com.example.recipeapplication.util

class Constants {
    companion object{
        const val API_KEY="ed100786cf3c434a8dfa61adab31318f"
        const val BASE_URL= "https://api.spoonacular.com"
        const val BASE_IMAGE_URL = "https://spoonacular.com/cdn/ingredients_100x100/"
        // API query keys

        const val QUERY_SEARCH ="query"
        const val QUERY_NUMBER ="number"
        const val QUERY_API_KEY ="apiKey"
        const val QUERY_TYPE ="type"
        const val QUERY_ADD_RECIPE_INFORMATION ="addRecipeInformation"
        const val QUERY_FILL_INGREDIENTS="fillIngredients"

        // Database
        const val DATABASE_NAME = "recipes_database"
        const val RECIPES_TABLE = "recipes_table"

        const val DEFAULT_MEAL_TYPE = "main course"
        const val DEFAULT_RECIPES_NUMBER = "50"

        const val PREFERENCES_MEAL_TYPE = "mealType"
        const val PREFERENCES_MEAL_TYPE_ID = "mealTypeId"

        const val PREFERENCES_NAME = "foody_preferences"

        const val PREFERENCES_BACK_ONLINE = "backOnline"
    }
}