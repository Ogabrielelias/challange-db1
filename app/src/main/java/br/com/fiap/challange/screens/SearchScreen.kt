package br.com.fiap.challange.screens

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import br.com.fiap.challange.components.Input
import br.com.fiap.challange.components.Select
import br.com.fiap.challange.R
import br.com.fiap.challange.constants.ExperiencesLevelList
import br.com.fiap.challange.constants.InterestsLevelList
import br.com.fiap.challange.constants.userRoleList
import br.com.fiap.challange.database.repository.UserRepository
import br.com.fiap.challange.model.UserWithExperiencesAndInterests
import br.com.fiap.challange.ui.theme.Black
import br.com.fiap.challange.ui.theme.Gray
import br.com.fiap.challange.ui.theme.LightBlue
import br.com.fiap.challange.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(navController: NavController) {

    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val usersList = remember { mutableStateListOf<UserWithExperiencesAndInterests>() }
    val searchValue = remember { mutableStateOf("") }
    val openFilter = remember { mutableStateOf(false) }
    val roleFilter = remember { mutableStateOf<String?>(null) }
    val experienceFilter = remember { mutableStateOf<Int?>(null) }
    val interestFilter = remember { mutableStateOf<Int?>(null) }


    if (openFilter.value) {
        FilterModal(
            role = roleFilter.value,
            exp = experienceFilter.value,
            int = interestFilter.value,
            onClose = { openFilter.value = false },
            onApplyFilter = { role, exp, int ->
                val expValue = if (exp == 0) null else exp
                val intValue = if (int == 0) null else int

                experienceFilter.value = expValue
                interestFilter.value = intValue
                roleFilter.value = if (role == "Ambos") null else role

                val isStudent: Int? = if (role == "Aluno") 1 else null
                val isMentor: Int? = if (role == "Mentor") 1 else null

                search(
                    context = context,
                    onSearch = { users ->
                        usersList.clear()
                        usersList.addAll(users.toSet())
                    },
                    searchTerm = searchValue.value.ifBlank { null },
                    isMentor = isMentor,
                    isStudent = isStudent,
                    formationLevel = expValue,
                    experienceLevel = intValue,
                    scope = scope
                )
            }
        )
    }
    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Column {
            Column(Modifier
                .drawBehind {
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
                        singleLine = true,
                        frontImage = R.drawable.search,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            val isStudent: Int? = if (roleFilter.value == "Aluno") 1 else null
                            val isMentor: Int? = if (roleFilter.value == "Mentor") 1 else null

                            search(
                                context = context,
                                onSearch = { users ->
                                    usersList.clear()
                                    usersList.addAll(users.toSet())
                                },
                                searchTerm = searchValue.value.ifBlank { null },
                                isMentor = isMentor,
                                isStudent = isStudent,
                                formationLevel = experienceFilter.value,
                                experienceLevel = interestFilter.value,
                                scope = scope
                            )
                        })
                    )
                    OutlinedButton(
                        onClick = { openFilter.value = true },
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
                        UserSearchCard(
                            name = user.user.name,
                            experiences = user.experiences.map { exp -> exp.experience },
                            interests = user.interests.map { int -> int.interest },
                            onNavigate = {navController.navigate("profile/${user.user.id}")}
                        )
                    }
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun UserSearchCard(
    name: String,
    experiences: List<String>? = null,
    interests: List<String>? = null,
    onNavigate:()->Unit
) {
    Column(Modifier.drawBehind {
        drawLine(
            color = LightBlue,
            start = Offset(0f, size.height),
            end = Offset(size.width, size.height),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
    }.pointerInput(Unit) {
        detectTapGestures {
            onNavigate()
        }
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
            if (experiences != null && experiences.isNotEmpty()) {
                Text(
                    "Experiências: ${experiences.joinToString(separator = ", ")}",
                    fontSize = 14.sp,
                    color = Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            if (interests != null && interests.isNotEmpty()) {
                Text(
                    "Interesses: ${interests.joinToString(separator = ", ")}",
                    fontSize = 14.sp,
                    color = Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun FilterModal(
    onApplyFilter: ((role: String, experience: Int, interest: Int) -> Unit)? = null,
    onClose: () -> Unit,
    role: String? = null,
    exp: Int? = null,
    int: Int? = null
) {
    var roleValue = remember { mutableStateOf(role ?: "Ambos") }
    var experienceValue = remember { mutableStateOf(exp ?: 0) }
    var interestValue = remember { mutableStateOf(int ?: 0) }

    var newExpList = (listOf("-") + ExperiencesLevelList)
    var newIntList = (listOf("-") + InterestsLevelList)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .zIndex(10f)
            .background(White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .drawBehind {
                            drawLine(
                                color = LightBlue,
                                start = Offset(0f, size.height),
                                end = Offset(size.width, size.height),
                                strokeWidth = 2.dp.toPx(),
                                cap = StrokeCap.Round
                            )
                        }) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Filtrar por:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        )

                        Image(
                            painter = painterResource(id = R.drawable.x),
                            contentDescription = "Close Button",
                            modifier = Modifier
                                .size(32.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        onClose()
                                    }
                                }
                        )
                    }
                }

                Column(
                    modifier = Modifier.padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Função:",
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp
                        )
                        Select(
                            value = roleValue.value,
                            items = userRoleList,
                            onSelect = { value -> roleValue.value = value })
                    }
                    Column {
                        Text(
                            text = "Nível de experiência:",
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp
                        )
                        Select(
                            value = newExpList[experienceValue.value],
                            items = newExpList,
                            onSelect = { value ->
                                experienceValue.value = newExpList.indexOf(value)
                            })
                    }
                    Column {
                        Text(
                            text = "Nível de conhecimento:",
                            fontWeight = FontWeight.Medium,
                            fontSize = 20.sp
                        )
                        Select(
                            value = newIntList[interestValue.value],
                            items = newIntList,
                            onSelect = { value ->
                                interestValue.value = newIntList.indexOf(value)
                            })
                    }
                }
            }

            Column {
                Button(
                    onClick = {
                        if (onApplyFilter != null) onApplyFilter(
                            roleValue.value,
                            experienceValue.value,
                            interestValue.value
                        )

                        onClose()
                    },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0XFF001CB0),
                        disabledContainerColor = Color(0xFF1F2E7E),
                        disabledContentColor = Color(0xFFC9C9C9)
                    )
                ) {
                    Text(
                        "Aplicar filtro",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(64.dp))
            }

        }
    }
}

fun search(
    context: Context,
    onSearch: (result: List<UserWithExperiencesAndInterests>) -> Unit,
    searchTerm: String? = null,
    isMentor: Int? = null,
    isStudent: Int? = null,
    formationLevel: Int? = null,
    experienceLevel: Int? = null,
    scope: CoroutineScope
) {
    val userRepository = UserRepository(context)
    scope.launch(Dispatchers.Main) {
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