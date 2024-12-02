package com.example.cookbook.view

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.cookbook.R
import com.example.cookbook.model.dataclass.AreaList
import com.example.cookbook.model.dataclass.IngredientList
import com.example.cookbook.model.dataclass.Meal
import com.example.cookbook.model.dataclass.MealCategories
import com.example.cookbook.ui.ScreenRecipeDetail
import com.example.cookbook.view.utils.BottomNavigationAppBar
import com.example.cookbook.view.utils.TopAppBarWithSearch
import com.example.cookbook.viewmodel.MainViewModel
import com.example.cookbook.viewmodel.MainViewModel.UiState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    paddingValues: PaddingValues,
    viewModel: MainViewModel,
    navController: NavController
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    viewModel.setBottomNavSelectedItemIndex(0)
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBarWithSearch(
                viewModel = viewModel,
                scrollBehavior = scrollBehavior,
                title = "CookBook 📚",
                onBackNavClicked = {},
                navController = navController
            )
        },
        bottomBar = {
            BottomNavigationAppBar(
                navController = navController,
                viewModel = viewModel
            )
        }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyVerticalGrid(GridCells.Adaptive(220.dp), modifier = Modifier.fillMaxSize()) {
                item {
                    RandomRecipeOfTheDay(viewModel, navController = navController)
                }
                item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                    QuickAccess(viewModel)
                }
            }
        }
    }
}

@Composable
fun QuickAccess(viewModel: MainViewModel) {
    val mealCategoriesState = viewModel.mealCategoriesState.collectAsStateWithLifecycle()
    val mealCategoriesListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val areaState = viewModel.areaListState.collectAsStateWithLifecycle()
    val areaListState = rememberLazyListState()
    val ingredientsState = viewModel.popularIngredientsListState.collectAsStateWithLifecycle()
    val ingredientsListState = rememberLazyListState()
    ElevatedCard(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Quick Access",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 2.dp)
            Box {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Popular Categories",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(8.dp)
                        )
                        IconButton(onClick = {
                            coroutineScope.launch {
                                mealCategoriesListState.animateScrollToItem(0)
                            }
                        }) {
                            Icon(
                                tint = MaterialTheme.colorScheme.primary,
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Scroll to Start"
                            )
                        }
                    }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        when (mealCategoriesState.value) {
                            is UiState.Loading -> {/* Show loading indicator */
                                CircularProgressIndicator(Modifier.align(Alignment.Center))
                            }

                            is UiState.Success -> {/* Display data */
                                val mealCategories =
                                    (mealCategoriesState.value as UiState.Success<List<MealCategories>>).data
                                QuickAccessMealCategories(
                                    mealCategories = mealCategories,
                                    mealCategoriesListState = mealCategoriesListState
                                )
                            }

                            is UiState.Error -> {/* Show error message */
                                val errorMessage =
                                    (mealCategoriesState.value as UiState.Error).message
                                Text(text = "Error: $errorMessage")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Popular Cuisine",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(8.dp)
                        )
                        IconButton(onClick = {
                            coroutineScope.launch {
                                areaListState.animateScrollToItem(0)
                            }
                        }) {
                            Icon(
                                tint = MaterialTheme.colorScheme.primary,
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Scroll to Start"
                            )
                        }
                    }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        when (areaState.value) {
                            is UiState.Loading -> {/* Show loading indicator */
                                CircularProgressIndicator(Modifier.align(Alignment.Center))
                            }

                            is UiState.Success -> {/* Display data */
                                val areaList =
                                    (areaState.value as UiState.Success<List<AreaList>>).data
                                QuickAccessArea(
                                    areaList = areaList,
                                    areaListState = areaListState,
                                    viewModel = viewModel
                                )
                            }

                            is UiState.Error -> {/* Show error message */
                                val errorMessage = (areaState.value as UiState.Error).message
                                Text(text = "Error: $errorMessage")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Popular Ingredients",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(8.dp)
                        )
                        IconButton(onClick = {
                            coroutineScope.launch {
                                ingredientsListState.animateScrollToItem(0)
                            }
                        }) {
                            Icon(
                                tint = MaterialTheme.colorScheme.primary,
                                imageVector = Icons.Default.ArrowBackIosNew,
                                contentDescription = "Scroll to Start"
                            )
                        }
                    }
                    Box(modifier = Modifier.fillMaxWidth()) {
                        when (ingredientsState.value) {
                            is UiState.Loading -> {/* Show loading indicator */
                                CircularProgressIndicator(Modifier.align(Alignment.Center))
                            }

                            is UiState.Success -> {/* Display data */
                                val ingredientsList =
                                    (ingredientsState.value as UiState.Success<List<IngredientList>>).data
                                QuickAccessIngredients(
                                    ingredientsList = ingredientsList,
                                    ingredientsListState = ingredientsListState,
                                    viewModel = viewModel
                                )
                            }

                            is UiState.Error -> {/* Show error message */
                                val errorMessage = (ingredientsState.value as UiState.Error).message
                                Text(text = "Error: $errorMessage")
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Browse By Name",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        BrowseByName(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun BrowseByName(viewModel: MainViewModel) {
    AlphabetBrowseBar(onLetterSelected = { letter ->
        // Handle letter selection
        println("Selected letter: $letter")
    })
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AlphabetBrowseBar(
    onLetterSelected: (String) -> Unit, modifier: Modifier = Modifier
) {
    val letters = ('A'..'Z').map { it.toString() }

    FlowRow(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        letters.forEachIndexed { index, letter ->
            Button(
                onClick = {
                    onLetterSelected(letter)
                    //TODO: Navigate to detail page
                },
                //modifier = Modifier.padding(horizontal = 2.dp),
                //contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                ), border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
            ) {
                Text(
                    text = letter,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun QuickAccessIngredients(
    ingredientsList: List<IngredientList>,
    ingredientsListState: LazyListState,
    viewModel: MainViewModel
) {
    if (ingredientsList.isNotEmpty()) {
        LazyRow(
            state = ingredientsListState,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            items(ingredientsList, key = { it.idIngredient }) { ingredient ->
                Column {
                    QuickAccessIngredientsItem(ingredient = ingredient, viewModel = viewModel)
                }
            }
        }
    } else {
        Text(text = "No areas found")
    }
}

@Composable
fun QuickAccessIngredientsItem(ingredient: IngredientList, viewModel: MainViewModel) {
    OutlinedCard(modifier = Modifier,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        onClick = {
            //TODO: Navigate to detail page
        }) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = "https://www.themealdb.com/images/ingredients/" + ingredient.strIngredient + "-Small.png",
                    placeholder = painterResource(id = R.drawable.placeholder)
                ),
                contentDescription = ingredient.strDescription,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .height(100.dp)
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = ingredient.strIngredient,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp, start = 8.dp, end = 8.dp)
            )
        }
    }
}

@Composable
fun QuickAccessArea(
    areaList: List<AreaList>, areaListState: LazyListState, viewModel: MainViewModel
) {
    if (areaList.isNotEmpty()) {
        LazyRow(
            state = areaListState,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            items(areaList, key = { it.strArea }) { area ->
                Column {
                    QuickAccessAreaItem(area = area, viewModel = viewModel)
                }
            }
        }
    } else {
        Text(text = "No areas found")
    }
}

@Composable
fun QuickAccessAreaItem(area: AreaList, viewModel: MainViewModel) {
    OutlinedCard(modifier = Modifier,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        onClick = {
            //TODO: Navigate to detail page
        }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = viewModel.fetchFlagUrl(area.strArea),
                    placeholder = painterResource(id = R.drawable.placeholder)
                ),
                contentDescription = area.strArea,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth()
//                    .clip(MaterialTheme.shapes.medium)
            )
            Text(
                text = area.strArea,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun QuickAccessMealCategories(
    mealCategoriesListState: LazyListState, mealCategories: List<MealCategories>
) {
    if (mealCategories.isNotEmpty()) {
        LazyRow(
            state = mealCategoriesListState,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top
        ) {
            items(mealCategories, key = { it.idCategory }) { category ->
                Column {
                    QuickAccessMealCategoriesItem(category = category)
                }
            }
        }
    } else {
        Text(text = "No categories found")
    }
}

@Composable
fun QuickAccessMealCategoriesItem(category: MealCategories) {
    OutlinedCard(modifier = Modifier,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
        onClick = {
            //TODO: Navigate to detail page
        }) {
        Column(
            modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    model = category.strCategoryThumb,
                    placeholder = painterResource(id = R.drawable.placeholder)
                ),
                contentDescription = category.strCategory,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    //.height(96.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = category.strCategory,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }

}

@Composable
fun RandomRecipeOfTheDay(viewModel: MainViewModel, navController: NavController) {
    val randomRecipeState = viewModel.randomRecipeOfTheDayState.collectAsStateWithLifecycle()
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = "Recipe of the Day",
                modifier = Modifier.padding(8.dp),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), thickness = 2.dp)
            Box(modifier = Modifier.fillMaxWidth()) {
                when (randomRecipeState.value) {
                    is UiState.Loading -> {/* Show loading indicator */
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }

                    is UiState.Success -> {/* Display data */
                        val randomRecipeOfTheDay =
                            (randomRecipeState.value as UiState.Success<List<Meal>>).data
                        RandomRecipeOfTheDayItem(
                            randomRecipeOfTheDay = randomRecipeOfTheDay,
                            navController = navController
                        )
                    }

                    is UiState.Error -> {/* Show error message */
                        val errorMessage = (randomRecipeState.value as UiState.Error).message
                        Text(text = "Error: $errorMessage")
                    }
                }
            }
            Text(
                text = "Discover a new recipe every day!",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun RandomRecipeOfTheDayItem(randomRecipeOfTheDay: List<Meal>, navController: NavController) {
    val meal = randomRecipeOfTheDay.firstOrNull()
    if (meal != null) {
        OutlinedCard(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary),
            onClick = {
                // TODO navigate to recipe detail screen
                navController.navigate(
                    ScreenRecipeDetail(
                        id = meal.idMeal,
                        mealName = meal.strMeal
                    )
                ) {
                    launchSingleTop = true
                }
            }) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = meal.strMealThumb,
                        placeholder = painterResource(id = R.drawable.placeholder)
                    ),
                    contentDescription = meal.strMeal,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(MaterialTheme.shapes.medium)
                )


                Text(
                    text = meal.strMeal,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    } else {
        // Handle the case where the list is empty
        Text("No recipe found")
    }
}