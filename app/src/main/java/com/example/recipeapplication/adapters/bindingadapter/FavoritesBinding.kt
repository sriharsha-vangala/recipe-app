package com.example.recipeapplication.adapters.bindingadapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapplication.adapters.FavoritesAdapter
import com.example.recipeapplication.data.database.FavoriteRecipesEntity

class FavoritesBinding {
    companion object {
        @BindingAdapter("viewVisibility","setData",requireAll = false)
        @JvmStatic
        fun setVisibility(view: View, favoriteRecipesEntity: List<FavoriteRecipesEntity>?, mAdapter: FavoritesAdapter?) {
            if(favoriteRecipesEntity.isNullOrEmpty()){
                when(view){
                    is ImageView->{
                        view.visibility = View.VISIBLE
                    }
                    is TextView ->{
                        view.visibility = View.VISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.INVISIBLE
                    }
                }
            }
            else{
                when(view){
                    is ImageView->{
                        view.visibility = View.INVISIBLE
                    }
                    is TextView ->{
                        view.visibility = View.INVISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        mAdapter?.setData(favoriteRecipesEntity)
                    }
                }
            }
        }
    }
}