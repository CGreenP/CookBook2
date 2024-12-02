package com.example.cookbook.model.dataclass

/*
For
List all Categories
www.themealdb.com/api/json/v1/1/list.php?c=list
*/

data class CategoryListResponse(
    val meals: List<CategoryList>,
)

data class CategoryList(
    val strCategory: String,
)
