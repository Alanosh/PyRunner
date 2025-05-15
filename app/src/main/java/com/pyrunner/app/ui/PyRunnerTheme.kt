package com.pyrunner.app.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun PyRunnerTheme(content: @Composable () -> Unit) {
    MaterialTheme {
        content()
    }
}
