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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.fiap.challange.Components.Input
import br.com.fiap.challange.Components.Select
import br.com.fiap.challange.R
import br.com.fiap.challange.ui.theme.Gray
import br.com.fiap.challange.ui.theme.MainBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescribeExperienceScreen(navController: NavHostController) {
    var ExperienceValue = remember {
        mutableStateMapOf<String,String>("Matemática" to "","Ciência da Computação" to "","Física" to "")
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
                    "Nos conte um pouco sobre suas experiências nestes assuntos:",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Start
                )

                DescribeExperiences(experiences = "Matemática", InputValue = ExperienceValue["Matemática"], onChange = {value -> ExperienceValue["Matemática"] = (value) })
                DescribeExperiences(experiences = "Ciência da Computação", InputValue = ExperienceValue["Ciência da Computação"], onChange = {value -> ExperienceValue["Ciência da Computação"] = (value) })
                DescribeExperiences(experiences = "Física", InputValue = ExperienceValue["Física"], onChange = {value -> ExperienceValue["Física"] = (value) })


                Button(onClick = { /*TODO*/ },
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
fun DescribeExperiences(experiences: String, InputValue: String?="", onChange: (values: String)-> Unit) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),

        ) {
        Text(
            "Qual o seu nível de experiência em ${experiences}?",
            fontSize = 14.sp,
            textAlign = TextAlign.Start
        )

        Select(
            items = listOf("Autônomo", "Graduado","Pós-Graduado", "Mestrado", "Doutorado"),
            onSelect = {})

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                "${experiences}:",
                fontSize = 14.sp,
                textAlign = TextAlign.Start
            )

            Input(
                value = InputValue?:"", onChange = {value -> onChange(value) },
                label = "Descreva suas experiências.."

            )
        }
    }
}




