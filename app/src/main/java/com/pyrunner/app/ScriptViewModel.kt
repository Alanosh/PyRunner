package com.pyrunner.app

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.security.crypto.EncryptedFile
import androidx.security.crypto.MasterKey
import com.chaquo.python.Python
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class ScriptViewModel : ViewModel() {
    private val _output = MutableStateFlow("")
    val output: StateFlow<String> = _output

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    fun runScript(scriptCode: String, args: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            _isRunning.value = true
            try {
                val py = Python.getInstance()
                val sys = py.getModule("sys")
                sys.put("argv", listOf("script.py") + args)
                val io = py.getModule("io")
                val stdout = io.callAttr("StringIO")
                sys.put("stdout", stdout)
                py.runCode(scriptCode)
                _output.value = stdout.callAttr("getvalue").toString()
            } catch (e: Exception) {
                _output.value = "Error: ${e.message}"
            } finally {
                _isRunning.value = false
            }
        }
    }

    fun installPythonPackage(packageName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val py = Python.getInstance()
                val subprocess = py.getModule("subprocess")
                subprocess.callAttr("run", listOf("pip", "install", packageName))
                _output.value = "Installed $packageName successfully"
            } catch (e: Exception) {
                _output.value = "Failed to install $packageName: ${e.message}"
            }
        }
    }

    fun saveEncryptedOutput(context: Context, output: String, fileName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val masterKey = MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build()
                val file = File(context.filesDir, "$fileName.enc")
                val encryptedFile = EncryptedFile.Builder(
                    context, file, masterKey, EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
                ).build()
                encryptedFile.openFileOutput().use { it.write(output.toByteArray()) }
                _output.value = "Output saved securely as $fileName.enc"
            } catch (e: Exception) {
                _output.value = "Error saving output: ${e.message}"
            }
        }
    }

    fun readEncryptedOutput(context: Context, fileName: String): String? {
        return try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            val file = File(context.filesDir, "$fileName.enc")
            val encryptedFile = EncryptedFile.Builder(
                context, file, masterKey, EncryptedFile.FileEncryptionScheme.AES256_GCM_HKDF_4KB
            ).build()
            encryptedFile.openFileInput().use { it.bufferedReader().readText() }
        } catch (e: Exception) {
            _output.value = "Error reading output: ${e.message}"
            null
        }
    }
}
