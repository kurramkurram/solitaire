package io.github.kurramkurram.solitaire.view.composable.component

import android.util.Log
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withLink

/**
 * 埋め込みリンク付きテキスト.
 *
 * @param modifier [Modifier]
 * @param preText リンク付きテキスト前の文言
 * @param linkText リンク付きテキストの文言
 * @param link リンク付きテキストに埋め込む文言
 * @param linkColor リンク付きテキストの色
 * @param onClickLink リンク付きテキストをクリックしたときの動作
 * @param suffixText リンク付きテキスト後の文言
 */
@Composable
fun LinkText(
    modifier: Modifier = Modifier,
    preText: String = "",
    linkText: String = "",
    link: String,
    linkColor: Color = Color.Blue,
    onClickLink: (() -> Unit)? = null,
    suffixText: String = "",
) {
    val uriHandler = LocalUriHandler.current
    Text(
        modifier = modifier,
        text = buildAnnotatedString {
            append(preText)
            val l =
                LinkAnnotation.Url(
                    link,
                    TextLinkStyles(
                        SpanStyle(
                            color = linkColor,
                            textDecoration = TextDecoration.Underline,
                        ),
                    ),
                ) {
                    if (onClickLink == null) {
                        try {
                            val url = (it as LinkAnnotation.Url).url
                            uriHandler.openUri(url)
                        } catch (e: IllegalArgumentException) {
                            Log.e("LinkText#LicenseScreenLinkText ", "$e")
                        }
                    } else {
                        onClickLink()
                    }
                }
            withLink(l) { append(linkText.ifEmpty { link }) }
            append(suffixText)
        },
    )
}

// @Composable
// @Preview
// fun PreviewAngerLogLinkText() {
//    AngerLogLinkText(
//        modifier = Modifier.padding(10.dp),
// //        preText = "「利用を開始する」を押下すると",
// //        linkText = "利用規約",
//        link = "https://example.com",
// //        postText = "に同意することになります。"
//    )
// }
