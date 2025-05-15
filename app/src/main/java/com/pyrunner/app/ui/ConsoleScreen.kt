package com.pyrunner.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pyrunner.app.ScriptViewModel

@Composable
fun ConsoleScreen(navController: NavController) {
    val viewModel: ScriptViewModel = viewModel()
    val output by viewModel.output.collectAsState()
    val isRunning by viewModel.isRunning.collectAsState()
    val context = LocalContext.current

    Column(modifier = Modifier.padding(16.dp)) {
        if (isRunning) {
            CircularProgressIndicator()
        }
        Text(
            text = output,
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        )
        Button(
            onClick = { viewModel.saveEncryptedOutput(context, output, "output_${System.currentTimeMillis()}") },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Save Output")
        }
    }
}
