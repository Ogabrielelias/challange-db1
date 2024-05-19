package br.com.fiap.challange.screens

import SharedViewModel
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
import br.com.fiap.challange.database.repository.NotificationRepository
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
    sendNotification: (title: String, text: String) -> Unit,
    sharedViewModel: SharedViewModel = viewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val loggedUser = sharedViewModel.user
    val context = LocalContext.current
    val notificationRepository = NotificationRepository(context)

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            // No listOf abaixo adicionar as rotas que possuirão as tabs de navegação
            if (currentRoute in listOf("search", "profile/{profileId}")) {
                val permissionState =
                    rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)

                LaunchedEffect(Unit) {
                    permissionState.launchPermissionRequest()
                }

                LaunchedEffect(Unit) {
                    while (true) {
                        delay(5000)
                        val lastNotification =
                            notificationRepository.getLastNewNotificationsFromUserId(loggedUser!!.user.id)

                        if (lastNotification !== null) {
                            val role = lastNotification.requestType
                            val userWantTo = if (role == "mentor") "aprender" else "ensinar"

                            sendNotification(
                                lastNotification.message,
                                "${lastNotification.fromUserName} gostaria de ${userWantTo} ${lastNotification.commomSubject} com você."
                            )

                            notificationRepository.markNotificationAsReceived(lastNotification.id)
                        }
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
                        selected = currentRoute == "tab2",
                        onClick = { navController.navigate("tab2"); currentTab = 1 },
                        text = {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .clip(shape = RoundedCornerShape(48.dp))
                                    .background(if (currentRoute == "tab2") MainBlue else White)
                                    .size(40.dp),

                                ) {
                                Image(
                                    painter = painterResource(if (currentRoute == "tab2") R.drawable.compasswhite else R.drawable.compass),
                                    contentDescription = "compass",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    )
                    Tab(
                        selected = currentRoute == "tab3",
                        onClick = { navController.navigate("tab3"); currentTab = 1 },
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
            composable("login") {
                LoginScreen(
                    navController = navController,
                    sharedViewModel = sharedViewModel
                )
            }
            composable("register") { RegisterScreen(navController = navController) }
            composable("search") { SearchScreen(navController = navController) }
            composable("interestRegister/{userId}") { backStackEntry ->
                InterestRegisterScreen(
                    navController = navController,
                    userId = backStackEntry.arguments?.getString("userId"),
                    user = loggedUser
                )
            }
            composable("experienceRegister/{userId}") { backStackEntry ->
                ExperienceRegisterScreen(
                    navController = navController,
                    userId = backStackEntry.arguments?.getString("userId"),
                    user = loggedUser
                )
            }
            composable("profile/{profileId}") { backStackEntry ->
                ProfileScreen(
                    navController = navController,
                    userId = backStackEntry.arguments?.getString("profileId")
                )
            }
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
