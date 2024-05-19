package br.com.fiap.challange.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun ProfileScreen(navController: NavHostController) {

    Column(
        modifier = Modifier
            .padding(
                bottom = 24.dp,
                top = 40.dp,
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

            Text(
                "Mateus Santos",
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Start
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
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}

@Composable
fun ExperiencesOrInterests(experienceorinterest: String, experienceorknow: String, level: String, describe: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    )
    {
        Text(
            "${experienceorinterest}:",
            fontSize = 30.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Start
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        )
        {
            Text(
                "Matemática:",
                fontSize = 20.sp,
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
}