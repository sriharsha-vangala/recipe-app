package com.example.recipeapplication.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipeapplication.adapters.RecipesAdapter
import com.example.recipeapplication.databinding.FragmentRecipesBinding
import com.example.recipeapplication.ui.NetworkListener
import com.example.recipeapplication.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.recipeapplication.util.NetworkResult
import com.example.recipeapplication.util.observeOnce
import com.example.recipeapplication.viewmodels.MainViewModel
import com.example.recipeapplication.viewmodels.RecipesViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class RecipesFragment : Fragment() {
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var recipeViewModel: RecipesViewModel
    private var mealTypeChip = DEFAULT_MEAL_TYPE
    private var mealTypeChipId = 0
    private val mAdapter by lazy { RecipesAdapter() }

    private lateinit var networkListener: NetworkListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        recipeViewModel = ViewModelProvider(requireActivity()).get(RecipesViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        // val view=inflater.inflate(R.layout.fragment_recipes,container,false)

        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchApiData(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })


        recipeViewModel.readMealType.asLiveData().observe(viewLifecycleOwner, { value ->
            mealTypeChip = value.selectedMealType
            updateChip(value.selectedMealTypeId, binding.mealTypeChipGroup)
        })

        binding.mealTypeChipGroup.setOnCheckedChangeListener { group, checkedId ->
            val chip = group.findViewById<Chip>(checkedId)
            val selectedMealType = chip.text.toString().lowercase()
            mealTypeChip = selectedMealType
            mealTypeChipId = checkedId
            println("Recipe fragment setOnChangeListener")
            println(mealTypeChip)
            recipeViewModel.saveMealType(mealTypeChip, mealTypeChipId)
            lifecycleScope.launch {
                letTheDataBeUpdated()
            }
        }

        recipeViewModel.readBackOnline.observe(viewLifecycleOwner, {
            recipeViewModel.backOnline = it
        })
        lifecycleScope.launch {
            networkListener = NetworkListener()
            networkListener.checkNetworkAvailability(requireContext())
                .collect { status ->
                    Log.d("NetworkListener", status.toString())
                    recipeViewModel.networkStatus = status
                    recipeViewModel.showNetworkStatus()
                    readDatabase()
                }
        }
        //make function
        recipeViewModel.saveMealType(mealTypeChip, mealTypeChipId)
        setupRecyclerView()
        readDatabase()
        return binding.root
    }


    private fun updateChip(selectedMealTypeId: Int, mealTypeChipGroup: ChipGroup) {
        if (selectedMealTypeId != 0) {
            try {
                mealTypeChipGroup.findViewById<Chip>(selectedMealTypeId).isChecked = true
            } catch (e: Exception) {
                Log.d("RecipesFragment", e.message.toString())
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewRecipesList.adapter = mAdapter
        binding.recyclerViewRecipesList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun readDatabase() {
        lifecycleScope.launch {
            mainViewModel.getRecipes(recipeViewModel.applyQueries())
            mainViewModel.readRecipes.observeOnce(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    Log.d("RecipesFragment", "readDatabase() is called")
                    mAdapter.setData(database[0].foodRecipe)
                } else {
                    requestApiData()
                }
            })
        }
    }

    private fun requestApiData() {
        Log.d("RecipesFragment", "requestApiData() called")
        mainViewModel.getRecipes(recipeViewModel.applyQueries())
        mainViewModel.recipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {

                }
            }
        })
    }

    private fun searchApiData(searchQuery: String) {
        mainViewModel.searchRecipes(recipeViewModel.applySearchQuery(searchQuery))
        mainViewModel.searchedRecipesResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    Log.d("RecipesFragment", "Search Api")
                    val foodRecipe = response.data
                    foodRecipe?.let { mAdapter.setData(it) }
                }
                is NetworkResult.Error -> {
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is NetworkResult.Loading -> {
                }
            }
        })
    }

    private suspend fun letTheDataBeUpdated() {
        delay(100)
        requestApiData()
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readRecipes.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    mAdapter.setData(database[0].foodRecipe)
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
