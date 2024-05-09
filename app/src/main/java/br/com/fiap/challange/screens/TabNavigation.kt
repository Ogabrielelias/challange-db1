package br.com.fiap.challange.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import br.com.fiap.challange.ui.theme.Blue70
import br.com.fiap.challange.ui.theme.Gray50

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TabNavigationScreen(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            println(navController.currentDestination?.route)
            // No listOf abaixo adicionar as rotas que possuirão as tabs de navegação
            if (currentRoute in listOf("")) {
                TabRow(
                    selectedTabIndex = currentTab,
                    contentColor = Gray50,
                    containerColor = Blue70,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Blue70)
                        .padding(all = 16.dp),
                ) {
                    Tab(
                        selected = currentRoute == "",
                        onClick = { navController.navigate(""); currentTab = 0 },
                        text = {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Star",
                                tint = Color(0XFF0D65FB),
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    )
                    Tab(
                        selected = currentRoute == "",
                        onClick = { navController.navigate(""); currentTab = 1 },
                        text = {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Heart Icon",
                                tint = Color(0XFF0D65FB),
                                modifier = Modifier.size(25.dp)
                            )
                        }
                    )
                }
            }
        }
    ) {
        NavHost(navController, startDestination = "login") {
            composable("login") { LoginScreen(navController = navController) }
            composable("register") { RegisterScreen(navController = navController) }
        }
    }
}

private var currentTab: Int by mutableStateOf(0)


@Composable
fun TabContent(content: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text(text = content)
    }
}

@Preview
@Composable
fun PreviewTabContent() {
    TabContent("Tab Content")
}
