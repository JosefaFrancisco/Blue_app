package com.example.blue_app.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.blue_app.ArticleImage
import com.google.accompanist.pager.*
import kotlinx.coroutines.delay

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageCarousel(images: List<ArticleImage>) {
    val pagerState = rememberPagerState()

    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            val next = (pagerState.currentPage + 1) % images.size
            pagerState.animateScrollToPage(next)
        }
    }

    Column {
        HorizontalPager(
            count = images.size,
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) { page ->
            Card(
                shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                AsyncImage(
                    model = images[page].url,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .align(alignment = androidx.compose.ui.Alignment.CenterHorizontally)
                .border(
                    width = 1.dp,
                    color = Color(0xFFB2DFDB),
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            HorizontalPagerIndicator(
                pagerState = pagerState,
                activeColor = MaterialTheme.colorScheme.primary, // Rosa fuerte
                inactiveColor = MaterialTheme.colorScheme.surfaceVariant // Rosa claro
            )
        }
    }
}