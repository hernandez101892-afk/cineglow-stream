@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.ui.series.MySeriesScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.material.icons.filled.List
import com.example.player.PlayerScreen
import com.example.ui.theme.CineGlowTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CineGlowTheme {
                val navController = rememberNavController()
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route

                Scaffold(
                    bottomBar = {
                        if (currentRoute != "player") {
                            BottomNavigationBar(navController, currentRoute)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") { HomeScreen(navController) }
                        composable("library") { LibraryScreen(navController) }
                        composable("detail") { DetailScreen(navController) }
                                                composable("myseries") { MySeriesScreen(navController) }
                        composable(
                            "player?tmdbId={tmdbId}&episode={episode}",
                            arguments = listOf(
                                navArgument("tmdbId") { defaultValue = "test" },
                                navArgument("episode") { 
                                    type = NavType.IntType
                                    defaultValue = 1 
                                }
                            )
                        ) { backStackEntry ->
                            val tmdbId = backStackEntry.arguments?.getString("tmdbId") ?: "test"
                            val episode = backStackEntry.arguments?.getInt("episode") ?: 1
                            PlayerScreen(
                                tmdbId = tmdbId,
                                initialEpisode = episode,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String?) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.9f),
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = "Inicio") },
            label = { Text("Inicio", style = MaterialTheme.typography.labelSmall) },
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
            label = { Text("Buscar", style = MaterialTheme.typography.labelSmall) },
            selected = currentRoute == "search",
            onClick = { },
        )
                NavigationBarItem(
            icon = { Icon(Icons.Filled.List, contentDescription = "Mis Series") },
            label = { Text("Mis Series", style = MaterialTheme.typography.labelSmall) },
            selected = currentRoute == "myseries",
            onClick = { navController.navigate("myseries") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.VideoLibrary, contentDescription = "Biblioteca") },
            label = { Text("Biblioteca", style = MaterialTheme.typography.labelSmall) },
            selected = currentRoute == "library",
            onClick = { navController.navigate("library") },
        )
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Hero section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .clickable { navController.navigate("detail") }
        ) {
            AsyncImage(
                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuAj1UQrqFLRpGGVdXwPaJ4Q_xzij5XI4VF6lbxZZJZTKzlQ6EAnVKD7Y9D8M-d7zaD4w1-o0ZcWCRtXW8K8dtc0ZpqfZa7Shzatvc4CrKoyqMhNZXMWc8BsWY0uLCfBGnE3kx_ymxwRWv9hJ9ZzF8KLEkEsiCyYy0byDprBf_Ma8ENzdzRz_5gBXL3uWsNSd4okDkS6WueU6818OYk-6kxljk1w8grxq86h19gp4W_9MI1ZEyzv8i5O",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background)
                        )
                    )
            )
            
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("ESTRENO EXCLUSIVO", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer).padding(4.dp))
                    Text("SCI-FI • 2024", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text("NEXUS\nPROTOCOL", style = MaterialTheme.typography.displayLarge, color = MaterialTheme.colorScheme.onSurface)
                Text("Cuando la última colonia humana descubre una señal alienígena...", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                
                Row(modifier = Modifier.padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Button(
                        onClick = { navController.navigate("player") },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = null)
                        Text("Reproducir", fontWeight = FontWeight.Bold)
                    }
                    IconButton(
                        onClick = {},
                        modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(12.dp))
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = null)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Continuar Viendo
        Text("Continuar Viendo", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(2) { index ->
                val images = listOf(
                    "https://lh3.googleusercontent.com/aida-public/AB6AXuDRFqnsO_R2VqGWp3OvzKJmnvCtO-7uJ7eNZttrrSSRC5cmFmxFPj9vwTD80VeJnFM1gLCtRFJ4Qc7_uzcC5kvgng3_Hc-IZgPOS_2p3wViMqDmFHbzy8b9CA3mKOzVIpqqKJmRh4GQC4MQHBXoCoG9-mPGaCK_9JMg5IIlyy6nFr8QjffKnaClmRc7SRAyJooDbnh1q2RDN663ZiR5HqZnE6qAVT9wDvx6gOon_53wNLEOkNCg0Hub",
                    "https://lh3.googleusercontent.com/aida-public/AB6AXuDJPp0OqD0kSgB_HzT6WJNyr_BtZ7CYSgEsNrN1wXpHZAhbjviUrnkvgC-MHMEcBxt8XCImzYSCSQVYWrMGtMnrbV41dgmFgus1Z8pua6R3LV9AbYFHDo9PzGm9Yca1x6XHrJTtE6Qk_FB36nYZVMkxOd14wE94ZjSQ4Xi-T0Y5yWBMgnpHsbK9HFx5ATOoIJGHye0C8t5YXWyzcAc23_qJEVzGjM8eTeXzY5qR-ji0xohP-AIJk1gc"
                )
                val titles = listOf("Cyber Eden", "Mindhunter V")
                Box(
                    modifier = Modifier
                        .width(260.dp)
                        .height(146.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { navController.navigate("player") }
                ) {
                    AsyncImage(model = images[index], contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                    Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f)))))
                    Column(modifier = Modifier.align(Alignment.BottomStart).padding(8.dp)) {
                        Text(titles[index], style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp))
                        Text("S1:E4", style = MaterialTheme.typography.labelSmall)
                        LinearProgressIndicator(progress = { 0.45f }, modifier = Modifier.fillMaxWidth().padding(top = 4.dp), color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}
