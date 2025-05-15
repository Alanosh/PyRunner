package com.pyrunner.app.ui

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import java.io.File

@Composable
fun ScriptListScreen(navController: NavController) {
    val context = LocalContext.current
    val filesDir = context.filesDir
    val scriptFiles = filesDir.listFiles { file -> file.extension == "py" }?.toList() ?: emptyList()

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        result.data?.data?.let { uri ->
            val inputStream = context.contentResolver.openInputStream(uri)
            val scriptContent = inputStream?.bufferedReader()?.readText() ?: ""
            inputStream?.close()
            val fileName = uri.path?.substringAfterLast("/") ?: "uploaded_script.py"
            File(filesDir, fileName).writeText(scriptContent)
            navController.navigate("editor/$fileName")
        }
    }

    LazyColumn {
        item {
            Button(onClick = {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "text/x-python"
                }
                launcher.launch(intent)
            }) {
                Text("Upload Script")
            }
        }
        items(scriptFiles) { file ->
            Button(onClick = { navController.navigate("editor/${file.name}") }) {
                Text(file.name)
            }
        }
    }
}
