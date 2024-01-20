package io.github.kurramkurram.solitaire.view.composable.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import io.github.kurramkurram.solitaire.R

/**
 * Compose用の水平方向のディバイダー.
 */
@Composable
fun DividerHorizontal() {
    Divider(
        Modifier.padding(horizontal = 5.dp),
        thickness = 1.dp,
        color = colorResource(id = R.color.dark_green)
    )
}
