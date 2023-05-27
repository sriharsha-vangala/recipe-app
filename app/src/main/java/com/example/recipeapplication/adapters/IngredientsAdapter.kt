package com.example.recipeapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipeapplication.R
import com.example.recipeapplication.models.ExtendedIngredient
import com.example.recipeapplication.util.Constants.Companion.BASE_IMAGE_URL
import com.example.recipeapplication.util.RecipesDiffUtil
import kotlinx.android.synthetic.main.ingredients_row_layout.view.*

class IngredientsAdapter:RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {
    private var ingredientsList = emptyList<ExtendedIngredient>()
    class MyViewHolder(itemView:View):RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ingredients_row_layout,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Glide.with(holder.itemView.context)
            .load(BASE_IMAGE_URL+ ingredientsList[position].image)
            .centerCrop()
            .into(holder.itemView.ingredientImageView)
        holder.itemView.ingredientName.text = ingredientsList[position].name.capitalize()
        holder.itemView.ingredientAmount.text = ingredientsList[position].amount.toString()
        holder.itemView.ingredientUnits.text = ingredientsList[position].unit

    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun setData(newIngredients: List<ExtendedIngredient>){
        val ingredientsDiffUtil = RecipesDiffUtil(ingredientsList,newIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientsList =  newIngredients
        diffUtilResult.dispatchUpdatesTo(this)
    }
}