package com.example.laboratorioroom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.laboratorioroom.data.AppDatabase
import com.example.laboratorioroom.data.Tarea
import com.example.laboratorioroom.ui.theme.LaboratorioRoomTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tareas_db"
        ).build()

        setContent {
            LaboratorioRoomTheme {
                var texto by remember { mutableStateOf("") }
                var tareas by remember { mutableStateOf(listOf<Tarea>()) }

                // Cargar tareas al iniciar
                LaunchedEffect(Unit) {
                    tareas = db.tareaDao().obtenerTareas()
                }

                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                ) {
                    Text(
                        text = "Lista de Tareas",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = texto,
                        onValueChange = { texto = it },
                        label = { Text("Escribe una tarea") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            if (texto.isNotBlank()) {
                                val tarea = Tarea(descripcion = texto)
                                lifecycleScope.launch {
                                    db.tareaDao().insertar(tarea)
                                    tareas = db.tareaDao().obtenerTareas()
                                }
                                texto = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar")
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Tareas guardadas:", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    LazyColumn {
                        items(tareas) { tarea ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            ) {
                                Text(
                                    text = "• ${tarea.descripcion}",
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}