package com.example.stubs

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

object AppLogger {
    fun logWarning(message: String) {
        println("WARNING: $message")
    }
}

class AdBlocker {
    fun initialize() {}
}

class VpnMonitor(private val context: Context) {
    fun isVpnActive(): Boolean = false
}

class WebMessageBridge {
    fun setup() {}
}

class OptimizedExtractionWebView(private val context: Context) {
    fun loadUrl(url: String) {}
    fun stopLoading() {}
    fun destroy() {}
}

@Composable
fun ExtractionLoaderScreen(statusText: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = statusText)
    }
}

class AnimeMetadataRepository {
    suspend fun getMetadata(id: String): String = "{}"
}

data class VideoSource(val url: String, val headers: Map<String, String>)
