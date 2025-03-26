package com.ajverma.snapclothes.presentation.utils.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ajverma.snapclothes.data.network.models.CategoryResponse
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import com.ajverma.snapclothes.R
import com.ajverma.snapclothes.presentation.utils.mapper.iconFromCategory
import com.ajverma.snapclothes.presentation.utils.mapper.nameFromCategory
import com.ajverma.snapclothes.ui.theme.SnapYellow

@Composable
fun SnapCategories(
    modifier: Modifier = Modifier,
    categories: List<String>,
    onClick: (String) -> Unit
) {
    LazyRow(
        modifier = modifier.padding(horizontal = 10.dp)
    ) {
        items(categories.size) {
            CategoryItem(
                categoryName = nameFromCategory(categories[it]),
                iconRes = iconFromCategory(categories[it]),
                modifier = Modifier.padding(8.dp),
                onClick = {
                    onClick(categories[it])
                }
            )
        }
    }
}

@Composable
fun CategoryItem(
    categoryName: String,
    iconRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .width(80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(SnapYellow.copy(alpha = 0.3f))
                .clickable {
                    onClick()

                },
            contentAlignment = Alignment.Center
        ) {
            // Category Icon
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = categoryName,
                modifier = Modifier
                    .size(30.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Category name
        Text(
            text = categoryName,
            fontSize = 14.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

