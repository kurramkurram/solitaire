package io.github.kurramkurram.solitaire.view

import android.content.Context
import android.widget.Toast

/**
 * トーストを表示する.
 *
 * @param resId メッセージのresource id
 */
fun Context.showToast(resId: Int) = Toast.makeText(this, resId, Toast.LENGTH_SHORT).show()
