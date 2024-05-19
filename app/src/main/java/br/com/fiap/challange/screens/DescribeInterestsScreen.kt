package br.com.fiap.challange.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.fiap.challange.Components.Input
import br.com.fiap.challange.Components.Select
import br.com.fiap.challange.R
import br.com.fiap.challange.constants.ExperiencesLevelList
import br.com.fiap.challange.constants.InterestsLevelList
import br.com.fiap.challange.constants.interesstsList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescribeInterestsScreen(
    userInterests: List<String>,
    onNext: (inputsValues: Map<String, String>, selectsValues: Map<String, String>) -> Unit
) {
    val interestsValue = remember { mutableStateMapOf<String, String>() }
    val interestsLevelValue = remember { mutableStateMapOf<String, String>() }

    LaunchedEffect(userInterests) {
        userInterests.forEach { interest ->
            interestsValue[interest] = ""
            interestsLevelValue[interest] = ""
        }
    }

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
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.educologo),
            contentDescription = null,
            Modifier
                .width(240.dp)
                .height(40.dp)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),

                ) {

                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    "Nos conte um pouco sobre suas experiências nestes assuntos e o que espera aprender com eles:",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start
                )
                userInterests.forEach { interest ->
                    DescribeInterests(
                        interest = interest,
                        InputValue = interestsValue[interest],
                        onChange = { value -> interestsValue[interest] = (value) },
                        onSelectLevel = {value -> interestsLevelValue[interest] = (value)}
                    )
                }

                Button(
                    onClick = { onNext(interestsValue, interestsLevelValue) },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0XFF001CB0),
                        disabledContainerColor = Color(0xFF1F2E7E),
                        disabledContentColor = Color(0xFFC9C9C9)
                    )
                ) {
                    Text(text = "Avançar")
                }
            }
        }
    }
}

@Composable
fun DescribeInterests(
    interest: String,
    InputValue: String? = "",
    onChange: (values: String) -> Unit,
    onSelectLevel:(values: String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),

        ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                "Qual o seu nível atual de conhecimento em ${interest}?",
                fontSize = 14.sp,
                textAlign = TextAlign.Start
            )

            Select(
                items = InterestsLevelList,
                onSelect = {
                    onSelectLevel(it)
                })
        }

        Column{
            Text(
                "${interest}:",
                fontSize = 14.sp,
                textAlign = TextAlign.Start
            )

            Input(
                value = InputValue ?: "", onChange = { value -> onChange(value) },
                label = "O que você já sabe deste conteúdo.."

            )
        }
    }
}