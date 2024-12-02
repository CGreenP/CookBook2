package com.example.cookbook.model.retrofit

import com.example.cookbook.model.serviceapi.MealDBApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val MEALDB_BASE_URL = "https://www.themealdb.com/api/json/v1/1/"
    private const val RESTCOUNTRIES_BASE_URL = "https://restcountries.com/v3.1/"

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()
    }

    private val mealDBRetrofit: Retrofit by lazy {
        Retrofit.Builder().baseUrl(MEALDB_BASE_URL).client(okHttpClient) // Add OkHttp client
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    val mealDBApiService: MealDBApiService by lazy {
        mealDBRetrofit.create(MealDBApiService::class.java)
    }
}


//object RetrofitInstance {
//    private const val MEALDBBASE_URL = "https://www.themealdb.com/api/json/v1/1/"
//    private const val RESTCOUNTRIESBASE_URL = "https://restcountries.com/v3.1/"
//    private val mealDBRetrofit: Retrofit by lazy {
//        Retrofit.Builder().baseUrl(MEALDBBASE_URL).addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//    val getMealDBApiService: MealDBApiService by lazy {
//        mealDBRetrofit.create(MealDBApiService::class.java)
//    }
//    private val restCountriesRetrofit: Retrofit by lazy {
//        Retrofit.Builder().baseUrl(RESTCOUNTRIESBASE_URL).addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//    val getRestCountriesApiService: RESTCountriesApiService by lazy {
//        restCountriesRetrofit.create(RESTCountriesApiService::class.java)
//    }
//}