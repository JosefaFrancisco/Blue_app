package com.example.blue_app.components

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp


@Composable
fun ClickableTextComponent(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Color(0xFF3061C3),
    textAlign: TextAlign = TextAlign.Start)
{
    Text(
        text = buildAnnotatedString {
            withStyle(style = SpanStyle(color = color)) {
                append(text)
            }
        },
        fontWeight = FontWeight.Bold,
        modifier = modifier.clickable(onClick = onClick),
        fontSize = 14.sp,
        textAlign = textAlign
    )
}


