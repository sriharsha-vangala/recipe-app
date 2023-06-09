package com.example.recipeapplication.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.recipeapplication.R
import kotlinx.android.synthetic.main.fragment_home.view.*


class HomeFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
       // val view=inflater.inflate(R.layout.fragment_first, container, false)

        view.btnGetStarted.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.homeToRecipes) }
        return view
    }
}