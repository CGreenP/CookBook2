package com.example.cookbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cookbook.model.dataclass.AreaList
import com.example.cookbook.model.dataclass.CategoryList
import com.example.cookbook.model.dataclass.IngredientList
import com.example.cookbook.model.dataclass.Meal
import com.example.cookbook.model.dataclass.MealCategories
import com.example.cookbook.model.repository.MealDBRepository
import com.example.cookbook.model.repository.RestCountriesRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val mealDBRepository = MealDBRepository()
    private val restCountriesRepository = RestCountriesRepository()

    private val _bottomNavSelectedItemIndex = MutableStateFlow(0)
    val bottomNavSelectedItemIndex = _bottomNavSelectedItemIndex.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchResultMealsState = MutableStateFlow<UiState<List<Meal>>>(UiState.Loading)

    @OptIn(FlowPreview::class)
    val searchResultMealsState = searchText.debounce(1000L).onEach {
        _isSearching.update { true }
    }.combine(_searchResultMealsState) { text, result ->
        if (text.isBlank()) {
            result
        } else {
            try {
                val response = mealDBRepository.searchMealsByName(text)
                if (response.meals == null || response.meals.isEmpty()) {
                    UiState.Error("No results found for '$text'") // Display custom message
                } else {
                    UiState.Success(response.meals)
                }
            } catch (e: Exception) {
                UiState.Error("Failed to search meals: ${e.message}")
            }
        }
    }.onEach {
        _isSearching.update { false }
    }.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), _searchResultMealsState.value
    )

    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady.asStateFlow()

    private val _randomRecipeOfTheDayState = MutableStateFlow<UiState<List<Meal>>>(UiState.Loading)
    val randomRecipeOfTheDayState: StateFlow<UiState<List<Meal>>> =
        _randomRecipeOfTheDayState.asStateFlow()

    private val _areaListState = MutableStateFlow<UiState<List<AreaList>>>(UiState.Loading)
    val areaListState: StateFlow<UiState<List<AreaList>>> = _areaListState.asStateFlow()

    private val _ingredientsListState =
        MutableStateFlow<UiState<List<IngredientList>>>(UiState.Loading)
    val ingredientsListState: StateFlow<UiState<List<IngredientList>>> =
        _ingredientsListState.asStateFlow()

    private val _popularIngredientsListState =
        MutableStateFlow<UiState<List<IngredientList>>>(UiState.Loading)
    val popularIngredientsListState: StateFlow<UiState<List<IngredientList>>> =
        _popularIngredientsListState.asStateFlow()

    private val _ingredientsScreenListState =
        MutableStateFlow<UiState<List<IngredientList>>>(UiState.Loading)
    val ingredientsScreenListState: StateFlow<UiState<List<IngredientList>>> =
        _ingredientsScreenListState.asStateFlow()

    private val _categoriesListState =
        MutableStateFlow<UiState<List<CategoryList>>>(UiState.Loading)
    val categoriesListState: StateFlow<UiState<List<CategoryList>>> =
        _categoriesListState.asStateFlow()

    private val _mealCategoriesState =
        MutableStateFlow<UiState<List<MealCategories>>>(UiState.Loading)
    val mealCategoriesState: StateFlow<UiState<List<MealCategories>>> =
        _mealCategoriesState.asStateFlow()

    private val _recipeDetailState = MutableStateFlow<UiState<List<Meal>>>(UiState.Loading)
    val recipeDetailState: StateFlow<UiState<List<Meal>>> = _recipeDetailState.asStateFlow()

    init {
        fetchDataBeforeStartUp()
    }

    fun fetchDataBeforeStartUp() {
        viewModelScope.launch {
            fetchRandomRecipeOfTheDay()
            fetchMealCategories()
            fetchAreaList()
            fetchIngredientsList()
            fetchPopularIngredientsList()
            fetchIngredientsScreenList()
            fetchCategoriesList()

            _isReady.value =
                !(_randomRecipeOfTheDayState.value is UiState.Loading && _areaListState.value is UiState.Loading && _ingredientsListState.value is UiState.Loading && _categoriesListState.value is UiState.Loading && _mealCategoriesState.value is UiState.Loading)
        }
    }

    fun fetchRandomRecipeOfTheDay() {
        viewModelScope.launch {
            try {
                val response = mealDBRepository.getRandomRecipe()
                _randomRecipeOfTheDayState.value = UiState.Success(response.meals)
            } catch (e: Exception) {
                _randomRecipeOfTheDayState.value =
                    UiState.Error("Failed to fetch random recipe: ${e.message}")
            }
        }
    }

    fun fetchAreaList() {
        viewModelScope.launch {
            try {
                val response = mealDBRepository.getAreasList()
                _areaListState.value = UiState.Success(response.meals)
            } catch (e: Exception) {
                _areaListState.value = UiState.Error("Failed to fetch Area List: ${e.message}")
            }
        }
    }

    fun fetchIngredientsList() {
        viewModelScope.launch {
            try {
                val response = mealDBRepository.getIngredientsList()
                _ingredientsListState.value = UiState.Success(response.meals)
            } catch (e: Exception) {
                _ingredientsListState.value =
                    UiState.Error("Failed to fetch Ingredients List: ${e.message}")
            }
        }
    }

    fun fetchPopularIngredientsList() {
        viewModelScope.launch {
            try {
                val response = mealDBRepository.getIngredientsList()
                _popularIngredientsListState.value =
                    UiState.Success(response.meals.shuffled().take(20))
            } catch (e: Exception) {
                _popularIngredientsListState.value =
                    UiState.Error("Failed to fetch Popular Ingredients List: ${e.message}")
            }
        }
    }

    fun fetchIngredientsScreenList() {
        viewModelScope.launch {
            try {
                val response = mealDBRepository.getIngredientsList()
                val ingredientsScreenList = response.meals.sortedBy { it.strIngredient }
//                var ingredientsScreenList : MutableList<IngredientList> = mutableListOf()
//                for (i in response.meals){
//                    if(i.strDescription!=null && i.strDescription!=""){
//                        ingredientsScreenList.add(i)
//                    }
//                }
                _ingredientsScreenListState.value = UiState.Success(ingredientsScreenList)
            } catch (e: Exception) {
                _ingredientsScreenListState.value =
                    UiState.Error("Failed to fetch Ingredients List: ${e.message}")
            }
        }
    }

    fun fetchCategoriesList() {
        viewModelScope.launch {
            try {
                val response = mealDBRepository.getCategoriesList()
                _categoriesListState.value = UiState.Success(response.meals)
            } catch (e: Exception) {
                _categoriesListState.value =
                    UiState.Error("Failed to fetch Categories List: ${e.message}")
            }
        }
    }

    fun fetchMealCategories() {
        viewModelScope.launch {
            try {
                val response = mealDBRepository.getCategories()
                _mealCategoriesState.value = UiState.Success(response.categories)
            } catch (e: Exception) {
                _mealCategoriesState.value =
                    UiState.Error("Failed to fetch Meal Categories: ${e.message}")
            }
        }
    }

    fun fetchRecipeDetail(id: String) {
        viewModelScope.launch {
            try {
                val response = mealDBRepository.getMealById(id = id)
                _recipeDetailState.value = UiState.Success(response.meals)
            } catch (e: Exception) {
                _recipeDetailState.value =
                    UiState.Error("Failed to fetch Recipe Detail: ${e.message}")
            }
        }
    }

    fun setBottomNavSelectedItemIndex(index: Int) {
        _bottomNavSelectedItemIndex.value = index
    }

    fun fetchFlagUrl(areaName: String): String {
        return mealDBRepository.fetchFlagUrl(areaName)
    }

    fun fetchBigFlagUrl(areaName: String): String {
        return restCountriesRepository.fetchBigFlagUrl(areaName)
    }


    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    sealed class UiState<out T> {
        object Loading : UiState<Nothing>()
        data class Success<out T>(val data: T) : UiState<T>()
        data class Error(val message: String) : UiState<Nothing>()
    }
}