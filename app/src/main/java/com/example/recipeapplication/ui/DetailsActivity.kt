package com.example.recipeapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.recipeapplication.R
import com.example.recipeapplication.adapters.ViewPagerAdapter
import com.example.recipeapplication.data.database.FavoriteRecipesEntity
import com.example.recipeapplication.models.Result
import com.example.recipeapplication.ui.fragments.InstructionsFragment
import com.example.recipeapplication.ui.fragments.IngredientsFragment
import com.example.recipeapplication.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_details.*

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {
    private val args by navArgs<DetailsActivityArgs>()
    private val mainViewModel : MainViewModel by viewModels()
    private var recipeAdded = false
    private var addedRecipeId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSupportActionBar(topAppBar)
        topAppBar.setTitleTextColor(ContextCompat.getColor(this,R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

       val fragments = ArrayList<Fragment>()
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Ingredients")
        titles.add("Instructions")

        val resultBundle = Bundle()
        resultBundle.putParcelable("recipeBundle",args.result)

        val adapter = ViewPagerAdapter(
            resultBundle,
            fragments,
            titles,
            supportFragmentManager
        )
        recipeViewPager.adapter = adapter
        recipeTabLayout.setupWithViewPager(recipeViewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.top_app_bar,menu)
        Log.d("DetailActivity","on option created")
        val menuItem = menu?.findItem(R.id.addToFavorites)
        changeFavoriteIconColor(menuItem!!,R.color.white)
        checkSavedRecipes(menuItem)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            finish()
        }else if(item.itemId == R.id.addToFavorites && !recipeAdded){
            addToFavorites(item)
        } else if(item.itemId == R.id.addToFavorites && recipeAdded){
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavorites.observe(this,{ favoriteRecipesEntity ->
            try {
                for (addedRecipe in favoriteRecipesEntity){
                    if (addedRecipe.result.title == args.result.title) {
                        changeFavoriteIconColor(menuItem,R.color.pink)
                        addedRecipeId = addedRecipe.id
                        print(args.result.recipeId)
                        Log.d("DetailActivity","@{args.result.recipeId}in the favorites")
                        recipeAdded = true
                    }
                    /*else{
                        Log.d("DetailActivity","NOT in the favorites")
                        changeFavoriteIconColor(menuItem,R.color.white)
                    }*/
                }
            }catch (e:Exception){
                Log.d("DetailActivity",e.message.toString())
            }
        })
    }

    private fun addToFavorites(item: MenuItem) {
        val favoriteRecipesEntity = FavoriteRecipesEntity(0,args.result)
        mainViewModel.addToFavorites(favoriteRecipesEntity)
        changeFavoriteIconColor(item,R.color.pink)
        showSnackBar("Successfully added To Favorites")
        recipeAdded = true
    }

    private fun removeFromFavorites(item: MenuItem){
        val favoriteRecipesEntity = FavoriteRecipesEntity(addedRecipeId,args.result)
        mainViewModel.removeFromFavorites(favoriteRecipesEntity)
        changeFavoriteIconColor(item, R.color.white)
        showSnackBar("Removed from Favorites.")
        recipeAdded = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(detailsLayout,message,Snackbar.LENGTH_SHORT).setAction("Okay"){}.show()

    }

    private fun changeFavoriteIconColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this,color))
    }
}