package com.example.sahara

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController


data class BottomNavigationItem(
    val title: String,
    val Icon: ImageVector
)

@Composable
fun MainScreen(){

    val items = listOf(
        BottomNavigationItem(
            title = "Food",
            Icon = Icons.Outlined.Fastfood
        ),
        BottomNavigationItem(
            title = "Environment",
            Icon = Icons.Outlined.Landscape
        ),
        BottomNavigationItem(
            title = "Health",
            Icon = Icons.Outlined.HealthAndSafety
        ),
        BottomNavigationItem(
            title = "Profile",
            Icon = Icons.Outlined.PersonOutline
        ),
    )
    var selItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val interactionSource = remember { MutableInteractionSource() }
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        bottomBar = {
            NavigationBar(
                contentColor = Color.Transparent
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 30.dp),
                    horizontalArrangement = Arrangement.Center) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        items.forEachIndexed { index, item ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .clickable(indication = null, interactionSource = interactionSource) {
                                        navController.navigate(item.title)
                                        selItemIndex = index
                                    }) {
                                val color =
                                    if (currentRoute == item.title)
                                        Color(0xFF2E80DA)
                                    else
                                        Color(0xffa8a8a8)
                                Icon(
                                    imageVector = item.Icon,
                                    item.title,
                                    tint = color,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.title,
                                    color = color,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp,
                                )
                            }
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "Food",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("Food") {
                Food()
            }
            composable("Environment") {
                Environment()
            }
            composable("Health") {

            }
            composable("Profile") {

            }
        }
    }
}