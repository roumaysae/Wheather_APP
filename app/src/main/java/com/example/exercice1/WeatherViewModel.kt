import androidx.lifecycle.ViewModel
import com.example.exercice1.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class WeatherViewModel : ViewModel() {
    fun fetchWeather(city: String, onWeatherFetched: (`MeteoItem`) -> Unit) {
        val scope = CoroutineScope(Dispatchers.IO)
        val baseUrl = "http://api.openweathermap.org/data/2.5/weather?q="
        val apiKey = "YOUR_API_KEY"

        scope.launch {
            val response = java.net.URL("$baseUrl$city&appid=$apiKey").readText()
            val weatherData = parseWeatherResponse(response)
            weatherData?.let {
                onWeatherFetched(weatherData)
            }
        }
    }

    private fun parseWeatherResponse(response: String): `MeteoItem`? {
        try {
            val jsonObject = JSONObject(response)
            val cityName = jsonObject.getString("name")
            val main = jsonObject.getJSONObject("main")
            val temperature = (main.getDouble("temp") - 273.15).toInt()
            val tempMin = (main.getDouble("temp_min") - 273.15).toInt()
            val tempMax = (main.getDouble("temp_max") - 273.15).toInt()
            val pressure = main.getInt("pressure")
            val humidity = main.getInt("humidity")
            val wind = jsonObject.getJSONObject("wind")
            val windSpeed = wind.getDouble("speed")
            val weather = jsonObject.getJSONArray("weather").getJSONObject(0)
            val weatherCondition = weather.getString("main")
            val image = when (weatherCondition) {
                "Rain" -> R.drawable.rainy
                "Clear" -> R.drawable.clear
                "Thunderstorm" -> R.drawable.thunderstorm
                "Clouds" -> R.drawable.cloudy
                else -> R.drawable.clear // Default image
            }
            val date = Date(jsonObject.getLong("dt") * 1000)
            val dateString = SimpleDateFormat("dd-MMM-yyyy' T'HH:mm", Locale.getDefault()).format(date)

            return `MeteoItem` (cityName, temperature, tempMin, tempMax, pressure, humidity, windSpeed, image, dateString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}

data class `MeteoItem`(
    val city: String,
    val temperature: Int,
    val tempMin: Int,
    val tempMax: Int,
    val pressure: Int,
    val humidity: Int,
    val windSpeed: Double, // New field for wind speed
    val image: Int,
    val date: String
)
