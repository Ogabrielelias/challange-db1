package br.com.fiap.challange.screens

import SharedViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import br.com.fiap.challange.Components.Input
import br.com.fiap.challange.R
import br.com.fiap.challange.manager.UserManager
import br.com.fiap.challange.ui.theme.Black
import br.com.fiap.challange.ui.theme.Gray
import br.com.fiap.challange.ui.theme.LightBlue
import br.com.fiap.challange.ui.theme.MainBlue
import br.com.fiap.challange.ui.theme.Purple100
import androidx.compose.runtime.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import br.com.fiap.challange.constants.ExperiencesLevelList
import br.com.fiap.challange.constants.InterestsLevelList
import br.com.fiap.challange.database.repository.UserRepository
import br.com.fiap.challange.model.MatchUserStudent
import br.com.fiap.challange.model.UserWithExperiencesAndInterests
import br.com.fiap.challange.utils.logoutUser

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MatchScreen(
    navController: NavHostController,
    user: UserWithExperiencesAndInterests?,
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current
    val userRepository = UserRepository(context)
    val snackbarHostState = remember { SnackbarHostState() }
    var selected by remember { mutableStateOf("Alunos") }
    var userValue by remember { mutableStateOf<UserWithExperiencesAndInterests?>(null) }
    var matchQueue = remember { mutableStateListOf<MatchUserStudent>() }

    LaunchedEffect(user) {
        if (user != null) {
            userValue = user
            if (user.user.isStudent == 1) {
                selected = "Mentores"
            }
        } else {
            navController.navigate("login")
        }
    }

    LaunchedEffect(selected) {

        if (selected == "Alunos") {
            val usersToMatch = userRepository.getStudentsToMatchFromExperiences(
                userValue?.experiences!!.map { it.experience },
                userValue?.user!!.id
            )

            matchQueue.clear()

            usersToMatch.forEach { user ->
                userValue?.experiences
                    ?.map { it.experience }
                    ?.intersect(user.interests.map {
                        it.interest
                    }.toSet())?.forEach { subject ->
                        val userInterest = user.interests.find { it.interest == subject }

                        matchQueue.add(
                            MatchUserStudent(
                                id = user.user.id,
                                name = user.user.name,
                                subject = subject,
                                level = userInterest!!.level,
                                description = userInterest.description,
                                othersList = user.interests.map{it.interest}.filter{it != subject}
                            )
                        )
                    }
            }
        } else if (selected == "Mentores") {
            val usersToMatch = userRepository.getMentorToMatchFromInterests(
                userValue?.interests!!.map { it.interest },
                userValue?.user!!.id
            )

            matchQueue.clear()

            usersToMatch.forEach { user ->
                println(user)
                userValue?.interests
                    ?.map { it.interest }
                    ?.intersect(user.experiences.map {
                        it.experience
                    }.toSet())?.forEach { subject ->
                        val userInterest = user.experiences.find { it.experience == subject }

                        matchQueue.add(
                            MatchUserStudent(
                                id = user.user.id,
                                name = user.user.name,
                                subject = subject,
                                level = userInterest!!.level,
                                description = userInterest.description,
                                othersList = user.experiences.map{it.experience}.filter{it != subject}
                            )
                        )
                    }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .zIndex(10f)
            .padding(20.dp)
    ) {
        LogOutButton(
            navController = navController,
            sharedViewModel = sharedViewModel
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .zIndex(10f)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .padding(all = 4.dp)
                        .border(
                            2.dp,
                            Color.Transparent,
                            CircleShape
                        )
                        .width(220.dp)
                        .background(Purple100, CircleShape)
                ) {
                    if (userValue?.user?.isStudent == 1) {
                        if (selected == "Mentores") {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = if (user?.user?.isMentor == 1 && user.user.isStudent == 1) {
                                    Modifier
                                        .padding(all = 8.dp)
                                        .border(
                                            2.dp,
                                            Color.Transparent,
                                            CircleShape
                                        )
                                        .fillMaxWidth(0.5f)
                                        .background(MainBlue, CircleShape)
                                } else {
                                    Modifier
                                        .padding(all = 8.dp)
                                        .border(
                                            2.dp,
                                            Color.Transparent,
                                            CircleShape
                                        )
                                        .fillMaxWidth()
                                        .background(MainBlue, CircleShape)
                                }
                            ) {
                                Text(
                                    text = "Mentores",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                                )
                            }
                        } else if (user?.user?.isMentor == 1 && user.user.isStudent == 1) {
                            Box(
                                modifier = Modifier
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .fillMaxWidth(0.5f)
                                    .pointerInput(Unit) {
                                        detectTapGestures {
                                            selected = "Mentores"
                                        }
                                    }
                            )
                        }
                    }

                    if (userValue?.user?.isMentor == 1) {
                        if (selected == "Alunos") {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = if (user?.user?.isMentor == 1 && user.user.isStudent == 1) {
                                    Modifier
                                        .padding(all = 8.dp)
                                        .border(
                                            2.dp,
                                            Color.Transparent,
                                            CircleShape
                                        )
                                        .fillMaxWidth(0.9f)
                                        .background(MainBlue, CircleShape)
                                } else {
                                    Modifier
                                        .padding(all = 8.dp)
                                        .border(
                                            2.dp,
                                            Color.Transparent,
                                            CircleShape
                                        )
                                        .fillMaxWidth()
                                        .background(MainBlue, CircleShape)
                                }
                            ) {
                                Text(
                                    text = "Alunos",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
                                )
                            }
                        } else if (user?.user?.isMentor == 1 && user.user.isStudent == 1) {
                            Box(
                                modifier = Modifier
                                    .height(30.dp)
                                    .background(Color.Transparent)
                                    .fillMaxWidth(0.9f)
                                    .pointerInput(Unit) {
                                        detectTapGestures {
                                            selected = "Alunos"
                                        }
                                    }
                            )
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Column {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            bottom = 24.dp,
                            top = 80.dp,
                            start = 24.dp,
                            end = 24.dp
                        )
                        .verticalScroll(rememberScrollState())
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    if (matchQueue.isNotEmpty()) {
                        if (selected == "Alunos") {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                Text(
                                    text = matchQueue[0].name,
                                    fontSize = 40.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontWeight = FontWeight.ExtraBold,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier
                                        .drawBehind {
                                            drawLine(
                                                color = LightBlue,
                                                start = Offset(0f, size.height),
                                                end = Offset(size.width, size.height),
                                                strokeWidth = 2.dp.toPx(),
                                                cap = StrokeCap.Round
                                            )
                                        }
                                )
                                UserMatchText(
                                    subject = matchQueue[0].subject,
                                    experienceorknow = "conhecimento",
                                    level = InterestsLevelList[matchQueue[0].level - 1],
                                    describe = matchQueue[0].description
                                )
                                if(matchQueue[0].othersList.isNotEmpty()){
                                    Row {
                                        Text(
                                            text = "Outros conhecimentos:",
                                            fontSize = 16.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            fontWeight = FontWeight.ExtraBold,
                                        )
                                        Text(
                                            text = matchQueue[0].othersList.joinToString(separator = ", "),
                                            fontSize = 16.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            fontWeight = FontWeight.Normal,
                                        )
                                    }
                                }
                            }
                        } else if (selected == "Mentores") {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(24.dp)
                            ) {
                                Text(
                                    text = matchQueue[0].name,
                                    fontSize = 40.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    textAlign = TextAlign.Start,
                                    modifier = Modifier
                                        .drawBehind {
                                            drawLine(
                                                color = LightBlue,
                                                start = Offset(0f, size.height),
                                                end = Offset(size.width, size.height),
                                                strokeWidth = 2.dp.toPx(),
                                                cap = StrokeCap.Round
                                            )
                                        }
                                )
                                UserMatchText(
                                    subject = matchQueue[0].subject,
                                    experienceorknow = "experiência",
                                    level = ExperiencesLevelList[matchQueue[0].level - 1],
                                    describe = matchQueue[0].description
                                )

                                if(matchQueue[0].othersList.isNotEmpty()){
                                    Row {
                                        Text(
                                            text = "Outras experiências:",
                                            fontSize = 16.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            fontWeight = FontWeight.ExtraBold,
                                        )
                                        Text(
                                            text = matchQueue[0].othersList.joinToString(separator = ", "),
                                            fontSize = 16.sp,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            fontWeight = FontWeight.Normal,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .zIndex(11f),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RejectButton()
                LikeButton()
            }
            Spacer(modifier = Modifier.height(70.dp))
        }
    }
}


@Composable
fun LikeButton() {
    Button(
        onClick = { /* ação do botão */ },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.button_like_icon),
            contentDescription = "Button Icon",
            modifier = Modifier.size(56.dp)
        )
    }
}

@Composable
fun RejectButton() {
    Button(
        onClick = { /* ação do botão */ },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.button_reject_icon),
            contentDescription = "Button Icon",
            modifier = Modifier.size(56.dp)
        )
    }
}

@Composable
fun LogOutButton(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    Button(
        onClick = { logoutUser(navController = navController, sharedViewModel = sharedViewModel) },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.log_out_button_icon),
            contentDescription = "Button Icon",
            modifier = Modifier.size(32.dp)
        )
    }
}

@Composable
fun UserMatchText(
    experienceorknow: String,
    level: String,
    describe: String,
    subject: String
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    )
    {
        Text(
            "${subject}:",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Start
        )

        Row() {
            Text(
                "Nível de ${experienceorknow}:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start
            )
            Text(
                " ${level}",
                fontSize = 18.sp
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
        }
        Text(
            "${describe}",
            fontSize = 16.sp
        )
    }
}