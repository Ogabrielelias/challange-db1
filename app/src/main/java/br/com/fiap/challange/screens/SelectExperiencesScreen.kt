package br.com.fiap.challange.screens

import Chip
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import br.com.fiap.challange.Components.Select
import br.com.fiap.challange.R
import br.com.fiap.challange.constants.interesstsList


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SelectExperiencesScreen(navController: NavController) {
    var experienceList = remember {
        mutableStateListOf<String>()
    }

    Box(
        Modifier
            .fillMaxSize()
            .zIndex(10f)
            .background(Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xff001CB0)),

                ) {
                Text(
                    text = "Avançar",
                    fontSize = 23.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .background(Color(0xFFFFFFFF))
            .padding(
                bottom = 24.dp,
                top = 40.dp,
                start = 24.dp,
                end = 24.dp
            )
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(120.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Image(
            painter = painterResource(R.drawable.educologo),
            contentDescription = null,
            modifier = Modifier
                .width(240.dp)
                .height(40.dp)
        )
    }

    Column(
        Modifier
            .padding(horizontal = 15.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ) {
        Row {

            Text(
                "Quais assuntos deseja aprender?",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Select(
            onSelect = { value ->
                if (!experienceList.contains(value)) {
                    experienceList.add(value)
                }
            },
            label = "Digite seus interesses",
            writableSelect = true,
            items = interesstsList
        )

        FlowRow(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            experienceList.forEachIndexed(action = { Index, Value ->
                Chip(
                    text = Value,
                    onDismiss = { experienceList.removeAt(Index) }
                )
            })
        }
    }
}

