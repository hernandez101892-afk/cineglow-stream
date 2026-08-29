import re

with open('app/src/main/java/com/example/MainActivity.kt', 'r') as f:
    content = f.read()

# Add import
import_str = "import com.example.ui.series.MySeriesScreen\nimport androidx.navigation.NavType\nimport androidx.navigation.navArgument\nimport androidx.compose.material.icons.filled.List"
content = content.replace("import com.example.player.PlayerScreen", import_str + "\nimport com.example.player.PlayerScreen")

# Add Navigation Bar Item
nav_item = """        NavigationBarItem(
            icon = { Icon(Icons.Filled.List, contentDescription = "Mis Series") },
            label = { Text("Mis Series", style = MaterialTheme.typography.labelSmall) },
            selected = currentRoute == "myseries",
            onClick = { navController.navigate("myseries") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.primary,
                selectedTextColor = MaterialTheme.colorScheme.primary,
                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
            )
        )"""

content = content.replace("NavigationBarItem(\n            icon = { Icon(Icons.Filled.VideoLibrary", nav_item + "\n        NavigationBarItem(\n            icon = { Icon(Icons.Filled.VideoLibrary")

# Update routing
routing_replacement = """                        composable("myseries") { MySeriesScreen(navController) }
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
                        }"""

# Need to replace the old player route
content = re.sub(r'composable\("player"\) \{.*?\}', routing_replacement, content, flags=re.DOTALL)

with open('app/src/main/java/com/example/MainActivity.kt', 'w') as f:
    f.write(content)
