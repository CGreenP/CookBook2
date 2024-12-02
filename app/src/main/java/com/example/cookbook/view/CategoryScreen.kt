package com.example.cookbook.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import coil3.compose.rememberAsyncImagePainter
import com.example.cookbook.R
import com.example.cookbook.model.dataclass.MealCategories
import com.example.cookbook.ui.ScreenHome
import com.example.cookbook.view.utils.BottomNavigationAppBar
import com.example.cookbook.view.utils.TopAppBarWithSearch
import com.example.cookbook.viewmodel.MainViewModel
import com.example.cookbook.viewmodel.MainViewModel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(paddingValues: PaddingValues, viewModel: MainViewModel, navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val mealCategoriesState = viewModel.mealCategoriesState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBarWithSearch(
                viewModel = viewModel,
                scrollBehavior = scrollBehavior,
                title = "Categories",
                onBackNavClicked = {
                    navController.navigate(ScreenHome){
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    viewModel.setBottomNavSelectedItemIndex(0) },
                navController = navController
            )
        },
        bottomBar = { BottomNavigationAppBar(navController=navController,viewModel=viewModel) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (mealCategoriesState.value) {
                is UiState.Loading -> {/* Show loading indicator */
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                is UiState.Success -> {/* Display data */
                    val mealCategories =
                        (mealCategoriesState.value as UiState.Success<List<MealCategories>>).data
                    CategoryScreenGrid(mealCategories = mealCategories)
                }

                is UiState.Error -> {/* Show error message */
                    val errorMessage = (mealCategoriesState.value as UiState.Error).message
                    Text(text = "Error: $errorMessage")
                }
            }
        }
    }
}

@Composable
fun CategoryScreenGrid(mealCategories: List<MealCategories>) {
    if (mealCategories.isNotEmpty()) {
        LazyVerticalGrid(GridCells.Adaptive(200.dp), modifier = Modifier.fillMaxSize()) {
            items(mealCategories, key = { it.idCategory }) { mealCategories ->
                CategoryGridItem(mealCategories = mealCategories)
            }
        }
    } else {
        Text(text = "No categories found")
    }
}

@Composable
fun CategoryGridItem(mealCategories: MealCategories) {
    OutlinedCard(modifier = Modifier
        .padding(8.dp)
        .fillMaxSize(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        onClick = {}) {
        Column(
            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = mealCategories.strCategoryThumb,
                    placeholder = painterResource(id = R.drawable.placeholder)
                ),
                contentDescription = mealCategories.strCategory,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    //.height(100.dp)
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium)

            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = mealCategories.strCategory,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}