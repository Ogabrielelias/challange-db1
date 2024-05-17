package br.com.fiap.challange.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController
import br.com.fiap.challange.Components.Input
import br.com.fiap.challange.Components.Select
import br.com.fiap.challange.R
import br.com.fiap.challange.constants.userRoleList
import br.com.fiap.challange.database.repository.InterestRepository
import br.com.fiap.challange.database.repository.UserRepository
import br.com.fiap.challange.model.Interest
import br.com.fiap.challange.model.User
import br.com.fiap.challange.ui.theme.MainBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        bottom = 24.dp,
                        top = 40.dp,
                        start = 24.dp,
                        end = 24.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.educologo),
                    contentDescription = null,
                    Modifier
                        .width(240.dp)
                        .height(40.dp)
                )

                Text(
                    "Registre-se!",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                )

                FormRegister(onSend = { message ->
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message,
                            duration = SnackbarDuration.Short
                        )
                    }
                })
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Já possui uma conta? ",
                        textAlign = TextAlign.Center
                    )
                    ClickableText(
                        text = AnnotatedString("Entrar"),
                        onClick = { navController.navigate("login") },
                        style = TextStyle(
                            color = MainBlue,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun FormRegister(onSend: (message: String) -> Unit) {
    val context = LocalContext.current
    val userRepository = UserRepository(context)
    val interestRepository = InterestRepository(context)

    var error = remember { mutableStateOf(false) }

    var nomeValue = remember {
        mutableStateOf("")
    }

    var idadeValue = remember {
        mutableStateOf("")
    }

    var generoValue = remember {
        mutableStateOf("")
    }

    var emailValue = remember {
        mutableStateOf("")
    }

    var senhaValue = remember {
        mutableStateOf("")
    }

    var confirmarSenhaValue = remember {
        mutableStateOf("")
    }

    var tipoValue = remember {
        mutableStateOf("")
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Input(
                label = "Nome",
                value = nomeValue.value,
                onChange = { value ->
                    nomeValue.value = value.replace("\n+".toRegex(), replacement = "")
                    error.value = false
                },
                isError = error.value,
                singleLine = true,
            )

            Input(
                label = "Idade",
                value = idadeValue.value,
                onChange = { value ->
                    idadeValue.value = value.replace("\n+".toRegex(), replacement = "")
                    error.value = false
                },
                isError = error.value,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Select(
                label = "Gênero",
                items = listOf("Masculino", "Feminino", "Outro"),
                onSelect = { value ->
                    generoValue.value = value
                    error.value = false
                },
            )

            Input(
                label = "Email",
                value = emailValue.value,
                onChange = { value ->
                    emailValue.value = value.replace("\n+".toRegex(), replacement = "")
                    error.value = false
                },
                singleLine = true,
                isError = error.value
            )

            Input(
                label = "Senha",
                value = senhaValue.value,
                onChange = { value ->
                    senhaValue.value = value
                    error.value = false
                },
                type = "password",
                singleLine = true,
                isError = error.value
            )

            Input(
                label = "Confirmar Senha",
                value = confirmarSenhaValue.value,
                onChange = { value ->
                    confirmarSenhaValue.value = value.replace("\n+".toRegex(), replacement = "")
                    error.value = false
                },
                type = "password",
                singleLine = true,
                isError = error.value
            )

            Select(
                label = "Deseja ser mentor e/ou aluno?",
                items = userRoleList,
                onSelect = { value ->
                    tipoValue.value = value
                    error.value = false
                }
            )
        }

        Button(
            onClick = { ->
                try {
                    var message = ""

                    if (senhaValue.value != confirmarSenhaValue.value) {
                        message = "Confirmação de senha inválida!"
                    } else if (
                        senhaValue.value.length < 3 ||
                        confirmarSenhaValue.value.length < 3 ||
                        nomeValue.value.length < 3 ||
                        idadeValue.value.toInt() < 14 ||
                        generoValue.value == "" ||
                        tipoValue.value == ""
                    ) {
                        message = "Preencha todos os campos corretamente!"
                    } else {
                        val user = userRepository.getUserByEmail(
                            email = emailValue.value
                        )

                        if (user == null) {
                            val isStudent =
                                if (generoValue.value == "Aluno" || generoValue.value == "Ambos") 1 else 0
                            val isMentor =
                                if (generoValue.value == "Mentor" || generoValue.value == "Ambos") 1 else 0

                            userRepository.save(
                                User(
                                    name = nomeValue.value,
                                    email = emailValue.value,
                                    password = senhaValue.value,
                                    age = idadeValue.value.toInt(),
                                    genre = generoValue.value,
                                    isStudent = isStudent,
                                    isMentor = isMentor
                                )
                            )

                            message = "Conta registrada!"

                            emailValue.value = ""
                            senhaValue.value = ""
                            confirmarSenhaValue.value = ""
                            nomeValue.value = ""
                            idadeValue.value = ""
                            generoValue.value = ""
                            tipoValue.value = ""
                        } else {
                            error.value = true
                            message = "E-mail já cadastrado!"
                        }
                    }

                    onSend(message)
                } catch (err: Throwable) {
                    println(err)
                    onSend("Erro de conexão com o servidor!")
                }
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0XFF001CB0),
                disabledContainerColor = Color(0xFF1F2E7E),
                disabledContentColor = Color(0xFFC9C9C9)
            )
        ) {
            Text(
                "Registrar",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
