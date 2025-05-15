package com.pyrunner.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pyrunner.app.ui.theme.PyRunnerTheme
import com.pyrunner.app.ui.ScriptListScreen
import com.pyrunner.app.ui.EditorScreen
import com.pyrunner.app.ui.ConsoleScreen
import com.pyrunner.app.ui.SettingsScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PyRunnerTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "scriptList") {
        composable("scriptList") { ScriptListScreen(navController) }
        composable("editor/{scriptPath}") { backStackEntry ->
            EditorScreen(navController, backStackEntry.arguments?.getString("scriptPath"))
        }
        composable("console") { ConsoleScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
    }
}
