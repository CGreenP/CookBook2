package com.example.cookbook.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import coil3.compose.rememberAsyncImagePainter
import com.example.cookbook.R
import com.example.cookbook.model.dataclass.Meal
import com.example.cookbook.ui.ScreenRecipeDetail
import com.example.cookbook.view.utils.TopAppBarWithSearch
import com.example.cookbook.viewmodel.MainViewModel
import com.example.cookbook.viewmodel.MainViewModel.UiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(viewModel: MainViewModel, searchText: String, navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val searchResultMealsState by viewModel.searchResultMealsState.collectAsStateWithLifecycle()
    viewModel.onSearchTextChange(searchText)
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBarWithSearch(
                viewModel = viewModel,
                scrollBehavior = scrollBehavior,
                title = "Search Result for $searchText",
                onBackNavClicked = { navController.navigateUp() },
                navController = navController
            )
        }) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {
            when (searchResultMealsState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                is UiState.Success -> {
                    val mealSearchResults =
                        (searchResultMealsState as UiState.Success<List<Meal>>).data
                    SearchScreenGrid(
                        mealSearchResults = mealSearchResults,
                        viewModel = viewModel,
                        searchText = searchText,
                        navController = navController
                    )

                }

                is UiState.Error -> {
                    val errorMessage = (searchResultMealsState as UiState.Error).message
                    Column(Modifier.align(Alignment.Center)) {
                        Text(
                            text = errorMessage,
                            style = MaterialTheme.typography.headlineLarge,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                        Icon(
                            imageVector = Icons.Filled.SentimentDissatisfied,
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .size(32.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchScreenGrid(
    mealSearchResults: List<Meal>,
    viewModel: MainViewModel,
    searchText: String,
    navController: NavController
) {
    if (mealSearchResults.isNotEmpty()) {
        LazyVerticalGrid(GridCells.Adaptive(200.dp), modifier = Modifier.fillMaxSize()) {
            items(mealSearchResults, key = { it.idMeal }) { searchResults ->
                SearchGridItem(searchResults = searchResults, navController = navController)
            }
        }
    } else {
        Text(text = "No results found!")
    }
}


@Composable
fun SearchGridItem(searchResults: Meal, navController: NavController) {
    OutlinedCard(modifier = Modifier
        .padding(8.dp)
        .fillMaxSize(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        onClick = {
            //TODO: Navigate to detail page
            navController.navigate(
                ScreenRecipeDetail(
                    id = searchResults.idMeal,
                    mealName = searchResults.strMeal
                )
            ) {
                launchSingleTop = true
            }
        }) {
        Column(
            modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = searchResults.strMealThumb,
                    placeholder = painterResource(id = R.drawable.placeholder)
                ),
                contentDescription = searchResults.strMeal,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    //.height(100.dp)
                    .fillMaxSize()
                    .clip(MaterialTheme.shapes.medium)

            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = searchResults.strMeal,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}