package com.example.cookbook.model.dataclass

/*
For

List all meal categories
www.themealdb.com/api/json/v1/1/categories.php
*/

data class MealCategoriesResponse(
    val categories: List<MealCategories>
)

data class MealCategories(
    val idCategory: String,
    val strCategory: String,
    val strCategoryThumb: String,
    val strCategoryDescription: String
)
