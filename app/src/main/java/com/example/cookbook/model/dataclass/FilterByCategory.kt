package com.example.cookbook.model.dataclass

/*
For
Filter by Category
www.themealdb.com/api/json/v1/1/filter.php?c=Seafood
 */

data class FilterByCategoryResponse(
    val meals: List<FilterByCategory>,
)

data class FilterByCategory(
    val strMeal: String,
    val strMealThumb: String,
    val idMeal: String,
)
