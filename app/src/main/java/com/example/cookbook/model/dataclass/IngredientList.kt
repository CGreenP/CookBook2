package com.example.cookbook.model.dataclass

/*
For
List all Ingredients
www.themealdb.com/api/json/v1/1/list.php?i=list
 */


data class IngredientListResponse(
    val meals: List<IngredientList>
)

data class IngredientList(
    val idIngredient: String,
    val strIngredient: String,
    val strDescription: String?,
    val strType: String?
)
