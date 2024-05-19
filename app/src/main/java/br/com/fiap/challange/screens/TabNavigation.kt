package br.com.fiap.challange.screens

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import br.com.fiap.challange.R
import br.com.fiap.challange.manager.UserManager
import br.com.fiap.challange.ui.theme.LightBlue
import br.com.fiap.challange.ui.theme.MainBlue
import br.com.fiap.challange.ui.theme.White
import br.com.fiap.challange.utils.generateNotificationMessage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun TabNavigationScreen(
    navController: NavHostController,
    sendNotification: (role: String, person: String, subject: String, message: String) -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var userManager: UserManager = UserManager(LocalContext.current)


    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            // No listOf abaixo adicionar as rotas que possuirão as tabs de navegação
            if (currentRoute in listOf("search", "profile", "match")) {
                val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

                LaunchedEffect(Unit) {
                    permissionState.launchPermissionRequest()
                }

                LaunchedEffect(Unit) {
                    while (true) {
                        delay(5000)
                        val role = "mentor"
                        val notificationTexts = generateNotificationMessage(role)
                        sendNotification(role, "Matheus", "programação", notificationTexts)
                    }
                }
                TabRow(
                    selectedTabIndex = currentTab,
                    contentColor = White,
                    containerColor = White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(White)
                        .drawBehind {
                            drawLine(
                                color = LightBlue,
                                start = Offset(0f, 0f),
                                end = Offset(size.width, 0f),
                                strokeWidth = 2.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        },
                    divider = {},
                    indicator = {}
                ) {
                    Tab(
                        selected = currentRoute == "search",
                        onClick = { navController.navigate("search"); currentTab = 0 },
                        text = {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(40.dp))
                                    .background(if (currentRoute == "search") MainBlue else White)
                                    .size(48.dp),
                            ) {
                                Image(
                                    painter = painterResource(if (currentRoute == "search") R.drawable.searchwhite else R.drawable.search),
                                    contentDescription = "search",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                    )
                    Tab(
                        selected = currentRoute == "match",
                        onClick = { navController.navigate("match"); currentTab = 0 },
                        text = {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(40.dp))
                                    .background(if (currentRoute == "match") MainBlue else White)
                                    .size(48.dp),
                            ) {
                                Image(
                                    painter = painterResource(if (currentRoute == "match") R.drawable.compasswhite else R.drawable.compass),
                                    contentDescription = "match",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        },
                    )
                    Tab(
                        selected = currentRoute == "tab3",
                        onClick = { navController.navigate("tab3"); currentTab = 2 },
                        text = {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(48.dp))
                                    .background(if (currentRoute == "tab3") MainBlue else White)
                                    .size(40.dp),

                                ) {
                                Image(
                                    painter = painterResource(if (currentRoute == "tab3") R.drawable.bellwhite else R.drawable.bell),
                                    contentDescription = "bell",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    )
                }
            }
        }
    ) {
        NavHost(navController, startDestination = "login") {
            composable("login") { LoginScreen(navController = navController, userManager) }
            composable("register") { RegisterScreen(navController = navController) }
            composable("search") { SearchScreen(navController = navController) }
            composable("interestRegister/{userId}") { backStackEntry ->
                InterestRegisterScreen(
                    navController = navController,
                    userId = backStackEntry.arguments?.getString("userId")
                )
            }
            composable("experienceRegister/{userId}") { backStackEntry ->
                ExperienceRegisterScreen(
                    navController = navController,
                    userId = backStackEntry.arguments?.getString("userId")
                )
            }
            composable("profile") { ProfileScreen(navController = navController) }
            composable("match") { MatchScreen(navController = navController, userManager = userManager)}
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
