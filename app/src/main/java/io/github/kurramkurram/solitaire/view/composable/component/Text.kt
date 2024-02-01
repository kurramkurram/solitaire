package io.github.kurramkurram.solitaire.view.composable.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

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
    textColor: Color = Color.White,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    Text(
        text = text,
        modifier = modifier,
        color = textColor,
        fontSize = fontSize,
        textAlign = textAlign
    )
}

@Preview
@Composable
fun PreviewDefaultText() {
    Column(modifier = Modifier.fillMaxSize()) {
        DefaultText(text = "text2", modifier = Modifier.padding(20.dp), textAlign = TextAlign.Start)
    }
}
