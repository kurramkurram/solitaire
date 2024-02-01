package io.github.kurramkurram.solitaire.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.util.DIALOG_RESULT_APP_CONFIRM
import io.github.kurramkurram.solitaire.view.composable.component.DefaultText

/**
 * アプリケーションプライバシーポリシーダイアログ.
 */
class DialogAppFragment : DialogBaseFragment() {

    companion object {
        private const val ANNOTATED_TAG = "app"
        private const val ANNOTATED_ANNOTATION = "app"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        return ComposeView(requireContext()).apply {
            setContent {
                DialogContent()
            }
        }
    }

    @Composable
    fun DialogContent() {
        Column(modifier = Modifier.padding(20.dp)) {

            val spanStyle = SpanStyle(
                fontSize = dimensionResource(id = R.dimen.app_confirm_dialog_description_text_size).value.sp,
                color = colorResource(id = R.color.black),
            )
            val annotatedText = buildAnnotatedString {
                withStyle(spanStyle) {
                    append(
                        stringResource(
                            id = R.string.app_confirm_dialog_description1,
                            stringResource(id = R.string.app_name)
                        )
                    )
                }

                pushStringAnnotation(tag = ANNOTATED_TAG, annotation = ANNOTATED_ANNOTATION)
                withStyle(spanStyle.copy(textDecoration = TextDecoration.Underline)) {
                    append(stringResource(id = R.string.app_confirm_dialog_description2))
                }
                pop()
                withStyle(spanStyle) {
                    append(stringResource(id = R.string.app_confirm_dialog_description3))
                }
            }

            ClickableText(
                text = annotatedText,
                onClick = { offset ->
                    annotatedText.getStringAnnotations(
                        tag = ANNOTATED_TAG, start = offset,
                        end = offset
                    ).firstOrNull()?.let {
                        startActivity(Intent(requireContext(), AppActivity::class.java))
                    }
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onPositiveClick(DIALOG_RESULT_APP_CONFIRM) },
                horizontalAlignment = Alignment.CenterHorizontally, // 横方向
            ) {
                Box(modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp))
                DefaultText(
                    text = stringResource(id = R.string.app_confirm_dialog_agree_button),
                    fontSize = dimensionResource(id = R.dimen.app_confirm_dialog_agree_text_size).value.sp,
                    textColor = colorResource(id = R.color.black)
                )
                Box(modifier = Modifier.padding(0.dp, 10.dp, 0.dp, 0.dp))
            }
        }
    }

    @Preview
    @Composable
    fun PreviewDialogContent() {
        DialogContent()
    }
}
