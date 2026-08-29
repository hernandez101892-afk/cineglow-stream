@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.example

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import coil.compose.AsyncImage
import com.example.ui.theme.CineGlowTheme

@androidx.annotation.OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun LibraryScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        CenterAlignedTopAppBar(
            title = { Text("BIBLIOTECA", style = MaterialTheme.typography.titleLarge) },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("MIS PELÍCULAS", color = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
            }
        }
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            items(4) { index ->
                val images = listOf(
                    "https://lh3.googleusercontent.com/aida-public/AB6AXuC6mij7gR_aR4UI-EUWU_vdK6tosh4CvuLxXBoG7G3OIn0feSdh1RGOnvdPnb8oUuiSTD4opTCVi63dxmGI9Or-PztIpTp9oeb0jAnU9AsMw3zK5d6twswyjfP1Nz--pEKAgG3WFPh6E2FjeTg3711QKcAksMrDpip5-VXmVK1W9hHi86Xi5dnZurmD5xkSGNOcqNAsVW1ohFAa1aqfzBRCG10vltNPeOKbPN9jno6jyyE3RpjKVK_d",
                    "https://lh3.googleusercontent.com/aida-public/AB6AXuAj1UQrqFLRpGGVdXwPaJ4Q_xzij5XI4VF6lbxZZJZTKzlQ6EAnVKD7Y9D8M-d7zaD4w1-o0ZcWCRtXW8K8dtc0ZpqfZa7Shzatvc4CrKoyqMhNZXMWc8BsWY0uLCfBGnE3kx_ymxwRWv9hJ9ZzF8KLEkEsiCyYy0byDprBf_Ma8ENzdzRz_5gBXL3uWsNSd4okDkS6WueU6818OYk-6kxljk1w8grxq86h19gp4W_9MI1ZEyzv8i5O",
                    "https://lh3.googleusercontent.com/aida-public/AB6AXuDRFqnsO_R2VqGWp3OvzKJmnvCtO-7uJ7eNZttrrSSRC5cmFmxFPj9vwTD80VeJnFM1gLCtRFJ4Qc7_uzcC5kvgng3_Hc-IZgPOS_2p3wViMqDmFHbzy8b9CA3mKOzVIpqqKJmRh4GQC4MQHBXoCoG9-mPGaCK_9JMg5IIlyy6nFr8QjffKnaClmRc7SRAyJooDbnh1q2RDN663ZiR5HqZnE6qAVT9wDvx6gOon_53wNLEOkNCg0Hub",
                    "https://lh3.googleusercontent.com/aida-public/AB6AXuDJPp0OqD0kSgB_HzT6WJNyr_BtZ7CYSgEsNrN1wXpHZAhbjviUrnkvgC-MHMEcBxt8XCImzYSCSQVYWrMGtMnrbV41dgmFgus1Z8pua6R3LV9AbYFHDo9PzGm9Yca1x6XHrJTtE6Qk_FB36nYZVMkxOd14wE94ZjSQ4Xi-T0Y5yWBMgnpHsbK9HFx5ATOoIJGHye0C8t5YXWyzcAc23_qJEVzGjM8eTeXzY5qR-ji0xohP-AIJk1gc"
                )
                val titles = listOf("The Crimson...", "Nexus Protocol", "Cyber Eden", "Mindhunter")
                val ratings = listOf("4.8", "4.5", "4.9", "4.7")
                
                Column(modifier = Modifier.clickable { navController.navigate("detail") }) {
                    Box(modifier = Modifier.fillMaxWidth().aspectRatio(2f/3f).clip(RoundedCornerShape(12.dp))) {
                        AsyncImage(model = images[index], contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                        Row(modifier = Modifier.align(Alignment.TopEnd).padding(8.dp).background(Color.Black.copy(alpha=0.6f), RoundedCornerShape(4.dp)).padding(horizontal = 4.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(14.dp))
                            Text(ratings[index], color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(titles[index], style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp))
                    Text("Sci-Fi • 2024", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

@Composable
fun DetailScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background).verticalScroll(rememberScrollState())) {
        Box(modifier = Modifier.fillMaxWidth().height(400.dp)) {
            AsyncImage(
                model = "https://lh3.googleusercontent.com/aida-public/AB6AXuC6mij7gR_aR4UI-EUWU_vdK6tosh4CvuLxXBoG7G3OIn0feSdh1RGOnvdPnb8oUuiSTD4opTCVi63dxmGI9Or-PztIpTp9oeb0jAnU9AsMw3zK5d6twswyjfP1Nz--pEKAgG3WFPh6E2FjeTg3711QKcAksMrDpip5-VXmVK1W9hHi86Xi5dnZurmD5xkSGNOcqNAsVW1ohFAa1aqfzBRCG10vltNPeOKbPN9jno6jyyE3RpjKVK_d",
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, MaterialTheme.colorScheme.background))))
            
            IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.TopStart).padding(16.dp)) {
                Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
            
            Column(modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)) {
                Text("THE CRIMSON GATE", style = MaterialTheme.typography.headlineLarge)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Filled.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    Text("4.8 • 2024 • TV-MA • 1h 58m", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { navController.navigate("player") },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Filled.PlayArrow, contentDescription = null)
                    Text("REPRODUCIR", fontWeight = FontWeight.Bold)
                }
                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("TRÁILER", fontWeight = FontWeight.Bold)
                }
                IconButton(onClick = { }, modifier = Modifier.background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)) {
                    Icon(Icons.Filled.Add, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "En las ruinas de una metrópolis gótica antaño gloriosa, un espadachín renegado debe proteger un misterioso portal carmesí que conecta su mundo en ruinas con un reino de horrores eldritch.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            Text("Episodios", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            
            // Episode item
            Row(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(12.dp)).clickable { navController.navigate("player") }.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = "https://lh3.googleusercontent.com/aida-public/AB6AXuBAu-IMcKeHK-cRFIvI8dIXZ3LPy8LTN5_xukurgD78WchRr1xymf06GZIAL0xlxyqSCCGdmh-LEO48CTFMfcPXh8TiCOUpm2RLjwTj6vAYRNsUH_XjJbNahBGqD4LbTl5YeDpNJR4jVEWhDl8uNcnZ5JtA08rXIoHMCvESV0PRXK0-v_brC62kZ8s7x492UJZ_xGBvUfljoKrem5eStFVO4T-_iBiOTpNxdrMNP_Gz5YQjqOoORSGO",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(120.dp, 80.dp).clip(RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("1. El Despertar Carmesí", style = MaterialTheme.typography.titleLarge.copy(fontSize = 16.sp))
                    Text("24m", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
