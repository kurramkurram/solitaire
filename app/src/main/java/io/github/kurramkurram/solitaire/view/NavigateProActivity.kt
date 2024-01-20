package io.github.kurramkurram.solitaire.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.kurramkurram.solitaire.BuildConfig
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.databinding.ActivityNavigateProBinding
import io.github.kurramkurram.solitaire.view.composable.component.DefaultText
import io.github.kurramkurram.solitaire.view.composable.component.DividerHorizontal
import io.github.kurramkurram.solitaire.view.composable.component.ListItem

/**
 * 有償版を案内する画面.
 */
class NavigateProActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNavigateProBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNavigateProBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.composeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            setContent {
                MaterialTheme {
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data =
                            Uri.parse("https://play.google.com/store/apps/details?id=io.github.kurramkurram.solitaire")
                    }
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally, // 横方向
                        verticalArrangement = Arrangement.Top // 縦方向
                    ) {
                        ListItem(
                            title = stringResource(id = R.string.navigate_pro),
                            hasArrow = true,
                            onClick = { startActivity(intent) }
                        )

                        DividerHorizontal()

                        DefaultText(
                            text = stringResource(id = R.string.navigate_pro_description),
                            modifier = Modifier.padding(20.dp),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}
