package com.example.blue_app.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp


enum class ButtonType { Filled, Outlined, Text }


@Composable
fun CustomButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    buttonType: ButtonType = ButtonType.Filled,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    shape: Shape = RoundedCornerShape(30.dp)
)
{
    Button(
        onClick = onClick,
        modifier = modifier
            .height(49.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        shape = shape
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = text)
        }
    }
}


