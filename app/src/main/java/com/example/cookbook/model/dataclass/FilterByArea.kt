package com.example.cookbook.model.dataclass

/*
For
Filter by Area
www.themealdb.com/api/json/v1/1/filter.php?a=Canadian
 */

data class FilterByAreaResponse(
    val meals: List<FilterByArea>,
)

data class FilterByArea(
    val strMeal: String,
    val strMealThumb: String,
    val idMeal: String,
)
