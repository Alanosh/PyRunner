package com.pyrunner.app.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pyrunner.app.ScriptViewModel

@Composable
fun SettingsScreen(navController: NavController) {
    val viewModel: ScriptViewModel = viewModel()
    var packageName by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = packageName,
            onValueChange = { packageName = it },
            label = { Text("Python Package Name") }
        )
        Button(
            onClick = {
                if (packageName.isNotBlank()) {
                    viewModel.installPythonPackage(packageName)
                    packageName = ""
                }
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Install Package")
        }
    }
}
