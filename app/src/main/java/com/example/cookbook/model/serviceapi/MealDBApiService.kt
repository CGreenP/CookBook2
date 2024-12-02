package com.example.cookbook.model.serviceapi

import com.example.cookbook.model.dataclass.AreaListResponse
import com.example.cookbook.model.dataclass.CategoryListResponse
import com.example.cookbook.model.dataclass.FilterByAreaResponse
import com.example.cookbook.model.dataclass.FilterByCategoryResponse
import com.example.cookbook.model.dataclass.FilterByMainIngredientResponse
import com.example.cookbook.model.dataclass.IngredientListResponse
import com.example.cookbook.model.dataclass.MealCategoriesResponse
import com.example.cookbook.model.dataclass.RecipeListResponse
import com.example.cookbook.model.dataclass.RecipeResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MealDBApiService {
    //List all Area
    //www.themealdb.com/api/json/v1/1/list.php?a=list
    @GET("list.php?a=list")
    suspend fun getAreasList(): AreaListResponse

    //List all Categories
    //www.themealdb.com/api/json/v1/1/list.php?c=list
    @GET("list.php?c=list")
    suspend fun getCategoriesList(): CategoryListResponse

    //Filter by Area
    //www.themealdb.com/api/json/v1/1/filter.php?a=Canadian
    @GET("filter.php")
    suspend fun filterByArea(@Query("a") area: String): FilterByAreaResponse

    //Filter by Category
    //www.themealdb.com/api/json/v1/1/filter.php?c=Seafood
    @GET("filter.php")
    suspend fun filterByCategory(@Query("c") category: String): FilterByCategoryResponse

    //Filter by main ingredient
    //www.themealdb.com/api/json/v1/1/filter.php?i=chicken_breast
    @GET("filter.php")
    suspend fun filterByIngredient(@Query("i") ingredient: String): FilterByMainIngredientResponse

    //List all Ingredients
    //www.themealdb.com/api/json/v1/1/list.php?i=list
    @GET("list.php?i=list")
    suspend fun getIngredientsList(): IngredientListResponse

    //List all meal categories
    //www.themealdb.com/api/json/v1/1/categories.php
    @GET("categories.php")
    suspend fun getCategories(): MealCategoriesResponse

    //Search meal by name
    //www.themealdb.com/api/json/v1/1/search.php?s=Arrabiata
    @GET("search.php")
    suspend fun searchMealsByName(@Query("s") query: String): RecipeResponse

    //Lookup full meal details by id
    //www.themealdb.com/api/json/v1/1/lookup.php?i=52772
    @GET("lookup.php")
    suspend fun getMealById(@Query("i") id: String): RecipeResponse

    //Lookup a single random meal
    //www.themealdb.com/api/json/v1/1/random.php
    @GET("random.php")
    suspend fun getRandomRecipe(): RecipeResponse

    //List all meals by first letter
    //www.themealdb.com/api/json/v1/1/search.php?f=a
    @GET("search.php")
    suspend fun searchMealsByFirstLetter(@Query("f") query: String): RecipeListResponse

}