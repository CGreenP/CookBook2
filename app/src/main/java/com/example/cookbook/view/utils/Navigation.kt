package com.example.cookbook.view.utils

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.cookbook.ui.ScreenCategory
import com.example.cookbook.ui.ScreenCuisine
import com.example.cookbook.ui.ScreenHome
import com.example.cookbook.ui.ScreenIngredients
import com.example.cookbook.ui.ScreenRecipeDetail
import com.example.cookbook.ui.ScreenSearch
import com.example.cookbook.view.CategoryScreen
import com.example.cookbook.view.CuisineScreen
import com.example.cookbook.view.HomeScreen
import com.example.cookbook.view.IngredientsScreen
import com.example.cookbook.view.RecipeDetailScreen
import com.example.cookbook.view.SearchScreen
import com.example.cookbook.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(
    viewModel: MainViewModel, // Provides the ViewModel instance
    navController: NavHostController = rememberNavController(), // Creates or remembers the NavController
    innerPadding: PaddingValues // Padding values from the parent composable (e.g., Scaffold)
) {
    NavHost(
        navController = navController,// The NavController to manage navigation
        startDestination = ScreenHome
    ) {
        composable<ScreenHome> {
            HomeScreen(
                paddingValues = innerPadding,
                viewModel = viewModel,
                navController = navController
            )
        }
        composable<ScreenCategory> {
            CategoryScreen(
                paddingValues = innerPadding,
                viewModel = viewModel,
                navController = navController
            )
        }
        composable<ScreenCuisine> {
            CuisineScreen(
                paddingValues = innerPadding,
                viewModel = viewModel,
                navController = navController
            )
        }
        composable<ScreenIngredients> {
            IngredientsScreen(
                paddingValues = innerPadding,
                viewModel = viewModel,
                navController = navController
            )
        }
        composable<ScreenSearch> {
            val searchText = it.toRoute<ScreenSearch>()
            SearchScreen(
                viewModel = viewModel,
                searchText = searchText.searchText,
                navController = navController
            )
        }
        composable<ScreenRecipeDetail> {
            val id = it.toRoute<ScreenRecipeDetail>()
            val mealName = it.toRoute<ScreenRecipeDetail>()
            RecipeDetailScreen(
                viewModel = viewModel,
                id = id.id,
                mealName = mealName.mealName,
                navController = navController
            )
        }
    }
}