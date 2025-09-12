package com.example.blue_app.screens

import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.blue_app.ApiClient
import com.example.blue_app.Article
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddArticlePage(
    onArticleAdded: () -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var selectedImages by remember { mutableStateOf<List<File>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    // Estados de error
    var nameError by remember { mutableStateOf(false) }
    var descriptionError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }
    var nameErrorMessage by remember { mutableStateOf("") }
    var descriptionErrorMessage by remember { mutableStateOf("") }
    var priceErrorMessage by remember { mutableStateOf("") }

    val context = LocalContext.current

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            selectedImages = uris.mapNotNull { uri ->
                try {
                    context.contentResolver.openInputStream(uri)?.use { inputStream ->
                        val file = File.createTempFile("image_${System.currentTimeMillis()}", ".jpg", context.cacheDir)
                        file.outputStream().use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                        file
                    }
                } catch (e: Exception) {
                    Toast.makeText(context, "Error al cargar imagen: ${e.message}", Toast.LENGTH_SHORT).show()
                    null
                }
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Título") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Rosa fuerte
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )

        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = {
                    name = it
                    nameError = false
                },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                isError = nameError,
                supportingText = {
                    if (nameError) {
                        Text(
                            text = nameErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                    descriptionError = false
                },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                isError = descriptionError,
                supportingText = {
                    if (descriptionError) {
                        Text(
                            text = descriptionErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            OutlinedTextField(
                value = price,
                onValueChange = {
                    price = it
                    priceError = false
                },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                isError = priceError,
                supportingText = {
                    if (priceError) {
                        Text(
                            text = priceErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            )

            Text("Imágenes:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            // Mostrar imágenes seleccionadas
            if (selectedImages.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(vertical = 8.dp)
                ) {
                    selectedImages.forEachIndexed { index, file ->
                        val bitmap = remember(file) { BitmapFactory.decodeFile(file.absolutePath) }
                        Box(modifier = Modifier.padding(end = 8.dp)) {
                            bitmap?.let {
                                Image(
                                    bitmap = it.asImageBitmap(),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                IconButton(
                                    onClick = {
                                        selectedImages = selectedImages.toMutableList().apply {
                                            removeAt(index)
                                        }
                                    },
                                    modifier = Modifier.align(Alignment.TopEnd)
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Eliminar",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { imagePicker.launch("image/*") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                enabled = !isLoading
            ) {
                Text("Seleccionar Imágenes")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    var hasError = false

                    if (name.isBlank()) {
                        nameError = true
                        nameErrorMessage = "El nombre es requerido"
                        hasError = true
                    }

                    if (description.isBlank()) {
                        descriptionError = true
                        descriptionErrorMessage = "La descripción es requerida"
                        hasError = true
                    }

                    if (price.isBlank()) {
                        priceError = true
                        priceErrorMessage = "El precio es requerido"
                        hasError = true
                    } else {
                        try {
                            val priceValue = price.toDouble()
                            if (priceValue <= 0) {
                                priceError = true
                                priceErrorMessage = "El precio debe ser mayor a 0"
                                hasError = true
                            }
                        } catch (e: NumberFormatException) {
                            priceError = true
                            priceErrorMessage = "El precio debe ser un número válido"
                            hasError = true
                        }
                    }

                    if (hasError) {
                        Toast.makeText(
                            context,
                            "Por favor complete todos los campos correctamente",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@Button
                    }

                    try {
                        isLoading = true
                        val priceValue = price.toDoubleOrNull() ?: run {
                            Toast.makeText(
                                context,
                                "El precio debe ser un número válido",
                                Toast.LENGTH_SHORT
                            ).show()
                            isLoading = false
                            return@Button
                        }

                        val namePart = name.toRequestBody("text/plain".toMediaTypeOrNull())
                        val descPart = description.toRequestBody("text/plain".toMediaTypeOrNull())
                        val pricePart = priceValue.toString().toRequestBody("text/plain".toMediaTypeOrNull())

                        val imageParts = selectedImages.map { file ->
                            MultipartBody.Part.createFormData(
                                "images[]",
                                file.name,
                                file.asRequestBody("image/*".toMediaTypeOrNull())
                            )
                        }

                        ApiClient.articleService.createArticle(
                            namePart,
                            descPart,
                            pricePart,
                            imageParts
                        ).enqueue(object : Callback<Article> {
                            override fun onResponse(call: Call<Article>, response: Response<Article>) {
                                isLoading = false
                                if (response.isSuccessful) {
                                    Toast.makeText(
                                        context,
                                        "Artículo creado exitosamente",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onArticleAdded()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Error al crear artículo: ${response.code()} - ${response.message()}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            override fun onFailure(call: Call<Article>, t: Throwable) {
                                isLoading = false
                                Toast.makeText(
                                    context,
                                    "Error de conexión: ${t.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        })
                    } catch (e: Exception) {
                        isLoading = false
                        Toast.makeText(
                            context,
                            "Error inesperado: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Guardar Artículo")
                }
            }
        }
    }
}