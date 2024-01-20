package io.github.kurramkurram.solitaire.view.composable.component

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit

/**
 * Compose用のデフォルトのテキスト.
 *
 * @param text テキスト
 * @param modifier [Modifier]
 * @param fontSize フォントサイズ
 */
@Composable
fun DefaultText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    Text(text = text, modifier = modifier, color = Color.White, fontSize = fontSize)
}