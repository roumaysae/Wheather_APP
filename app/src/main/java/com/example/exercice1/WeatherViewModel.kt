package com.example.exercice1

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeatherViewModel : ViewModel() {
private val _weather = MutableLiveData<String>()
val weather: LiveData<String> = _weather

// Example LiveData for temperature
private val _temperature = MutableLiveData<Int>()
val temperature: LiveData<Int> = _temperature

// Example function to fetch weather data
fun fetchWeather(city: String) {
    // Implement logic to fetch weather data from API
    // Update LiveData with fetched data
    // Example:
    _weather.value = "Sunny"
    _temperature.value = 25
}
}