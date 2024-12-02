package com.example.cookbook.view.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Egg
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Egg
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.cookbook.model.dataclass.BottomNavItem
import com.example.cookbook.ui.ScreenCategory
import com.example.cookbook.ui.ScreenCuisine
import com.example.cookbook.ui.ScreenHome
import com.example.cookbook.ui.ScreenIngredients
import com.example.cookbook.viewmodel.MainViewModel


val items = listOf(
    BottomNavItem(
        title = "Home", selectedIcon = Icons.Filled.Home, unselectedIcon = Icons.Outlined.Home
    ), BottomNavItem(
        title = "Category",
        selectedIcon = Icons.Filled.Category,
        unselectedIcon = Icons.Outlined.Category
    ), BottomNavItem(
        title = "Cuisine",
        selectedIcon = Icons.Filled.Public,
        unselectedIcon = Icons.Outlined.Public
    ), BottomNavItem(
        title = "Ingredients", selectedIcon = Icons.Filled.Egg, unselectedIcon = Icons.Outlined.Egg
    )
)

@Composable
fun BottomNavigationAppBar(navController: NavController, viewModel: MainViewModel) {
    val selectedItemIndex by viewModel.bottomNavSelectedItemIndex.collectAsStateWithLifecycle()
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(selected = selectedItemIndex == index, onClick = {
                viewModel.setBottomNavSelectedItemIndex(index)
                when (item.title) {
                    "Home" -> navController.navigate(ScreenHome) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }

                    "Category" -> navController.navigate(ScreenCategory) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }

                    "Cuisine" -> navController.navigate(ScreenCuisine) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }

                    "Ingredients" -> navController.navigate(ScreenIngredients) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }, label = { Text(text = item.title) }, icon = {
                Icon(
                    imageVector = if (selectedItemIndex == index) {
                        item.selectedIcon
                    } else {
                        item.unselectedIcon
                    }, contentDescription = item.title
                )
            })
        }
    }
}