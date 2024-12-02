package com.example.cookbook

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.cookbook.ui.theme.CookBookTheme
import com.example.cookbook.view.utils.Navigation
import com.example.cookbook.viewmodel.MainViewModel
import com.example.cookbook.viewmodel.MainViewModel.UiState

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.categoriesListState.value is UiState.Loading
                //viewModel.isReady.value
                //!viewModel.isReady.value
            }
            setOnExitAnimationListener { screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView, View.SCALE_X, 0.4f, 0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }
                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView, View.SCALE_Y, 0.4f, 0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }
        }
        enableEdgeToEdge()
        setContent {
            CookBookTheme {
//                LaunchedEffect(key1 = Unit) { // Trigger fetchCategories when composable enters
//                    viewModel.fetchDataBeforeStartUp()
//                }


                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    println(innerPadding)
//                    HomeScreen(
//                        paddingValues = innerPadding, viewModel = viewModel,navController = rememberNavController()
//                    )
                    //CategoryScreen(paddingValues = innerPadding, viewModel = viewModel)
                    //CuisineScreen(paddingValues = innerPadding, viewModel = viewModel)
                    //IngredientsScreen(paddingValues = innerPadding, viewModel = viewModel)
                    //SearchScreen(viewModel=viewModel,scrollBehavior=scrollBehavior)
                    //RecipeDetailScreen(viewModel=viewModel,scrollBehavior=scrollBehavior,id="52772")
                    Navigation(innerPadding = innerPadding, viewModel = viewModel)
                }
            }
        }
    }
}
