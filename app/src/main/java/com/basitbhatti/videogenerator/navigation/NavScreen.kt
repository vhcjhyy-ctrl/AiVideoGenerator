package com.basitbhatti.videogenerator.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavScreen(val route: String, val label: String, val icon: ImageVector) {
    object Home : NavScreen("home", "الرئيسية", Icons.Default.Home)
    object Queue : NavScreen("queue", "سجل الفيديوهات", Icons.AutoMirrored.Default.List)
}
