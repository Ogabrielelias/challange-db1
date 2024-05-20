package br.com.fiap.challange.screens

import SharedViewModel
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.fiap.challange.R
import br.com.fiap.challange.ui.theme.LightBlue
import br.com.fiap.challange.ui.theme.MainBlue
import br.com.fiap.challange.ui.theme.Purple100
import androidx.compose.runtime.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.zIndex
import br.com.fiap.challange.constants.ExperiencesLevelList
import br.com.fiap.challange.constants.InterestsLevelList
import br.com.fiap.challange.constants.emojisList
import br.com.fiap.challange.database.repository.ExperienceRepository
import br.com.fiap.challange.database.repository.InterestRepository
import br.com.fiap.challange.database.repository.MatchRepository
import br.com.fiap.challange.database.repository.NotificationRepository
import br.com.fiap.challange.database.repository.UserRepository
import br.com.fiap.challange.model.Match
import br.com.fiap.challange.model.MatchUserStudent
import br.com.fiap.challange.model.Notification
import br.com.fiap.challange.model.UserWithExperiencesAndInterests
import br.com.fiap.challange.utils.generateNotificationMessage
import br.com.fiap.challange.utils.logoutUser
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MatchScreen(
    navController: NavHostController,
    user: UserWithExperiencesAndInterests?,
    sharedViewModel: SharedViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userRepository = UserRepository(context)
    val interestRepository = InterestRepository(context)
    val experienceRepository = ExperienceRepository(context)
    val matchRepository = MatchRepository(context)
    val notificationRepository = NotificationRepository(context)
    val snackbarHostState = remember { SnackbarHostState() }
    var selected by remember { mutableStateOf("Alunos") }
    var userValue by remember { mutableStateOf<UserWithExperiencesAndInterests?>(null) }
    var matchQueue = remember { mutableStateListOf<MatchUserStudent>() }
    val uniqueMatches = mutableSetOf<MatchUserStudent>()

    LaunchedEffect(Unit) {
        if (user != null) {
            userValue = user
            if (user.user.isStudent == 1) {
                selected = "Mentores"
            }
        } else {
            navController.navigate("login")
        }
    }

    LaunchedEffect(matchQueue) {
        if (selected == "Alunos") {
            val usersToMatch = userRepository.getStudentsToMatchFromExperiences(
                userValue?.experiences!!.map { it.experience },
                userValue?.user!!.id
            )

            usersToMatch.forEach { userMatch ->
                uniqueMatches.add(
                    MatchUserStudent(
                        id = userMatch.id,
                        name = userMatch.name,
                        subject = userMatch.userInterest,
                        level = userMatch.interestLevel,
                        description = userMatch.interestDescription,
                        othersList = interestRepository.getInterestByUserId(userMatch.id)
                            .map { it.interest }
                            .filter { it != userMatch.userInterest }
                    )
                )
            }

            matchQueue.clear()
            matchQueue.addAll(uniqueMatches.toSet())
            uniqueMatches.clear()

        } else if (selected == "Mentores") {
            val usersToMatch = userRepository.getMentorToMatchFromInterests(
                userValue?.interests!!.map { it.interest },
                userValue?.user!!.id
            )

            matchQueue.clear()

            usersToMatch.forEach { userMatch ->
                uniqueMatches.add(
                    MatchUserStudent(
                        id = userMatch.id,
                        name = userMatch.name,
                        subject = userMatch.userExperience,
                        level = userMatch.experienceLevel,
                        description = userMatch.experienceDescription,
                        othersList = experienceRepository.getExperiencesByUserId(userMatch.id)
                            .map { it.experience }
                            .filter { it != userMatch.userExperience }
                    )
                )
            }

            matchQueue.clear()
            matchQueue.addAll(uniqueMatches.toSet())
            uniqueMatches.clear()
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
                                if (matchQueue[0].othersList.isNotEmpty()) {
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

                                if (matchQueue[0].othersList.isNotEmpty()) {
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
                    } else {
                        Text(
                            text = "A procura de novos ${selected.lowercase()}.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
    if (matchQueue.isNotEmpty()) {
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
                    RejectButton(onClick = {
                        try {
                            scope.launch {
                                if (matchQueue.isNotEmpty()) {
                                    val currentMatch =
                                        (if (selected == "Mentores") matchQueue[0].id else userValue?.user?.id)?.let { mentorId ->
                                            (if (selected == "Mentores") userValue?.user?.id else matchQueue[0].id)?.let { studentId ->
                                                matchRepository.getMatchesByMentorStudentAndSubject(
                                                    matchSubject = matchQueue[0].subject,
                                                    mentorId = mentorId,
                                                    studentId = studentId
                                                )
                                            }
                                        }

                                    if (currentMatch != null && currentMatch.isNotEmpty()) {
                                        userValue?.user?.let {
                                            Match(
                                                id = currentMatch[0].id,
                                                userMentorId = if (selected == "Mentores") matchQueue[0].id else it.id,
                                                userStudentId = if (selected == "Mentores") it.id else matchQueue[0].id,
                                                studentHasMatch = if (selected == "Mentores") 0 else currentMatch[0].studentHasMatch,
                                                mentorHasMatch = if (selected == "Alunos") 0 else currentMatch[0].mentorHasMatch,
                                                matchSubject = matchQueue[0].subject
                                            )
                                        }?.let {
                                            matchRepository.update(it)
                                        }
                                    } else {
                                        (if (selected == "Mentores") matchQueue[0].id else userValue?.user?.id)?.let { mentorId ->
                                            (if (selected == "Mentores") userValue?.user?.id else matchQueue[0].id)?.let { studentId ->
                                                Match(
                                                    userMentorId = mentorId,
                                                    userStudentId = studentId,
                                                    studentHasMatch = if (selected == "Mentores") 0 else null,
                                                    mentorHasMatch = if (selected == "Alunos") 0 else null,
                                                    matchSubject = matchQueue[0].subject
                                                )
                                            }
                                        }?.let {
                                            matchRepository.save(it)
                                        }
                                    }

                                    matchQueue.removeAt(0)
                                }
                            }
                        } catch (err: Throwable) {
                            println(err)
                        }
                    })
                    LikeButton(onClick = {
                        try {
                            scope.launch {
                                if (matchQueue.isNotEmpty()) {
                                    val currentMatch =
                                        (if (selected == "Mentores") matchQueue[0].id else userValue?.user?.id)?.let { mentorId ->
                                            (if (selected == "Mentores") userValue?.user?.id else matchQueue[0].id)?.let { studentId ->
                                                matchRepository.getMatchesByMentorStudentAndSubject(
                                                    matchSubject = matchQueue[0].subject,
                                                    mentorId = mentorId,
                                                    studentId = studentId
                                                )
                                            }
                                        }

                                    if (currentMatch != null && currentMatch.isNotEmpty()) {
                                        userValue?.user?.let {
                                            val match = Match(
                                                id = currentMatch[0].id,
                                                userMentorId = if (selected == "Mentores") matchQueue[0].id else it.id,
                                                userStudentId = if (selected == "Mentores") it.id else matchQueue[0].id,
                                                studentHasMatch = if (selected == "Mentores") 1 else currentMatch[0].studentHasMatch,
                                                mentorHasMatch = if (selected == "Alunos") 1 else currentMatch[0].mentorHasMatch,
                                                matchSubject = matchQueue[0].subject
                                            )

                                            if (selected == "Mentores" && currentMatch[0].mentorHasMatch == 1) {
                                                notificationRepository.save(
                                                    Notification(
                                                        frontIcon = emojisList.random(),
                                                        hasSeen = 0,
                                                        message = generateNotificationMessage("aluno"),
                                                        commomSubject = matchQueue[0].subject,
                                                        fromUserId = userValue!!.user.id,
                                                        toUserId = currentMatch[0].id,
                                                        hasReceived = 0,
                                                        requestType = "mentor"
                                                    )
                                                )
                                            } else if (selected == "Alunos" && currentMatch[0].studentHasMatch == 1) {
                                                notificationRepository.save(
                                                    Notification(
                                                        frontIcon = emojisList.random(),
                                                        hasSeen = 0,
                                                        message = generateNotificationMessage("mentor"),
                                                        commomSubject = matchQueue[0].subject,
                                                        fromUserId = userValue!!.user.id,
                                                        toUserId = currentMatch[0].id,
                                                        hasReceived = 0,
                                                        requestType = "aluno"
                                                    )
                                                )
                                            }

                                            match
                                        }?.let {
                                            matchRepository.update(it)
                                        }
                                    } else {
                                        (if (selected == "Mentores") matchQueue[0].id else userValue?.user?.id)?.let { mentorId ->
                                            (if (selected == "Mentores") userValue?.user?.id else matchQueue[0].id)?.let { studentId ->
                                                Match(
                                                    userMentorId = mentorId,
                                                    userStudentId = studentId,
                                                    studentHasMatch = if (selected == "Mentores") 1 else null,
                                                    mentorHasMatch = if (selected == "Alunos") 1 else null,
                                                    matchSubject = matchQueue[0].subject
                                                )
                                            }
                                        }?.let {
                                            matchRepository.save(it)
                                        }
                                    }
                                    matchQueue.removeAt(0)
                                }
                            }
                        } catch (err: Throwable) {
                            println(err)
                        }
                    })
                }
                Spacer(modifier = Modifier.height(70.dp))
            }
        }
    }
}


@Composable
fun LikeButton(onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
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
fun RejectButton(onClick: () -> Unit) {
    Button(
        onClick = { onClick() },
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