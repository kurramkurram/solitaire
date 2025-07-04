package io.github.kurramkurram.solitaire.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import android.webkit.URLUtil
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.unit.dp
import io.github.kurramkurram.solitaire.data.LibraryLicenseDto
import io.github.kurramkurram.solitaire.data.Licenses
import io.github.kurramkurram.solitaire.databinding.ActivityLicenseBinding
import io.github.kurramkurram.solitaire.view.composable.component.DefaultText
import io.github.kurramkurram.solitaire.view.composable.component.DividerHorizontal
import io.github.kurramkurram.solitaire.view.composable.component.LinkText

class LicenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLicenseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLicenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        disableEdgeToEdge(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.composeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )

            setContent {
                MaterialTheme {
                    LicenseScreen()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}

/**
 * ライセンス画面.
 *
 * @param modifier [Modifier]
 */
@Composable
fun LicenseScreen(
    modifier: Modifier = Modifier
) {
    var licenseState by remember { mutableStateOf(emptyList<LibraryLicenseDto>()) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        licenseState = Licenses.create(context)
    }

    LazyColumn(modifier = Modifier.padding(10.dp)) {
        items(licenseState) {
            Column(modifier.padding(horizontal = 5.dp)) {
                DefaultText(
                    modifier =
                        modifier.padding(
                            start = 0.dp,
                            top = 0.dp,
                            end = 0.dp,
                            bottom = 10.dp,
                        ),
                    text = it.name,
                )

                val terms = it.terms
                if (URLUtil.isValidUrl(terms)) {
                    LinkText(modifier, link = terms)
                } else {
                    DefaultText(terms)
                }
                DividerHorizontal()
            }
        }
    }
}