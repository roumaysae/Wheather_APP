package com.example.exercice1

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.material3.TextField // Import correct pour TextField depuis Material3

@Composable
fun MainScreen() {
    var query by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Weather App") },
                actions = {
                    IconButton(onClick = { /* Handle refresh */ }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Utiliser le TextField de Material3
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Enter city name") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    onImeActionPerformed = { action: ImeAction, softwareController: SoftwareKeyboardController ->
                        // Gérer l'action de recherche
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { /* Gérer la recherche */ }) {
                    Text(text = "Search")
                }
                Spacer(modifier = Modifier.height(16.dp))
                WeatherInfo()
            }
        }
    )
}

@Composable
fun WeatherInfo() {
    // Récupérer les informations météorologiques et mettre à jour l'interface en conséquence
    // Utiliser WeatherViewModel pour récupérer les données météorologiques

    // Exemple d'informations météorologiques
    val weather = remember { mutableStateOf("Sunny") }
    val temperature = remember { mutableStateOf(25) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Weather: ${weather.value}")
        Text(text = "Temperature: ${temperature.value}°C")
        // Ajouter plus d'informations météorologiques ici (ex. vitesse du vent, pression, etc.)
    }
}
