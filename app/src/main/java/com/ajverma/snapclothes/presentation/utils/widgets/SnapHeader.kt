package com.ajverma.snapclothes.presentation.utils.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ajverma.snapclothes.ui.theme.SnapClothesTheme

@Composable
fun SnapHeader(
    modifier: Modifier = Modifier,
    title: String,
    showFavourites: Boolean = false,
    onFavouritesClick: () -> Unit = {},
    showBackButton: Boolean = false,
    onBackClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
                clip = true
            )
            .height(60.dp)
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = 10.dp)

        ,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,

    ) {
        // Back Icon
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back",
            tint = if (showBackButton) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(30.dp)
                .clickable(enabled = showBackButton) { onBackClick() }
        )

        // Title
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )

        // Favourite Icon
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Favourites",
            tint = if (showFavourites) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(30.dp)
                .clickable(enabled = showFavourites) { onFavouritesClick() }
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun SnapHeaderPreview() {
    SnapClothesTheme {
        SnapHeader(
            title = "SnapClothes",
            showFavourites = true,
            showBackButton = true
        )
    }
}
