package br.com.fiap.challange.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.fiap.challange.database.repository.NotificationRepository
import br.com.fiap.challange.model.Notification
import br.com.fiap.challange.model.NotificationWithUserNames
import br.com.fiap.challange.model.UserWithExperiencesAndInterests
import br.com.fiap.challange.ui.theme.Gray
import br.com.fiap.challange.ui.theme.LightBlue
import br.com.fiap.challange.ui.theme.MainBlue
import kotlinx.coroutines.launch

@Composable
fun NotificationsScreen(
    navController: NavHostController,
    user: UserWithExperiencesAndInterests?
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val notificationRepository = NotificationRepository(context)

    val notificationList = remember {
        mutableStateListOf<NotificationWithUserNames>()
    }

    LaunchedEffect(user) {
        if (user != null) {
            notificationList.addAll(notificationRepository.getNotificationsFromUserId(user.user.id))
        }
    }



    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        if (notificationList.isEmpty()) {
            Text(
                text = "Nenhuma notificação encontrada.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center,
                fontSize = 20.sp
            )
        } else {
            notificationList.forEach {
                val role = it.requestType
                val userWantTo = if (role == "mentor") "aprender" else "ensinar"
                val preposition = if (role == "aluno") "a" else "com"

                NotificationCard(
                    isNewMessage = it.hasSeen == 0,
                    frontIcon = it.frontIcon!!,
                    title = it.message,
                    text = "${it.fromUserName} gostaria de $userWantTo ${it.commomSubject} $preposition você.",
                    onClick = {
                        scope.launch {
                            notificationRepository.markNotificationAsSeen(it.id)
                            if (user != null) {
                                notificationList.clear()
                                notificationList.addAll(notificationRepository.getNotificationsFromUserId(user.user.id))
                            }
                        }
                    }
                )
            }
        }

    }
}

@Composable
fun NotificationCard(
    isNewMessage: Boolean = false,
    frontIcon: String,
    title: String,
    text: String,
    onClick: () -> Unit
) {
    Row(
        if (isNewMessage) {
            Modifier
                .drawBehind {
                    drawLine(
                        color = MainBlue,
                        start = Offset(0f, size.height),
                        end = Offset(0f, 0f),
                        strokeWidth = 16.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
        } else {
            Modifier
        }
    ) {
        Row(
            Modifier
                .drawBehind {
                    drawLine(
                        color = LightBlue,
                        start = Offset(0f, size.height),
                        end = Offset(size.width, size.height),
                        strokeWidth = 3.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
                .pointerInput(Unit) {
                    detectTapGestures {
                        onClick()
                    }
                }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(text = frontIcon, fontSize = 32.sp)

                Column {
                    Text(
                        text = title,
                        fontSize = 20.sp
                    )
                    Text(
                        text = text,
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        color = Gray
                    )
                }
            }
        }
    }
}