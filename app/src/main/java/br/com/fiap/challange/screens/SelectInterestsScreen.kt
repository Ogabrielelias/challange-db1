package br.com.fiap.challange.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.fiap.challange.Components.Input
import br.com.fiap.challange.R


@Composable
fun SelectInterestsScreen (navController: NavHostController) {
    InterestsScreen()
}

@Composable

fun InterestsScreen() {

    Column(
        modifier = Modifier
            .background(Color(0xFFFFFFFF))
            .fillMaxWidth(),
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

    Column (Modifier.padding(horizontal = 15.dp)){
        Spacer(modifier = Modifier.height(270.dp))
        Text(
            "Quais assuntos deseja aprender?",
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 5.dp)
            )
        
        Input(value = "", onChange = {}, placeholder = "Digite seus interesses")
        
        Spacer(modifier = Modifier.height(310.dp))
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
                .size(65.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff001CB0))
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
fun SelectInterestsPreview() {
    InterestsScreen()
}
