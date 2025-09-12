package com.example.blue_app.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: ImageVector,
    isPassword: Boolean = false,
    placeholder: String = "",
    isError: Boolean = false, // Nuevo parámetro para validación
    errorMessage: String = "", // Mensaje de error
    containerColor: Color = Color.White,
    shape: Shape = RoundedCornerShape(10.dp)
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = {
                Text(
                    text = label,
                    fontSize = 14.sp
                )
            },
            placeholder = {
                if (placeholder.isNotEmpty()) {
                    Text(text = placeholder, fontSize = 14.sp, color = Color.Gray)
                }
            },
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = label,
                    modifier = Modifier.size(24.dp)
                )
            },
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
            isError = isError,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor = containerColor,
                unfocusedBorderColor = if (isError) Color.Red else MaterialTheme.colorScheme.outline,
                focusedBorderColor = if (isError) Color.Red else MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = if (isError) Color.Red else MaterialTheme.colorScheme.primary,
                focusedLeadingIconColor = if (isError) Color.Red else MaterialTheme.colorScheme.primary,
                focusedTextColor = Color.Black
            ),
            shape = shape,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .defaultMinSize(minHeight = 64.dp) // clave: mantener altura mínima estable
        )
        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}