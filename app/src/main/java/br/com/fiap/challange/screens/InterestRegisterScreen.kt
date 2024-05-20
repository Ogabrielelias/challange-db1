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
import br.com.fiap.challange.database.repository.ExperienceRepository
import br.com.fiap.challange.database.repository.InterestRepository
import br.com.fiap.challange.database.repository.UserRepository
import br.com.fiap.challange.model.Interest
import br.com.fiap.challange.model.User
import br.com.fiap.challange.model.UserWithExperiencesAndInterests
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@ExperimentalMaterial3Api
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InterestRegisterScreen(navController: NavHostController, userId: String?, user: UserWithExperiencesAndInterests?) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val interestRepository = remember { InterestRepository(context) }

    val step = remember { mutableStateOf<Int>(1) }
    val hasInterest = remember { mutableStateOf<Boolean>(false) }
    val selectedInterests = remember { mutableStateListOf<String>() }
    val snackbarHostState = remember { SnackbarHostState() }
    val isLoading = remember { mutableStateOf<Boolean>(true) }
    val userValue = remember { mutableStateOf<UserWithExperiencesAndInterests?>(null) }

    LaunchedEffect(user) {
        user?.let {
            hasInterest.value = user.interests.isNotEmpty()

            if (user.interests.isEmpty()) isLoading.value = false
            userValue.value = user
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
        }
        if (userId !== null && user != null) {
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

                                        if (userValue.value != null) {
                                            val userExperiences = userValue.value!!.experiences

                                            if (userExperiences.isEmpty() && userValue.value!!.user.isMentor == 1) {
                                                navController.navigate("experienceRegister/${userId}")
                                            } else {
                                                navController.navigate("match")
                                            }
                                        }
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
                scope.launch {
                    if (userValue.value != null) {
                        val userExperiences = userValue.value!!.experiences

                        if (userExperiences.isEmpty() && userValue.value!!.user.isMentor == 1) {
                            navController.navigate("experienceRegister/${userId}")
                        } else {
                            navController.navigate("match")
                        }
                    }
                }
            }
        } else {
            navController.navigate("login")
        }
    }
}