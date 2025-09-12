package com.example.blue_app.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.blue_app.*
import com.example.blue_app.components.ClickableTextComponent
import com.example.blue_app.components.CustomButton
import com.example.blue_app.components.CustomTextField
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val emailError = remember { mutableStateOf(false) }
    val passwordError = remember { mutableStateOf(false) }
    val emailErrorMessage = remember { mutableStateOf("") }
    val passwordErrorMessage = remember { mutableStateOf("") }
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                color = colorScheme.surface.copy(alpha = 0.9f),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Iniciar Sesión",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            color = colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )

                    CustomTextField(
                        value = email.value,
                        onValueChange = {
                            email.value = it
                            emailError.value = false
                        },
                        label = "Email",
                        leadingIcon = Icons.Default.Email,
                        containerColor = colorScheme.surfaceVariant,
                        isError = emailError.value,
                        errorMessage = emailErrorMessage.value
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    CustomTextField(
                        value = password.value,
                        onValueChange = {
                            password.value = it
                            passwordError.value = false
                        },
                        label = "Contraseña",
                        leadingIcon = Icons.Default.Lock,
                        isPassword = true,
                        containerColor = colorScheme.surfaceVariant,
                        isError = passwordError.value,
                        errorMessage = passwordErrorMessage.value
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    CustomButton(
                        onClick = {
                            var hasError = false

                            if (email.value.isEmpty()) {
                                emailError.value = true
                                emailErrorMessage.value = "Email es requerido"
                                hasError = true
                            } else if (!Validators.isValidEmail(email.value)) {
                                emailError.value = true
                                emailErrorMessage.value = "Email no válido"
                                hasError = true
                            }

                            if (password.value.isEmpty()) {
                                passwordError.value = true
                                passwordErrorMessage.value = "Contraseña es requerida"
                                hasError = true
                            } else if (!Validators.isValidPassword(password.value)) {
                                passwordError.value = true
                                passwordErrorMessage.value = "La contraseña debe tener al menos 8 caracteres"
                                hasError = true
                            }

                            if (hasError) {
                                Toast.makeText(context, "Por favor complete todos los campos correctamente", Toast.LENGTH_SHORT).show()
                                return@CustomButton
                            }

                            val request = LoginRequest(
                                email = email.value,
                                password = password.value
                            )

                            ApiClient.authService.login(request)
                                .enqueue(object : Callback<AuthResponse> {
                                    override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                                        if (response.isSuccessful && response.body()?.success == true) {
                                            val token = response.body()?.data?.token
                                            val userId = response.body()?.data?.user?.id

                                            saveToken(context, token ?: "")
                                            saveUserId(context, userId ?: 0)

                                            onLoginClick()
                                        } else {
                                            Toast.makeText(context, "Credenciales inválidas", Toast.LENGTH_LONG).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                                        Toast.makeText(context, "Error de conexión: ${t.message}", Toast.LENGTH_LONG).show()
                                    }
                                })
                        },
                        text = "Iniciar sesión",
                        containerColor = colorScheme.primary,
                        contentColor = colorScheme.onPrimary,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ClickableTextComponent(
                text = "No tienes cuenta? Regístrate",
                onClick = onRegisterClick,
                color = colorScheme.onSurfaceVariant
            )
        }
    }
}