@file:OptIn(ExperimentalMaterial3Api::class)

package br.com.fiap.challange.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import br.com.fiap.challange.constants.InterestsLevelList
import br.com.fiap.challange.database.repository.InterestRepository
import br.com.fiap.challange.database.repository.UserRepository
import br.com.fiap.challange.model.Interest
import br.com.fiap.challange.model.User
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InterestRegisterScreen(navController: NavHostController, userId: String?) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val interestRepository = remember { InterestRepository(context) }

    var step = remember { mutableStateOf<Int>(1) }
    var hasInterest = remember { mutableStateOf<Boolean>(false) }
    var selectedInterests = remember { mutableStateListOf<String>() }
    val snackbarHostState = remember { SnackbarHostState() }
    var isLoading = remember { mutableStateOf<Boolean>(true) }

    LaunchedEffect(userId) {
        userId?.let {
            val interests = interestRepository.getInterestByUserId(it.toLong())
            hasInterest.value = interests.isNotEmpty()
            isLoading.value = false
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) {
        if (isLoading.value) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (userId !== null) {
            if (!hasInterest.value) {
                when (step.value) {
                    1 -> SelectInterestsScreen(onNext = { interests ->
                        if (interests.isNotEmpty()) {
                            selectedInterests.clear()
                            selectedInterests.addAll(interests)
                            step.value = 2
                        } else {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    "Preencha todos os campos corretamente",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    })

                    2 -> DescribeInterestsScreen(
                        userInterests = selectedInterests,
                        onNext = { interests, levels ->

                            scope.launch {
                                if (interests.values.all { it.isNotEmpty() } &&
                                    levels.values.all { it.isNotEmpty() }) {
                                    try {
                                        isLoading.value = true
                                        val saveJobs = selectedInterests.map { interest ->
                                            scope.async {
                                                interestRepository.save(
                                                    Interest(
                                                        interest = interest,
                                                        description = interests[interest].toString(),
                                                        level = InterestsLevelList.indexOf(levels[interest]) + 1,
                                                        userId = userId.toLong()
                                                    )
                                                )
                                            }
                                        }

                                        saveJobs.awaitAll()

                                        navController.navigate("experienceRegister/${userId}")
                                    } catch (err: Throwable) {
                                        isLoading.value = false
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                "Erro ao registrar seus interesses.",
                                                duration = SnackbarDuration.Short
                                            )
                                        }
                                    }
                                } else {
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                            "Preencha todos os campos corretamente",
                                            duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                            }
                        }
                    )
                }
            } else {
                navController.navigate("experienceRegister/${userId}")
            }
        }else{
            navController.navigate("login")
        }
    }
}