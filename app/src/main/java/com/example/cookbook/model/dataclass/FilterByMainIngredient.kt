package com.example.cookbook.model.dataclass

/*
For
Filter by main ingredient
www.themealdb.com/api/json/v1/1/filter.php?i=chicken_breast
 */

data class FilterByMainIngredientResponse(
    val meals: List<FilterByMainIngredient>,
)

data class FilterByMainIngredient(
    val strMeal: String,
    val strMealThumb: String,
    val idMeal: String,
)
