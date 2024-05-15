package br.com.fiap.challange.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.challange.Components.Input
import br.com.fiap.challange.R
import br.com.fiap.challange.database.repository.UserRepository
import br.com.fiap.challange.model.User
import br.com.fiap.challange.model.UserWithExperiencesAndInterests
import br.com.fiap.challange.ui.theme.Black
import br.com.fiap.challange.ui.theme.Gray
import br.com.fiap.challange.ui.theme.LightBlue
import br.com.fiap.challange.ui.theme.White
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var usersList = remember { mutableStateListOf<UserWithExperiencesAndInterests>() }
    var searchValue = remember { mutableStateOf("") }


    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Column {
            Column(Modifier.drawBehind {
                drawLine(
                    color = LightBlue,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round
                )
            }) {
                Column(
                    modifier = Modifier
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Input(
                        value = searchValue.value,
                        onChange = { value -> searchValue.value = value },
                        label = "pesquisar",
                        frontImage = R.drawable.search,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            search(
                                context,
                                onSearch = { users ->
                                    usersList.clear()
                                    usersList.addAll(users)
                                },
                                searchTerm = searchValue.value
                            )
                        })
                    )
                    OutlinedButton(
                        onClick = { /*TODO*/ },
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Text("Exibir filtros", color = Black)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .verticalScroll(rememberScrollState())
            ) {
                if (usersList.isNotEmpty()) {
                    usersList.forEach { user ->
                        userSearchCard(
                            name = user.user.name,
                            experiences = user.experiences.map { exp -> exp.experience },
                            interests = user.interests.map { int -> int.interest }
                        )
                    }
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun userSearchCard(
    name: String,
    experiences: List<String>? = null,
    interests: List<String>? = null
) {
    Column(Modifier.drawBehind {
        drawLine(
            color = LightBlue,
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
    }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                name,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (experiences.isNullOrEmpty()) {
                Text(
                    "Experiências: ${experiences?.joinToString(separator = ", ")}",
                    fontSize = 14.sp,
                    color = Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (interests.isNullOrEmpty()) {
                Text(
                    "Interesses: ${interests?.joinToString(separator = ", ")}",
                    fontSize = 14.sp,
                    color = Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

fun search(
    context: Context,
    onSearch: (result: List<UserWithExperiencesAndInterests>) -> Unit,
    searchTerm: String? = null,
    isMentor: Boolean? = null,
    isStudent: Boolean? = null,
    formationLevel: Int? = null,
    experienceLevel: Int? = null
) {
    val userRepository = UserRepository(context)
    GlobalScope.launch(Dispatchers.Main) {
        val searchedUsers = userRepository.searchUsers(
            searchTerm,
            isMentor,
            isStudent,
            formationLevel,
            experienceLevel
        )
        onSearch(searchedUsers)
    }
}