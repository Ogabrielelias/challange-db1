package br.com.fiap.challange.screens

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import br.com.fiap.challange.Components.Input
import br.com.fiap.challange.R
import br.com.fiap.challange.database.repository.UserRepository
import br.com.fiap.challange.model.User
import br.com.fiap.challange.model.UserWithExperiencesAndInterests
import br.com.fiap.challange.ui.theme.MainBlue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Column(
            modifier = Modifier
                .padding(
                    bottom = 24.dp,
                    top = 40.dp,
                    start = 24.dp,
                    end = 24.dp
                )
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
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        "Seja bem-vindo!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        "Faça login e comece sua jornada de aprendizado e orientação agora mesmo!",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )

                }
                FormLogin(onSend = { status, user ->
                    var message = "Login efetuado!"
                    if (!status) {
                        message = "E-mail e/ou Senha Inválido!"
                    } else {
                        if (user!!.interests.isEmpty() && user.user.isStudent == 1) {
                            navController.navigate("interestRegister/${user.user.id}")
                        } else if (user.experiences.isEmpty() && user.user.isMentor == 1) {
                            navController.navigate("experienceRegister/${user.user.id}")
                        } else {
                            navController.navigate("search")
                        }
                    }
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message,
                            duration = SnackbarDuration.Short
                        )
                    }
                })
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Não possui uma conta? ",
                    textAlign = TextAlign.Center
                )
                ClickableText(
                    text = AnnotatedString("Registre-se"),
                    onClick = { navController.navigate("register") },
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

fun validateLoginInputs(email: String, senha: String): Boolean {
    if (
        !TextUtils.isEmpty(email) &&
        Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
        senha.length > 0
    ) return true

    return false
}

@Composable
fun FormLogin(onSend: (status: Boolean, user: UserWithExperiencesAndInterests?) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val error = remember { mutableStateOf(false) }

    val buttonDisabled = remember { mutableStateOf(true) }

    val emailValue = remember {
        mutableStateOf("")
    }

    val senhaValue = remember {
        mutableStateOf("")
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Input(
                label = "Email",
                value = emailValue.value,
                onChange = { value ->
                    emailValue.value = value.replace("\n+".toRegex(), replacement = "")
                    buttonDisabled.value = !validateLoginInputs(
                        value,
                        senhaValue.value
                    )
                    error.value = false
                },
                frontImage = R.drawable.mail,
                singleLine = true,
                isError = error.value
            )

            Input(
                label = "Senha",
                value = senhaValue.value,
                onChange = { value ->
                    senhaValue.value = value.replace("\n+".toRegex(), replacement = "")
                    buttonDisabled.value = !validateLoginInputs(
                        emailValue.value,
                        value
                    )
                    error.value = false
                },
                type = "password",
                singleLine = true,
                frontImage = R.drawable.lock,
                isError = error.value
            )
        }

        Button(
            onClick = { ->
                try {
                    scope.launch {
                        val user: UserWithExperiencesAndInterests = login(
                            context = context,
                            email = emailValue.value,
                            senha = senhaValue.value
                        )

                        if (user == null) {
                            error.value = true
                        } else {
                            emailValue.value = ""
                            senhaValue.value = ""
                        }

                        onSend(user != null, user)
                    }
                } catch (err: Throwable) {
                    println(err)
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
                "Entrar",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

suspend fun login(
    context: Context,
    email: String,
    senha: String
): UserWithExperiencesAndInterests {
    val userRepository = UserRepository(context)
    val user = userRepository.getUserByLogin(
        email = email,
        senha = senha
    )

    return (user)
}