package com.pyrunner.app.ui

import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pyrunner.app.ScriptViewModel
import com.github.barteksc.codeview.CodeView
import java.io.File

@Composable
fun EditorScreen(navController: NavController, scriptPath: String?) {
    val context = LocalContext.current
    val viewModel: ScriptViewModel = viewModel()
    val filesDir = context.filesDir
    val scriptFile = scriptPath?.let { File(filesDir, it) } ?: File(filesDir, "sample_script.py")
    var scriptCode by remember { mutableStateOf(scriptFile.readText()) }
    var args by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        AndroidView(
            factory = { ctx ->
                CodeView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        400
                    )
                    setCode(scriptCode, "python")
                }
            },
            update = { view -> view.setCode(scriptCode, "python") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = args,
            onValueChange = { args = it },
            label = { Text("Command-line Arguments") },
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
        )
        Button(
            onClick = {
                scriptFile.writeText(scriptCode)
                viewModel.runScript(scriptCode, args.split(" ").filter { it.isNotBlank() })
                navController.navigate("console")
            },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Run Script")
        }
        Button(
            onClick = { scriptFile.writeText(scriptCode) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Save Script")
        }
    }
}
