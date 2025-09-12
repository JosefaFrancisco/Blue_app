package com.example.blue_app.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.blue_app.ApiClient
import com.example.blue_app.Article
import com.example.blue_app.components.ImageCarousel
import com.example.blue_app.getUserId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController) {
    var articles by remember { mutableStateOf<List<Article>>(emptyList()) }
    var filteredArticles by remember { mutableStateOf<List<Article>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var articleToDelete by remember { mutableStateOf<Article?>(null) }
    val context = LocalContext.current

    // ID del usuario actual (se carga en LaunchedEffect)
    var currentUserId by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        // Cargar ID guardado (getUserId es tu función de SharedPreferences)
        try {
            currentUserId = getUserId(context)
        } catch (_: Exception) {
            currentUserId = 0
        }

        try {
            val response = withContext(Dispatchers.IO) {
                ApiClient.articleService.getArticles().execute()
            }
            if (response.isSuccessful) {
                val body = response.body()
                articles = body?.data ?: emptyList()
                filteredArticles = articles
            } else {
                hasError = true
            }
        } catch (e: Exception) {
            hasError = true
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        when {
            isLoading -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) { CircularProgressIndicator() }

            hasError -> Text("Error al cargar los artículos")
            filteredArticles.isEmpty() -> Text("No hay artículos disponibles")

            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredArticles, key = { it.id }) { article ->
                    // estado por ítem
                    var offsetX by remember { mutableStateOf(0f) }
                    var bgColor by remember { mutableStateOf(Color.Transparent) }

                    // es dueño?
                    val isOwner = article.user_id == currentUserId // Usar user_id en lugar de user.id


                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            // pointerInput depende de isOwner: si no es dueño no se registra el detector
                            .pointerInput(isOwner) {
                                if (!isOwner) return@pointerInput
                                detectHorizontalDragGestures(
                                    onDragEnd = {
                                        when {
                                            offsetX > 150f -> {
                                                // eliminar (solo dueño)
                                                articleToDelete = article
                                                showDeleteDialog = true
                                            }
                                            offsetX < -150f -> {
                                                // editar (solo dueño)
                                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                                    "article",
                                                    article
                                                )
                                                navController.navigate("edit")
                                            }
                                        }
                                        offsetX = 0f
                                        bgColor = Color.Transparent
                                    },
                                    onHorizontalDrag = { change, dragAmount ->
                                        change.consume()
                                        offsetX += dragAmount
                                        bgColor = when {
                                            offsetX > 50f -> Color(0x22FF0000) // Rojo para eliminar
                                            offsetX < -50f -> Color(0x2200FF00) // Verde para editar
                                            else -> Color.Transparent
                                        }
                                    }
                                )
                            }
                    ) {
                        // Si no es dueño, se evita que la tarjeta se desplace mostrando offset 0
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(bgColor)
                        )

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                // evitar aplicar offset si no es dueño (evita efectos raros)
                                .offset(x = (if (isOwner) offsetX else 0f).dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Column {
                                if (article.images.isNotEmpty()) {
                                    ImageCarousel(images = article.images)
                                }
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(article.name, style = MaterialTheme.typography.titleMedium)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        article.description,
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 2
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        "Precio: $${article.price}",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )

                                    // indicador de propiedad
                                    if (isOwner) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            "Tu artículo",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Dialogo de confirmación de eliminación (solo se abre cuando isOwner fue true al momento de crear articleToDelete)
        if (showDeleteDialog && articleToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false; articleToDelete = null },
                title = { Text("Confirmar eliminación") },
                text = { Text("¿Estás seguro de que deseas eliminar este artículo?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val article = articleToDelete!!
                            ApiClient.articleService.deleteArticle(article.id)
                                .enqueue(object : Callback<Void> {
                                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                        if (response.isSuccessful) {
                                            articles = articles.filter { it.id != article.id }
                                            filteredArticles = filteredArticles.filter { it.id != article.id }
                                            Toast.makeText(context, "Artículo eliminado", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Error al eliminar artículo", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<Void>, t: Throwable) {
                                        Toast.makeText(context, "Error de conexión: ${t.message}", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            showDeleteDialog = false
                            articleToDelete = null
                        }
                    ) {
                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false; articleToDelete = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
