package com.example.recipeapplication.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.recipeapplication.R
import com.example.recipeapplication.models.Result
import kotlinx.android.synthetic.main.fragment_instructions.view.*

class InstructionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_instructions, container, false)
        val args= arguments
        val myBundle: Result? = args?.getParcelable("recipeBundle")

        view.instructionsWebView.webViewClient = object :WebViewClient(){}
        val websiteUrl:String = myBundle!!.sourceUrl
        view.instructionsWebView.loadUrl(websiteUrl)
        return view
    }

}