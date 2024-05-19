package br.com.fiap.challange.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.asLiveData

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MatchScreen(navController: NavHostController, userManager: UserManager) {
    val snackbarHostState = remember { SnackbarHostState() }
    var isClicked by remember { mutableStateOf(false) }
    var boxColor = Black
    var textColor = Black
    var isMentor = 0
    var isStudent = 0

    LaunchedEffect(key1 = userManager) {
        userManager.userIsMentorFlow.collect { isMentor = it }
        userManager.userIsStudentFlow.collect { isStudent = it }
    }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
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
                Column(
                    modifier = Modifier
                        .padding(
                            bottom = 24.dp,
                            top = 16.dp,
                            start = 24.dp,
                            end = 24.dp
                        )
                        .verticalScroll(rememberScrollState())
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            LogOutButton()

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(all = 4.dp)
                                    .width(180.dp)
                                    .border(
                                        2.dp,
                                        Color.Transparent,
                                        CircleShape
                                    )
                                    .background(Purple100, CircleShape)
                            ){
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .padding(all = 8.dp)
                                        .fillMaxWidth()
                                        .border(
                                            2.dp,
                                            Color.Transparent,
                                            CircleShape
                                        )
                                        .background(MainBlue, CircleShape)
                                ){
                                    Text(text = "Alunos", color = Color.White, modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                                }
                            }

//                            Box(
//                                contentAlignment = Alignment.Center,
//                                modifier = Modifier
//                                    .padding(all = 4.dp)
//                                    .border(
//                                        2.dp,
//                                        Color.Transparent,
//                                        CircleShape
//                                    )
//                                    .background(Purple100, CircleShape)
//                            ) {
//                                Row(
//                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
//                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
//                                ) {
//                                    Box(
//                                        contentAlignment = Alignment.Center,
//                                        modifier = Modifier
//                                            .border(
//                                                2.dp,
//                                                Color.Transparent,
//                                                CircleShape
//                                            )
//                                            .background(
//                                                if (isClicked) Color.Transparent else MainBlue,
//                                                CircleShape
//                                            )
//                                            .clip(CircleShape)
//                                            .clickable {
//                                                isClicked = !isClicked
//                                            }
//                                    ) {
//                                        Text(
//                                            text = "Alunos",
//                                            color = if (isClicked) Color.Black else Color.White, // Use a variável para a cor do texto
//                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//                                        )
//                                    }
//                                    Box(
//                                        contentAlignment = Alignment.Center,
//                                        modifier = Modifier
//                                            .border(
//                                                2.dp,
//                                                Color.Transparent,
//                                                CircleShape
//                                            )
//                                            .background(
//                                                if (isClicked) MainBlue else Color.Transparent,
//                                                CircleShape
//                                            )
//                                            .clip(CircleShape)
//                                            .clickable {
//                                                isClicked = !isClicked
//                                            }
//                                    ) {
//                                        Text(
//                                            text = "Mentores",
//                                            color = if (isClicked) Color.White else Color.Black, // Use a variável para a cor do texto
//                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//                                        )
//                                    }
//                                }
//                            }

                        }
                        Text(text = if(isMentor == 1) "Sou um mentor" else "sou um estudante")
                        Text(
                            text = "Mateus Santos",
                            fontSize = 40.sp,
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

                        ExperiencesOrInterests(
                            experienceorinterest = "Experiências",
                            experienceorknow = "experiência",
                            level = "Graduado",
                            describe = "Ao longo da minha jornada como mentor graduado em matemática, tive a oportunidade de guiar alunos desde os conceitos mais básicos até os desafios mais avançados, testemunhando suas jornadas de crescimento e superação. Cada experiência foi única e gratificante, reforçando minha convicção no poder da educação e no impacto transformador que um mentor pode ter na vida de seus alunos."
                        )

                        ExperiencesOrInterests(
                            experienceorinterest = "Interesses",
                            experienceorknow = "conhecimento",
                            level = "Básico",
                            describe = "Sei realizar operações aritméticas simples, como adição, subtração, multiplicação e divisão, além de resolver equações lineares básicas e entender conceitos fundamentais de frações e decimais. Também sei identificar e desenhar formas geométricas básicas, como triângulos, quadrados e círculos."
                        )

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

//                    Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun LikeButton() {
    Button(
        onClick = { /* ação do botão */ },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.button_like_icon),
            contentDescription = "Button Icon",
            modifier = Modifier.size(70.dp)
        )
    }
}

@Composable
fun RejectButton() {
    Button(
        onClick = { /* ação do botão */ },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.button_reject_icon),
            contentDescription = "Button Icon",
            modifier = Modifier.size(70.dp)
        )
    }
}
@Composable
fun LogOutButton() {
    Button(
        onClick = { /* ação do botão */ },
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White
        )
    ) {
        Image(
            painter = painterResource(id = R.drawable.log_out_button_icon),
            contentDescription = "Button Icon",
            modifier = Modifier.size(40.dp)
        )
    }
}