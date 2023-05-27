package com.example.recipeapplication.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.recipeapplication.R
import com.example.recipeapplication.data.database.FavoriteRecipesEntity
import com.example.recipeapplication.databinding.FavoriteListItemBinding
import com.example.recipeapplication.ui.fragments.FavouritesFragmentDirections
import com.example.recipeapplication.util.RecipesDiffUtil
import com.example.recipeapplication.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favorite_list_item.view.*

class FavoritesAdapter(private val requireActivity: FragmentActivity, private val mainViewModel: MainViewModel) :
    RecyclerView.Adapter<FavoritesAdapter.MyViewHolder>(), ActionMode.Callback {
    private var favoriteRecipes = emptyList<FavoriteRecipesEntity>()

    private lateinit var mActionMode: ActionMode
    private lateinit var rootView: View
    private var multiSelection = false
    private var selectedRecipes = arrayListOf<FavoriteRecipesEntity>()
    private var myViewHolders = arrayListOf<MyViewHolder>()

    class MyViewHolder(private val binding: FavoriteListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favoriteRecipesEntity: FavoriteRecipesEntity) {
            binding.favorites = favoriteRecipesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteListItemBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        myViewHolders.add(holder)
        rootView = holder.itemView.rootView
        val currentRecipe = favoriteRecipes[position]
        holder.bind(currentRecipe)

        holder.itemView.favoriteRecipesBackground.setOnClickListener {
            if (multiSelection) {
                applySelection(holder, currentRecipe)
            } else {
                val action = FavouritesFragmentDirections.actionFavouritesFragmentToDetailsActivity(
                    currentRecipe.result
                )
                holder.itemView.findNavController().navigate(action)
            }
        }

        holder.itemView.favoriteRecipesBackground.setOnLongClickListener {
            if (!multiSelection) {
                multiSelection = true
                requireActivity.startActionMode(this)
                applySelection(holder, currentRecipe)
                true
            } else {
                multiSelection = false
                false
            }
        }
    }

    private fun applySelection(holder: MyViewHolder, currentRecipe: FavoriteRecipesEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.lightGray)
            actionModeTitle()
        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLight, R.color.purple_500)
            actionModeTitle()
        }
    }

    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.itemView.favoriteRecipesBackground.setBackgroundColor(
            ContextCompat.getColor(
                requireActivity,
                backgroundColor
            )
        )
        holder.itemView.favoriteRecipeCardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun actionModeTitle(){
        when(selectedRecipes.size){
            0 ->{
                mActionMode.finish()
            }
            1->{
                mActionMode.title = "1 item selected"
            }
            else->{
                mActionMode.title = "${selectedRecipes.size} items selected"
            }
        }
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }

    fun setData(newFavorites: List<FavoriteRecipesEntity>) {
        val favoritesDiffUtil = RecipesDiffUtil(favoriteRecipes, newFavorites)
        val diffUtilResult = DiffUtil.calculateDiff(favoritesDiffUtil)
        favoriteRecipes = newFavorites
        diffUtilResult.dispatchUpdatesTo(this)
    }

    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        mode?.menuInflater?.inflate(R.menu.favorites_menu, menu)
        mActionMode = mode!!
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        if (item?.itemId == R.id.favoriteRecipeDeleteIcon){
            selectedRecipes.forEach{
                mainViewModel.removeFromFavorites(it)
            }
            showSnackBar("${selectedRecipes.size} Recipe(s) removed")
            multiSelection = false
            selectedRecipes.clear()
            mode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        myViewHolders.forEach{holder->
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.lightGray)
        }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    private fun showSnackBar(message:String){
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
            .show()
    }
    fun clearContextualActionMode(){
        if (this::mActionMode.isInitialized){
            mActionMode.finish()
        }
    }
    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }
}