package io.github.kurramkurram.solitaire.view

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import io.github.kurramkurram.solitaire.R
import io.github.kurramkurram.solitaire.databinding.ActivityNotificationBinding
import io.github.kurramkurram.solitaire.view.composable.component.DefaultText
import io.github.kurramkurram.solitaire.view.composable.component.DividerHorizontal
import io.github.kurramkurram.solitaire.view.composable.component.ListItem

/**
 * 通知を有効にする画面.
 */
class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.composeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            setContent {
                MaterialTheme {
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally, // 横方向
                        verticalArrangement = Arrangement.Top // 縦方向
                    ) {
                        DefaultText(
                            text = stringResource(id = R.string.notification_on_question),
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.start_other_app_activity_margin)),
                            textAlign = TextAlign.Center,
                            fontSize = dimensionResource(id = R.dimen.start_other_app_activity_question_text_size).value.sp
                        )

                        DividerHorizontal()

                        ListItem(
                            icon = Pair(
                                R.drawable.notifications,
                                stringResource(id = R.string.notification_on_title)
                            ),
                            title = stringResource(id = R.string.notification_on_title),
                            hasArrow = true,
                            onClick = { startActivity(intent) }
                        )

                        DividerHorizontal()

                        DefaultText(
                            text = stringResource(
                                id = R.string.notification_on_description,
                                stringResource(id = R.string.app_name),
                            ),
                            modifier = Modifier.padding(dimensionResource(id = R.dimen.start_other_app_activity_margin)),
                            fontSize = dimensionResource(id = R.dimen.start_other_app_activity_description_text_size).value.sp
                        )
                    }
                }
            }
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean =
        if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
}
