package io.github.kurramkurram.solitaire.view.composable.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.kurramkurram.solitaire.R

/**
 * Compose用のリストアイテム.
 *
 * @param icon iconのresource idとcontentDescription
 * @param title タイトル
 * @param description 説明
 * @param hasArrow 矢印の有無
 */
@Composable
fun ListItem(
    icon: Pair<Int, String>? = null,
    title: String,
    description: String? = null,
    hasArrow: Boolean = false,
    onClick: () -> Unit = {}
) {
    Box(modifier = Modifier.clickable { onClick() }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(dimensionResource(id = R.dimen.setting_item_height))
                .padding(vertical = dimensionResource(id = R.dimen.setting_item_margin)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                // アイコン
                icon?.let {
                    Image(
                        painter = painterResource(id = it.first),
                        modifier = Modifier.padding(start = dimensionResource(id = R.dimen.setting_item_margin)),
                        contentDescription = it.second,
                    )
                }

                // タイトル
                DefaultText(
                    text = title,
                    modifier = Modifier.padding(start = dimensionResource(id = R.dimen.setting_item_text_view_margin_start)),
                )
            }

            // 説明
            description?.let { DefaultText(text = description) }

            // 矢印
            if (hasArrow) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_forward),
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.setting_item_margin)),
                    contentDescription = stringResource(id = R.string.content_description_arrow)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewContent() {
    MaterialTheme {
        Column {
            ListItem(
                icon = Pair(R.drawable.ic_baseline_info, ""),
                title = stringResource(id = R.string.navigate_pro),
                hasArrow = true
            )
        }
    }
}
