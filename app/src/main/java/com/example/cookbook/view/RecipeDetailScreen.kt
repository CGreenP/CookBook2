package com.example.cookbook.view

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.rememberAsyncImagePainter
import com.example.cookbook.R
import com.example.cookbook.model.dataclass.Meal
import com.example.cookbook.view.utils.TopAppBarWithSearch
import com.example.cookbook.viewmodel.MainViewModel
import com.example.cookbook.viewmodel.MainViewModel.UiState
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlin.or

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    viewModel: MainViewModel,
    id: String,
    mealName: String,
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    )
) {
    viewModel.fetchRecipeDetail(id)
    val recipeDetailState by viewModel.recipeDetailState.collectAsStateWithLifecycle()

    Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
        TopAppBarWithSearch(
            viewModel = viewModel,
            scrollBehavior = scrollBehavior,
            title = mealName,
            onBackNavClicked = {
                navController.navigateUp()
            },
            navController = navController
        )
    }) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (recipeDetailState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                is UiState.Success -> {
                    val recipeDetailResult = (recipeDetailState as UiState.Success<List<Meal>>).data
                    RecipeDetailScreenView(
                        recipeDetailResult = recipeDetailResult,
                        viewModel = viewModel
                    )
                }

                is UiState.Error -> {
                    val errorMessage = (recipeDetailState as UiState.Error).message
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
fun RecipeDetailScreenView(recipeDetailResult: List<Meal>, viewModel: MainViewModel) {
    if (recipeDetailResult.isNotEmpty()) {
        LazyVerticalGrid(GridCells.Adaptive(220.dp), modifier = Modifier.fillMaxSize()) {
            item {
                RecipeDetailHeader(recipeDetailResult = recipeDetailResult)
            }
            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                RecipeDetailBottom(recipeDetailResult = recipeDetailResult)
            }
        }
    } else {
        Text(text = "No recipe found!")
    }
}

@Composable
fun RecipeDetailHeader(recipeDetailResult: List<Meal>) {
    val meal = recipeDetailResult.firstOrNull()
    if (meal != null) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                OutlinedCard(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    colors = CardDefaults.cardColors(MaterialTheme.colorScheme.onPrimary)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
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
            }
        }
    } else {
        // Handle the case where the list is empty
        Text("No recipe found")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailBottom(recipeDetailResult: List<Meal>) {
    val meal = recipeDetailResult.firstOrNull()
    if (meal != null) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                var state by remember { mutableStateOf(0) }
                val titles = listOf("Ingredients", "Instructions")
                Column(modifier = Modifier.fillMaxSize()) {
                    PrimaryTabRow(
                        selectedTabIndex = state
                    ) {
                        titles.forEachIndexed { index, title ->
                            Tab(
                                selected = state == index,
                                onClick = { state = index },
                                text = { Text(title) })
                        }
                    }
                    if (state == 0) {
                        RecipeDetailIngredients(meal = meal)
                    } else {
                        RecipeDetailInstructions(meal = meal)
                    }
                }
            }
        }
    } else {
        // Handle the case where the list is empty
        Text("No recipe found")
    }
}

@Composable
fun RecipeDetailIngredients(meal: Meal) {
    Column(modifier = Modifier.fillMaxSize()) {
        //TODO Ingredients
    }
}

@Composable
fun RecipeDetailInstructions(meal: Meal) {
    val instructions = meal.strInstructions.toString().split("\r\n")
    Column(modifier = Modifier.fillMaxSize()) {
        instructions.forEachIndexed { index, instruction ->
            Text(
                text = instruction, // Add step numbers
                style = MaterialTheme.typography.bodyMedium, // Use a suitable font size
                color = MaterialTheme.colorScheme.onSurface, // Use appropriate color
                modifier = Modifier.padding(
                    top = 2.dp,
                    bottom = 2.dp,
                    start = 8.dp,
                    end = 8.dp
                ) // Add spacing between steps
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Box(modifier = Modifier.padding(8.dp)) {
        YoutubeScreen(youtubeVideoId = meal.strYoutube.toString().substringAfterLast("v="))
        }
    }
}

@Composable
fun YoutubeScreen(
    youtubeVideoId: String,
) {
    LocalContext.current
    LocalLifecycleOwner.current
    AndroidView(factory = { context ->
        YouTubePlayerView(context).apply {
            addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(youtubeVideoId, 0f)
                }
            })
        }
    })
}

//@Composable
//fun YoutubeScreen(youtubeVideoId: String) {
//    val youtubePlayer = remember { mutableStateOf<YouTubePlayer?>(null) }
//
//    AndroidView(factory = { context ->
//        val iFramePlayerOptions = IFramePlayerOptions.Builder()
//            .controls(1)
//            .fullscreen(1) // Enable full-screen button
//            .build()
//
//        YouTubePlayerView(context).apply {
//            enableAutomaticInitialization = false // Disable automatic initialization
//            initialize(object : AbstractYouTubePlayerListener() {
//                override fun onReady(youTubePlayer: YouTubePlayer) {
//                    youtubePlayer.value = youTubePlayer
//                    youTubePlayer.cueVideo(youtubeVideoId, 0f)
//                }
//            }, true, iFramePlayerOptions) // Pass IFramePlayerOptions
//        }
//    })
//}

//@Composable
//fun YoutubeScreen(youtubeVideoId: String) {
//    val context = LocalContext.current
//    var isFullscreen by remember { mutableStateOf(false) }
//    val youtubePlayer = remember { mutableStateOf<YouTubePlayer?>(null) }
//    val activity = context as Activity
//
//    val youtubePlayerView = YouTubePlayerView(context).apply {
//        enableAutomaticInitialization = false
//        val iFramePlayerOptions = IFramePlayerOptions.Builder()
//            .controls(1)
//            .fullscreen(1)
//            .build()
//
//        initialize(object : AbstractYouTubePlayerListener() {
//            override fun onReady(youTubePlayer: YouTubePlayer) {
//                youtubePlayer.value = youTubePlayer
//                youTubePlayer.cueVideo(youtubeVideoId, 0f)
//            }
//        }, true, iFramePlayerOptions)
//
//        addFullscreenListener(object : FullscreenListener {
//            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: Function0<Unit>) {
//                isFullscreen = true
//            }
//
//            override fun onExitFullscreen() {
//                isFullscreen = false
//            }
//        })
//    }
//
//    // Fullscreen container
//    Box(modifier = Modifier.fillMaxSize()) {
//        if (isFullscreen) {
//            AndroidView(
//                factory = { youtubePlayerView },
//                modifier = Modifier.fillMaxSize()
//            )
//        } else {
//            AndroidView(
//                factory = { youtubePlayerView },
//                modifier = Modifier // Your default modifier
//            )
//        }
//    }
//
//    // LaunchedEffect to handle potential configuration changes
//    LaunchedEffect(isFullscreen) {
//        // If isFullscreen changes, you might need to trigger recomposition
//        // or handle other side effects here.
//    }
//}

//@Composable
//fun YoutubeScreen(youtubeVideoId: String) {
//    val context = LocalContext.current
//    var isFullscreen by remember { mutableStateOf(false) }
//    val youtubePlayer = remember { mutableStateOf<YouTubePlayer?>(null) }
//    val activity = context as Activity
//
//    val youtubePlayerView = remember {
//        YouTubePlayerView(context).apply {
//            enableAutomaticInitialization = false
//            val iFramePlayerOptions = IFramePlayerOptions.Builder()
//                .controls(1)
//                .fullscreen(1)
//                .build()
//
//            initialize(object : AbstractYouTubePlayerListener() {
//                override fun onReady(youTubePlayer: YouTubePlayer) {
//                    youtubePlayer.value = youTubePlayer
//                    youTubePlayer.cueVideo(youtubeVideoId, 0f)
//                }
//            }, true, iFramePlayerOptions)
//
//            addFullscreenListener(object : FullscreenListener {
//                override fun onEnterFullscreen(
//                    fullscreenView: View,
//                    exitFullscreen: Function0<Unit>
//                ) {
//                    isFullscreen = true
//
//                    // Hide system UI
//                    activity.window.decorView.systemUiVisibility = (
//                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
//                                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//                                    or View.SYSTEM_UI_FLAG_FULLSCREEN
//                            )
//                }
//
//                override fun onExitFullscreen() {
//                    isFullscreen = false
//
//                    // Show system UI
//                    activity.window.decorView.systemUiVisibility = (
//                            View.SYSTEM_UI_FLAG_VISIBLE
//                            )
//                }
//            })
//        }
//    }
//
//    // Directly render the YouTubePlayerView
//    AndroidView(
//        factory = { youtubePlayerView },
//        modifier = Modifier.fillMaxSize() // Or your desired modifier
//    )
//
//    // LaunchedEffect to handle potential configuration changes
//    LaunchedEffect(isFullscreen) {
//        // If isFullscreen changes, you might need to trigger recomposition
//        // or handle other side effects here.
//    }
//}