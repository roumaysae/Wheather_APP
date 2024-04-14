    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.layout.*
    import androidx.compose.foundation.text.BasicTextField
    import androidx.compose.material.*
    import androidx.compose.runtime.*
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.graphics.Color
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.input.TextFieldValue
    import androidx.compose.ui.tooling.preview.Preview
    import androidx.compose.ui.unit.dp
    import com.example.exercice1.R
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch
    import org.json.JSONObject
    import java.text.SimpleDateFormat
    import java.util.*

    class MainActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                WeatherApp()
            }
        }
    }

    @Composable
    fun WeatherApp() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "Weather App") },
                    backgroundColor = Color.Blue,
                    contentColor = Color.White,
                    elevation = AppBarDefaults.TopAppBarElevation
                )
            },
               content = {
                    WeatherContent()
                }
        )
    }

    @Composable
    fun WeatherContent() {
        var cityName by remember { mutableStateOf(TextFieldValue()) }
        var weatherData by remember { mutableStateOf<`MeteoItem`?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BasicTextField(
                value = cityName,
                onValueChange = { cityName = it },
                singleLine = true,
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = {
              //  fetchWeather(cityName.text)
            }) {
                Text("Search")
            }

            weatherData?.let { data ->
                Text("City: ${data.city}")
                Text("Temperature: ${data.temperature}°C")
                Text("Min Temperature: ${data.tempMin}°C")
                Text("Max Temperature: ${data.tempMax}°C")
                Text("Pressure: ${data.pressure} hPa")
                Text("Humidity: ${data.humidity}%")
                Text("Wind Speed: ${data.windSpeed} m/s")
                Image(
                    painter = painterResource(id = data.image),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }

    @Composable
    fun fetchWeather(city: String) {
        val scope = rememberCoroutineScope()
        val baseUrl = "http://api.openweathermap.org/data/2.5/weather?q="
        val apiKey = "YOUR_API_KEY"

      //  scope.launch(Dispatchers.IO) {
            val response = java.net.URL("$baseUrl$city&appid=$apiKey").readText()
            val weatherData = parseWeatherResponse(response)
            weatherData?.let {
           //     updateWeather(weatherData)
            }
        }

    @Composable
    fun WeatherContent(viewModel: WeatherViewModel) {
        var cityName by remember { mutableStateOf(TextFieldValue()) }
        var weatherData by remember { mutableStateOf<MeteoItem?>(null) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BasicTextField(
                value = cityName,
                onValueChange = { cityName = it },
                singleLine = true,
                textStyle = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = {
                viewModel.fetchWeather(cityName.text) { weather ->
                    weatherData = weather
                }
            }) {
                Text("Search")
            }

            weatherData?.let { data ->
                Text("City: ${data.city}")
                Text("Temperature: ${data.temperature}°C")
                Text("Min Temperature: ${data.tempMin}°C")
                Text("Max Temperature: ${data.tempMax}°C")
                Text("Pressure: ${data.pressure} hPa")
                Text("Humidity: ${data.humidity}%")
                Text("Wind Speed: ${data.windSpeed} m/s") // Display wind speed
                Image(
                    painter = painterResource(id = data.image),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            }
        }
    }

    fun parseWeatherResponse(response: String): `MeteoItem`? {
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

            return `MeteoItem`(cityName, temperature, tempMin, tempMax, pressure, humidity, windSpeed, image, dateString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @Composable
    fun updateWeather(weatherData: `MeteoItem`) {
        // Update UI with new weather data
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        WeatherApp()
    }