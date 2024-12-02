package com.example.cookbook.ui

import kotlinx.serialization.Serializable

@Serializable
object ScreenHome

@Serializable
object ScreenCategory

@Serializable
object ScreenCuisine

@Serializable
object ScreenIngredients

@Serializable
data class ScreenSearch(
    val searchText: String
)

@Serializable
data class ScreenRecipeDetail(
    val id: String, val mealName: String
)