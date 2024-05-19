package br.com.fiap.challange.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import br.com.fiap.challange.constants.ExperiencesLevelList
import br.com.fiap.challange.constants.InterestsLevelList
import br.com.fiap.challange.database.repository.ExperienceRepository
import br.com.fiap.challange.database.repository.InterestRepository
import br.com.fiap.challange.model.Experience
import br.com.fiap.challange.model.Interest
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("CoroutineCreationDuringComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ExperienceRegisterScreen(navController: NavHostController, userId: String?) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val experienceRepository = remember { ExperienceRepository(context) }

    val step = remember { mutableStateOf(1) }
    val hasExperiences = remember { mutableStateOf(false) }
    val selectedExperiences = remember { mutableStateListOf<String>() }
    val snackbarHostState = remember { SnackbarHostState() }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(userId) {
        userId?.let {
            val experiences = experienceRepository.getExperiencesByUserId(it.toLong())
            hasExperiences.value = experiences.isNotEmpty()
            if(experiences.isEmpty()) isLoading.value = false
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
        if (userId !== null) {
            if (!hasExperiences.value) {
                when (step.value) {
                    1 -> SelectExperiencesScreen(onNext = { experiences ->
                        if (experiences.isNotEmpty()) {
                            selectedExperiences.clear()
                            selectedExperiences.addAll(experiences)
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

                    2 -> DescribeExperienceScreen(
                        userExperiences = selectedExperiences,
                        onNext = { experiences, levels ->

                            scope.launch {
                                if (experiences.values.all { it.isNotEmpty() } &&
                                    levels.values.all { it.isNotEmpty() }) {
                                    try {
                                        isLoading.value = true
                                        val saveJobs = selectedExperiences.map { experience ->
                                            scope.async {
                                                experienceRepository.save(
                                                    Experience(
                                                        experience = experience,
                                                        description = experiences[experience].toString(),
                                                        level = ExperiencesLevelList.indexOf(levels[experience]) + 1,
                                                        userId = userId.toLong()
                                                    )
                                                )
                                            }
                                        }

                                        saveJobs.awaitAll()

                                        navController.navigate("search")
                                    } catch (err: Throwable) {
                                        isLoading.value = false
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                "Erro ao registrar suas experiências.",
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
                navController.navigate("search")
            }
        }else{
            navController.navigate("login")
        }
    }
}