package com.example.cookbook.view.utils

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.cookbook.model.dataclass.Meal
import com.example.cookbook.ui.ScreenRecipeDetail
import com.example.cookbook.ui.ScreenSearch
import com.example.cookbook.viewmodel.MainViewModel
import com.example.cookbook.viewmodel.MainViewModel.UiState
import kotlinx.coroutines.flow.MutableStateFlow


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithSearch(
    viewModel: MainViewModel,
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    onBackNavClicked: () -> Unit = {},
    navController: NavController
) {
    val activeState = remember { MutableStateFlow(false) }
    val active by activeState.collectAsStateWithLifecycle()
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val searchResultMealsState by viewModel.searchResultMealsState.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val navigationIcon: @Composable () -> Unit = {
        if (!title.contains("CookBook 📚")) {
            IconButton(onClick = onBackNavClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        } else {
            null
        }
    }
    Column {
        CenterAlignedTopAppBar(title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
            navigationIcon = navigationIcon,
            actions = {
                if (!active) {
                    IconButton(onClick = { activeState.value = true }) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            scrollBehavior = scrollBehavior
        )
        // SearchBar
        if (active) {
            Box(
                modifier = Modifier.padding(WindowInsets.systemBars.asPaddingValues())
            ) {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        OutlinedTextField(value = searchText,
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text, imeAction = ImeAction.Search
                            ),
                            keyboardActions = KeyboardActions(onSearch = {
                                Log.v("Search", "Go to Search Page")
                                activeState.value = false
                                navController.navigate(ScreenSearch(searchText)) {
                                    launchSingleTop = true
                                }
                                //SearchScreen(viewModel=viewModel,searchText=searchText,scrollBehavior=scrollBehavior)
                            }),
                            onValueChange = {
                                viewModel.onSearchTextChange(it)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            label = { Text(text = "Search") },
                            //placeholder = { Text(text = "Search") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                    contentDescription = "Search",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            supportingText = { Text(text = "What are you craving today?") },
                            singleLine = true, // Ensure single line input
                            trailingIcon = {
                                IconButton(onClick = {
                                    activeState.value = false
                                }) {
                                    Icon(
                                        Icons.Filled.Close,
                                        contentDescription = "Clear",
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }
                            },
                            shape = MaterialTheme.shapes.medium,
                            colors = OutlinedTextFieldDefaults.colors(MaterialTheme.colorScheme.primary),
                            isError = searchResultMealsState is UiState.Error
                        )
                        if (searchText.isBlank() && !isSearching) {

                            Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = "Search",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .size(28.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                                Text(
                                    text = "Start typing to search...",
                                    style = MaterialTheme.typography.headlineMedium,
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colorScheme.primary
                                )

                            }
                        } else if (isSearching) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.Center)
                                )
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                when (searchResultMealsState) {
                                    is UiState.Loading -> {
                                        item {
                                            CircularProgressIndicator(
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .align(Alignment.CenterHorizontally)
                                            )
                                        }
                                    }

                                    is UiState.Success -> {
                                        items((searchResultMealsState as UiState.Success<List<Meal>>).data,
                                            key = { it.idMeal }) { meal ->
                                            Text(text = meal.strMeal,
                                                style = MaterialTheme.typography.headlineMedium,
                                                color = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier
                                                    .clickable {
                                                        navController.navigate(
                                                            ScreenRecipeDetail(
                                                                id = meal.idMeal,
                                                                mealName = meal.strMeal
                                                            )
                                                        ) {
                                                            launchSingleTop = true
                                                        }
                                                    }
                                                    .fillMaxWidth()
                                                    .padding(start = 24.dp))
                                            HorizontalDivider(
                                                modifier = Modifier.padding(
                                                    vertical = 4.dp
                                                ), thickness = 1.dp
                                            )
                                        }
                                    }

                                    is UiState.Error -> {
                                        item {
                                            Text(
                                                text = (searchResultMealsState as UiState.Error).message,
                                                style = MaterialTheme.typography.headlineMedium,
                                                color = MaterialTheme.colorScheme.error,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                        item {
                                            Icon(
                                                imageVector = Icons.Filled.SentimentDissatisfied,
                                                contentDescription = "Error",
                                                tint = MaterialTheme.colorScheme.error,
                                                modifier = Modifier.size(28.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}