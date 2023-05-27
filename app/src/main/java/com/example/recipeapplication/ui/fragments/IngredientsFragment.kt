package com.example.recipeapplication.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.recipeapplication.R
import com.example.recipeapplication.adapters.IngredientsAdapter
import com.example.recipeapplication.models.Result
import kotlinx.android.synthetic.main.fragment_ingredients.view.*
import org.jsoup.Jsoup

class IngredientsFragment : Fragment() {

    private val mAdapter : IngredientsAdapter by lazy { IngredientsAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_ingredients, container, false)
        val args = arguments
        val myBundle: Result? = args?.getParcelable("recipeBundle")

        Glide.with(view.context)
            .load(myBundle?.image)
            .centerCrop()
            .into(view.main_imageView)

        view.titleTextView.text = myBundle?.title
        view.recipeTime.text = myBundle?.readyInMinutes.toString()
        setupRecyclerView(view)

        myBundle?.extendedIngredients?.let { mAdapter.setData(it) }

        myBundle?.summary.let {
            val summary = Jsoup.parse(it).text()
            view.summaryTextView.text = summary
        }
        return view
    }

    private fun setupRecyclerView(view: View) {
        view.ingredientsRecyclerView.adapter = mAdapter
        view.ingredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
    }
}