package com.example.cookbook.model.repository

import com.example.cookbook.model.dataclass.CountryFlagsList1

class RestCountriesRepository {
    fun fetchBigFlagUrl(areaName: String): String {
        val flagUrl = when (areaName) {
            "American" -> CountryFlagsList1.American.flagUrl
            "British" -> CountryFlagsList1.British.flagUrl
            "Canadian" -> CountryFlagsList1.Canadian.flagUrl
            "Chinese" -> CountryFlagsList1.Chinese.flagUrl
            "Croatian" -> CountryFlagsList1.Croatian.flagUrl
            "Dutch" -> CountryFlagsList1.Dutch.flagUrl
            "Egyptian" -> CountryFlagsList1.Egyptian.flagUrl
            "Filipino" -> CountryFlagsList1.Filipino.flagUrl
            "French" -> CountryFlagsList1.French.flagUrl
            "Greek" -> CountryFlagsList1.Greek.flagUrl
            "Indian" -> CountryFlagsList1.Indian.flagUrl
            "Irish" -> CountryFlagsList1.Irish.flagUrl
            "Italian" -> CountryFlagsList1.Italian.flagUrl
            "Jamaican" -> CountryFlagsList1.Jamaican.flagUrl
            "Japanese" -> CountryFlagsList1.Japanese.flagUrl
            "Kenyan" -> CountryFlagsList1.Kenyan.flagUrl
            "Malaysian" -> CountryFlagsList1.Malaysian.flagUrl
            "Mexican" -> CountryFlagsList1.Mexican.flagUrl
            "Moroccan" -> CountryFlagsList1.Moroccan.flagUrl
            "Polish" -> CountryFlagsList1.Polish.flagUrl
            "Portuguese" -> CountryFlagsList1.Portuguese.flagUrl
            "Russian" -> CountryFlagsList1.Russian.flagUrl
            "Spanish" -> CountryFlagsList1.Spanish.flagUrl
            "Thai" -> CountryFlagsList1.Thai.flagUrl
            "Tunisian" -> CountryFlagsList1.Tunisian.flagUrl
            "Turkish" -> CountryFlagsList1.Turkish.flagUrl
            "Ukrainian" -> CountryFlagsList1.Ukrainian.flagUrl
            "Vietnamese" -> CountryFlagsList1.Vietnamese.flagUrl
            else -> CountryFlagsList1.Unknown.flagUrl
        }
        return flagUrl
    }
}