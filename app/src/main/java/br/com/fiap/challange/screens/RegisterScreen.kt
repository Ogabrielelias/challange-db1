package br.com.fiap.challange.screens

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController
import br.com.fiap.challange.R
import br.com.fiap.challange.database.repository.UserRepository
import br.com.fiap.challange.model.User
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun RegisterScreen (navController: NavController) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {

                FormRegister(onSend = { status ->
                    var message = "Conta Registrada!"
                    if(!status){
                        message = "E-mail já cadastrado!"
                    }
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message,
                            duration = SnackbarDuration.Short
                        )
                    }
                })
        }
    }
}

fun validateRegisterInputs (name:String, email:String, senha:String) : Boolean {
    if(
        name.length >= 3 &&
        !TextUtils.isEmpty(email) &&
        Patterns.EMAIL_ADDRESS.matcher(email).matches() &&
        senha.length >= 8
    ) return true

    return false
}

@Composable
fun FormRegister(onSend: (status:Boolean) -> Unit){
    val context = LocalContext.current
    val userRepository = UserRepository(context)

    var error = remember { mutableStateOf(false) }

    val buttonDisabled = remember { mutableStateOf(true) }

    var nameValue = remember {
        mutableStateOf("")
    }

    var emailValue = remember {
        mutableStateOf("")
    }

    var senhaValue = remember {
        mutableStateOf("")
    }

    Column (
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Input(
                label = "Nome",
                value = nameValue.value,
                onChange = { value ->
                    nameValue.value = value
                    error.value = false
                    buttonDisabled.value = !validateRegisterInputs(
                        value,
                        emailValue.value,
                        senhaValue.value
                    )
                },
                frontImage = R.drawable.user,
                isError = error.value
            )

            Input(
                label = "Email",
                value = emailValue.value,
                onChange = { value ->
                    emailValue.value = value
                    error.value = false
                    buttonDisabled.value = !validateRegisterInputs(
                        nameValue.value,
                        value,
                        senhaValue.value
                    )
                },
                frontImage = R.drawable.mail,
                isError = error.value
            )

            Input(
                label = "Senha",
                value = senhaValue.value,
                onChange = { value ->
                    senhaValue.value = value
                    error.value = false
                    buttonDisabled.value = !validateRegisterInputs(
                        nameValue.value,
                        emailValue.value,
                        value
                    )
                },
                type = "password",
                frontImage = R.drawable.lock,
                isError = error.value
            )
        }

        Button(
            enabled = !buttonDisabled.value,
            onClick = {
                try{
                    val usuario = User(
                        name = nameValue.value,
                        email = emailValue.value,
                        password = senhaValue.value
                    )

                    userRepository.save(usuario)

                    onSend(true)

                    nameValue.value = ""
                    emailValue.value = ""
                    senhaValue.value = ""

                }catch (err:Throwable) {
                    error.value = true
                    onSend(false)
                }
                      },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0XFF0D65FB),
                disabledContainerColor = Color(0XFF162A4D),
                disabledContentColor = Color.Gray
            )
        ) {
            Text(
                "Registrar  ➔",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
