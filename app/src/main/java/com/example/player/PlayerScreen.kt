package com.example.player

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.stubs.ExtractionLoaderScreen
import com.example.stubs.VideoSource
import com.example.stubs.VpnMonitor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class PlayerState {
    object Idle : PlayerState()
    data class Loading(val message: String) : PlayerState()
    data class Playing(val videoSource: VideoSource) : PlayerState()
    data class Error(val message: String) : PlayerState()
    data class VpnWarning(val message: String) : PlayerState()
}

class PlayerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<PlayerState>(PlayerState.Idle)
    val uiState: StateFlow<PlayerState> = _uiState

    private var currentEpisode = 1
    
    fun loadStream(context: android.content.Context, tmdbId: String, episode: Int) {
        currentEpisode = episode
        viewModelScope.launch {
            val vpnMonitor = VpnMonitor(context)
            val isTorrent = false 
            
            if (isTorrent && !vpnMonitor.isVpnActive()) {
                _uiState.value = PlayerState.VpnWarning("⚠️ Tu IP real está expuesta. Activa una VPN antes de reproducir este Torrent.")
                return@launch
            }

            _uiState.value = PlayerState.Loading("Conectando al servidor principal (UnlimPlay)...")
            
            val manager = HybridStreamingManager(context)
            val source = manager.extractStream(tmdbId, isMovie = false, season = 1, episode = episode)
            
            if (source != null) {
                _uiState.value = PlayerState.Playing(source)
            } else {
                _uiState.value = PlayerState.Loading("Servidor principal no disponible. Buscando en fuentes de respaldo...")
                kotlinx.coroutines.delay(2000)
                _uiState.value = PlayerState.Error("No se pudo encontrar el video en las fuentes de respaldo.")
            }
        }
    }

    fun nextEpisode(context: android.content.Context, tmdbId: String) {
        loadStream(context, tmdbId, currentEpisode + 1)
    }

    fun previousEpisode(context: android.content.Context, tmdbId: String) {
        if (currentEpisode > 1) {
            loadStream(context, tmdbId, currentEpisode - 1)
        }
    }
}

@Composable
fun PlayerScreen(tmdbId: String, initialEpisode: Int, onNavigateBack: () -> Unit, viewModel: PlayerViewModel = viewModel()) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var episode by rememberSaveable { mutableStateOf(initialEpisode) }
    
    LaunchedEffect(tmdbId, episode) {
        if (uiState is PlayerState.Idle || uiState is PlayerState.Playing) {
            viewModel.loadStream(context, tmdbId, episode)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        when (val state = uiState) {
            is PlayerState.Idle -> { }
            is PlayerState.Loading -> {
                ExtractionLoaderScreen(statusText = state.message)
            }
            is PlayerState.VpnWarning -> {
                Card(
                    modifier = Modifier.align(Alignment.Center).padding(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE65100))
                ) {
                    Text(
                        text = state.message,
                        color = Color.White,
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            is PlayerState.Error -> {
                Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.message, color = Color.Red, modifier = Modifier.padding(16.dp))
                    Button(onClick = onNavigateBack) {
                        Text("Volver")
                    }
                }
            }
            is PlayerState.Playing -> {
                ExoPlayerView(
                    videoSource = state.videoSource,
                    onNavigateBack = onNavigateBack,
                    onNext = { 
                        episode++
                        viewModel.nextEpisode(context, tmdbId) 
                    },
                    onPrevious = { 
                        if(episode > 1) {
                            episode--
                            viewModel.previousEpisode(context, tmdbId) 
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ExoPlayerView(videoSource: VideoSource, onNavigateBack: () -> Unit, onNext: () -> Unit, onPrevious: () -> Unit) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(true) }
    
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val dataSourceFactory = DefaultHttpDataSource.Factory().apply {
                setDefaultRequestProperties(videoSource.headers)
            }
            // Subtitles configuration
            val subtitleUri = Uri.parse("https://example.com/subtitles.vtt")
            val subtitleConfig = MediaItem.SubtitleConfiguration.Builder(subtitleUri)
                .setMimeType(androidx.media3.common.MimeTypes.TEXT_VTT)
                .setLanguage("es")
                .setSelectionFlags(androidx.media3.common.C.SELECTION_FLAG_DEFAULT)
                .build()
                
            val mediaItem = MediaItem.Builder()
                .setUri(Uri.parse(videoSource.url))
                .setSubtitleConfigurations(listOf(subtitleConfig))
                .build()
            setMediaItem(mediaItem)
            prepare()
            playWhenReady = true
        }
    }
    
    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = exoPlayer
                    useController = false
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // Gradient overlays
        Box(modifier = Modifier.fillMaxSize().background(androidx.compose.ui.graphics.Brush.verticalGradient(
            colors = listOf(Color.Black.copy(alpha = 0.7f), Color.Transparent, Color.Black.copy(alpha = 0.9f))
        )))
        
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 48.dp, start = 16.dp, end = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column {
                Text("NEON GENESIS: THE AWAKENING", style = MaterialTheme.typography.titleLarge, color = Color.White)
                Text("S1:E4 - \"City of Lights\"", style = MaterialTheme.typography.bodyMedium, color = Color.LightGray)
            }
            IconButton(onClick = onNavigateBack, modifier = Modifier.background(Color.DarkGray.copy(alpha=0.5f), CircleShape)) {
                Icon(Icons.Filled.Close, contentDescription = "Close", tint = Color.White)
            }
        }
        
        // Subtitles
        Text(
            text = "\"The mainframe is compromised. We need to disconnect now.\"",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontSize = 26.sp,
                shadow = androidx.compose.ui.graphics.Shadow(color = Color.Black, blurRadius = 8f)
            ),
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 120.dp).padding(horizontal = 32.dp)
        )
        
        // Center Controls
        Row(
            modifier = Modifier.align(Alignment.Center),
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { exoPlayer.seekBack() }, modifier = Modifier.size(64.dp).background(Color.DarkGray.copy(alpha=0.5f), CircleShape)) {
                Icon(Icons.Filled.Replay10, contentDescription = "Rewind", tint = Color.White, modifier = Modifier.size(36.dp))
            }
            IconButton(
                onClick = { 
                    if(isPlaying) exoPlayer.pause() else exoPlayer.play()
                    isPlaying = !isPlaying
                }, 
                modifier = Modifier.size(80.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
            ) {
                Icon(if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow, contentDescription = "Play/Pause", tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(48.dp))
            }
            IconButton(onClick = { exoPlayer.seekForward() }, modifier = Modifier.size(64.dp).background(Color.DarkGray.copy(alpha=0.5f), CircleShape)) {
                Icon(Icons.Filled.Forward10, contentDescription = "Forward", tint = Color.White, modifier = Modifier.size(36.dp))
            }
        }
        
        // Bottom controls
        Column(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(16.dp)) {
            // Progress Bar
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                Text("24:15", style = MaterialTheme.typography.labelSmall, color = Color.White)
                Slider(
                    value = 0.45f,
                    onValueChange = {},
                    modifier = Modifier.weight(1f).padding(horizontal = 8.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = Color.White,
                        activeTrackColor = MaterialTheme.colorScheme.primaryContainer,
                        inactiveTrackColor = Color.DarkGray
                    )
                )
                Text("42:30", style = MaterialTheme.typography.labelSmall, color = Color.White)
            }
            
            // Bottom actions
            Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = onNext,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Filled.SkipNext, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SIGUIENTE", color = Color.White, fontWeight = FontWeight.Bold)
                }
                
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Icon(Icons.Filled.Subtitles, contentDescription = "Subtitles", tint = Color.White, modifier = Modifier.size(28.dp))
                    Icon(Icons.Filled.VolumeUp, contentDescription = "Volume", tint = Color.White, modifier = Modifier.size(28.dp))
                    Icon(Icons.Filled.Settings, contentDescription = "Settings", tint = Color.White, modifier = Modifier.size(28.dp))
                }
            }
        }
    }
}
