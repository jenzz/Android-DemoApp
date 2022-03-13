package com.jenzz.demo.common.ui.widgets

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.jenzz.demo.common.ToastMessage
import com.jenzz.demo.common.ToastMessageId

@Composable
fun Toast(
    message: ToastMessage,
    onToastShown: (ToastMessageId) -> Unit,
) {
    val context = LocalContext.current
    Toast.makeText(context, message.text, Toast.LENGTH_SHORT).show()
    onToastShown(message.id)
}
