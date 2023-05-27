package com.example.recipeapplication.adapters.bindingadapter

import android.accounts.AuthenticatorDescription
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.example.recipeapplication.models.Result
import com.example.recipeapplication.ui.DetailsActivityArgs
import com.example.recipeapplication.ui.MainActivity
import com.example.recipeapplication.ui.fragments.RecipesFragment
import com.example.recipeapplication.ui.fragments.RecipesFragmentDirections
import org.jsoup.Jsoup
import java.lang.Exception

class RecipesRowBinding {

    companion object{

        @BindingAdapter("onRecipeClickListener")
        @JvmStatic
        fun onRecipeClickListener(recipeRowLayout: ConstraintLayout,result: Result){
            recipeRowLayout.setOnClickListener {
                try {
                    val action = RecipesFragmentDirections.actionRecipesFragmentToDetailsActivity(result)
                    recipeRowLayout.findNavController().navigate(action)
                }catch (e:Exception){
                    Log.d("onRecipeClick",e.toString())
                }
            }
        }

        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl:String){
            Glide.with(imageView.context)
                .load(imageUrl)
                .centerCrop()
                .circleCrop()
                .into(imageView)
        }
        @BindingAdapter("setReadyMinutes")
        @JvmStatic
        fun setNumberOfLikes(textView: TextView,minutes:Int){
            textView.text = minutes.toString()
        }

        @BindingAdapter("parseHtml")
        @JvmStatic
        fun parseHtml(textView: TextView,description: String?){
            if (description != null){
                val desc = Jsoup.parse(description).text()
                textView.text = desc
            }
        }
    }
}