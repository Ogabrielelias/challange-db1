package br.com.fiap.challange.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.fiap.challange.Components.Input
import br.com.fiap.challange.R
import br.com.fiap.challange.ui.theme.MainBlue


@Composable
fun SelectTeachersScreen(navController: NavHostController) {
    TeachersScreen()
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TeachersScreen() {
    val texto = remember {
        mutableStateOf("")
    }
    val interestsList = remember {
        mutableListOf<String>()
    }

    Column(
        modifier = Modifier
            .background(Color(0xFFFFFFFF))
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(120.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Image(
            painter = painterResource(R.drawable.educologo),
            contentDescription = null,
            modifier = Modifier
                .size(240.dp)
        )
    }

    Column(
        Modifier
            .padding(horizontal = 15.dp),
    ) {
        Spacer(modifier = Modifier.height(270.dp))
        Row {

            Text(
                "Quais assuntos deseja ministrar aulas?",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }
        Input(
            value = texto.value, onChange = { value -> texto.value = value },
            placeholder = "Digite suas experiências.",
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardAction = KeyboardActions(
                onDone = {
                    interestsList.add(texto.value)
                    texto.value = ""
                }
            ),

            )

        FlowRow(
            modifier = Modifier.padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)

        ) {
            interestsList.forEachIndexed(action = { Index, Value ->
                InputChipExample(text = Value,
                    onDismiss = { interestsList.removeAt(Index) })

            })
        }
    }
    Column(
        Modifier
            .fillMaxSize()
            .padding(15.dp)
            .height(85.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .size(65.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff001CB0)),

            ) {
            Text(
                text = "Avançar",
                fontSize = 23.sp,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = 110.dp,
                        vertical = 10.dp
                    )
            )
        }
    }


}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun TeachersInterestsPreview() {
    TeachersScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputChipExample2(
    text: String,
    onDismiss: () -> Unit,

    ) {
    var enabled by remember { mutableStateOf(true) }
    if (!enabled) return

    InputChip(
        onClick = {
            onDismiss()
            enabled = !enabled
        },
        colors = InputChipDefaults.inputChipColors(selectedContainerColor = Color(0xFFCCD4FF)),


        label = { Text(text, color = MainBlue) },
        selected = enabled,
        trailingIcon = {
            Icon(
                Icons.Default.Close,
                contentDescription = "Localized description",
                Modifier.size(InputChipDefaults.AvatarSize)
            )
        }
    )
}
