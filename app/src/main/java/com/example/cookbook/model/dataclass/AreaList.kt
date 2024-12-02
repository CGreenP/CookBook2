package com.example.cookbook.model.dataclass

/*
For
List all Area
www.themealdb.com/api/json/v1/1/list.php?a=list
 */

data class AreaListResponse(
    val meals: List<AreaList>
)

data class AreaList(
    val strArea: String
)
