package com.wongislandd.infinityindex.infra.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wongislandd.infinityindex.infra.models.DefaultImageType


@Composable
fun UnknownImage(imageType: DefaultImageType, modifier: Modifier = Modifier) {
    val placeholderIcon = when (imageType) {
        DefaultImageType.PERSON -> Icons.Default.Person
        DefaultImageType.PLACE -> Icons.Default.Place
        DefaultImageType.BOOK -> Icons.Default.Menu
        DefaultImageType.THING -> Icons.Default.Favorite
    }
    Box(
        modifier = modifier
            .padding(16.dp)
    ) {
        Icon(
            imageVector = placeholderIcon,
            contentDescription = "Unknown image",
            tint = MaterialTheme.colors.onSurface,
            modifier = Modifier.size(100.dp).align(Alignment.Center)
        )
    }
}