package com.example.cookbook.model.repository

import com.example.cookbook.model.dataclass.AreaListResponse
import com.example.cookbook.model.dataclass.CategoryListResponse
import com.example.cookbook.model.dataclass.CountryFlagsList2
import com.example.cookbook.model.dataclass.FilterByAreaResponse
import com.example.cookbook.model.dataclass.FilterByCategoryResponse
import com.example.cookbook.model.dataclass.FilterByMainIngredientResponse
import com.example.cookbook.model.dataclass.IngredientListResponse
import com.example.cookbook.model.dataclass.MealCategoriesResponse
import com.example.cookbook.model.dataclass.RecipeListResponse
import com.example.cookbook.model.dataclass.RecipeResponse
import com.example.cookbook.model.retrofit.RetrofitInstance

class MealDBRepository {
    private val mealDBApiService = RetrofitInstance.mealDBApiService

    suspend fun getAreasList(): AreaListResponse = mealDBApiService.getAreasList()

    suspend fun getCategoriesList(): CategoryListResponse = mealDBApiService.getCategoriesList()

    suspend fun filterByArea(area: String): FilterByAreaResponse =
        mealDBApiService.filterByArea(area)

    suspend fun filterByCategory(category: String): FilterByCategoryResponse =
        mealDBApiService.filterByCategory(category)

    suspend fun filterByIngredient(ingredient: String): FilterByMainIngredientResponse =
        mealDBApiService.filterByIngredient(ingredient)

    suspend fun getIngredientsList(): IngredientListResponse = mealDBApiService.getIngredientsList()

    suspend fun getCategories(): MealCategoriesResponse = mealDBApiService.getCategories()

    suspend fun searchMealsByName(query: String): RecipeResponse =
        mealDBApiService.searchMealsByName(query)

    suspend fun getMealById(id: String): RecipeResponse = mealDBApiService.getMealById(id)

    suspend fun getRandomRecipe(): RecipeResponse = mealDBApiService.getRandomRecipe()

    suspend fun searchMealsByFirstLetter(query: String): RecipeListResponse =
        mealDBApiService.searchMealsByFirstLetter(query)

    fun fetchFlagUrl(areaName: String): String {
        val flagUrl = when (areaName) {
            "American" -> CountryFlagsList2.American.flagUrl
            "British" -> CountryFlagsList2.British.flagUrl
            "Canadian" -> CountryFlagsList2.Canadian.flagUrl
            "Chinese" -> CountryFlagsList2.Chinese.flagUrl
            "Croatian" -> CountryFlagsList2.Croatian.flagUrl
            "Dutch" -> CountryFlagsList2.Dutch.flagUrl
            "Egyptian" -> CountryFlagsList2.Egyptian.flagUrl
            "Filipino" -> CountryFlagsList2.Filipino.flagUrl
            "French" -> CountryFlagsList2.French.flagUrl
            "Greek" -> CountryFlagsList2.Greek.flagUrl
            "Indian" -> CountryFlagsList2.Indian.flagUrl
            "Irish" -> CountryFlagsList2.Irish.flagUrl
            "Italian" -> CountryFlagsList2.Italian.flagUrl
            "Jamaican" -> CountryFlagsList2.Jamaican.flagUrl
            "Japanese" -> CountryFlagsList2.Japanese.flagUrl
            "Kenyan" -> CountryFlagsList2.Kenyan.flagUrl
            "Malaysian" -> CountryFlagsList2.Malaysian.flagUrl
            "Mexican" -> CountryFlagsList2.Mexican.flagUrl
            "Moroccan" -> CountryFlagsList2.Moroccan.flagUrl
            "Polish" -> CountryFlagsList2.Polish.flagUrl
            "Portuguese" -> CountryFlagsList2.Portuguese.flagUrl
            "Russian" -> CountryFlagsList2.Russian.flagUrl
            "Spanish" -> CountryFlagsList2.Spanish.flagUrl
            "Thai" -> CountryFlagsList2.Thai.flagUrl
            "Tunisian" -> CountryFlagsList2.Tunisian.flagUrl
            "Turkish" -> CountryFlagsList2.Turkish.flagUrl
            "Ukrainian" -> CountryFlagsList2.Ukrainian.flagUrl
            "Vietnamese" -> CountryFlagsList2.Vietnamese.flagUrl
            else -> CountryFlagsList2.Unknown.flagUrl
        }
        return flagUrl
    }

}