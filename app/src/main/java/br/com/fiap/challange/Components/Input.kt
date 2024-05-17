package br.com.fiap.challange.Components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.challange.R
import br.com.fiap.challange.ui.theme.Gray
import br.com.fiap.challange.ui.theme.MainBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Input(
    label: String? = null,
    placeholder: String = "",
    value: String,
    onChange: (value: String) -> Unit,
    type: String? = "",
    frontImage: Int? = null,
    isError: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
    keyboardActions: KeyboardActions = KeyboardActions()
) {

    var inputIsFocused = remember {
        mutableStateOf("")
    }

    var passwordVisible: Boolean by remember {
        mutableStateOf(false)
    }

    var inputFrontImg: (@Composable () -> Unit)? = if (frontImage != null) {
        {
            Image(
                painter = painterResource(frontImage),
                contentDescription = null,
                Modifier
                    .size(24.dp)
            )
        }
    } else null

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        OutlinedTextFieldBackground(
            Color.White,
        ) {
            if (type == "password") {
                OutlinedTextField(
                    label = { if (label != null) Text(label) },
                    placeholder = { Text(placeholder) },
                    value = value,
                    onValueChange = { value ->
                        onChange(value)
                    },
                    shape = RoundedCornerShape(10.dp),
                    isError = isError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { state ->
                            inputIsFocused.value = state.toString()
                        },
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.Black,
                        focusedIndicatorColor = MainBlue,
                        containerColor = Color.Transparent,
                        unfocusedIndicatorColor = Gray
                    ),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    leadingIcon = inputFrontImg,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                passwordVisible = !passwordVisible
                            }
                        ) {
                            var image = R.drawable.eye
                            if (passwordVisible) image = R.drawable.eyeoff
                            Image(
                                painter = painterResource(image),
                                contentDescription = null,
                                Modifier
                                    .size(24.dp)
                            )
                        }
                    }
                )
            } else {
                OutlinedTextField(
                    label = { if (label != null) Text(label) },
                    placeholder = { Text(placeholder) },
                    value = value,
                    onValueChange = { value ->
                        onChange(value)
                    },
                    isError = isError,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { state ->
                            inputIsFocused.value = state.toString()
                        },
                    keyboardOptions = keyboardOptions,
                    keyboardActions = keyboardActions,
                    leadingIcon = inputFrontImg,
                    colors = TextFieldDefaults.textFieldColors(
                        cursorColor = Color.Black,
                        focusedIndicatorColor = MainBlue,
                        containerColor = Color.Transparent,
                        unfocusedIndicatorColor = Gray
                    )
                )
            }
        }
    }
}

@Composable
fun OutlinedTextFieldBackground(
    color: Color,
    content: @Composable () -> Unit
) {

    var boxModifier = Modifier
        .background(
            color,
            shape = RoundedCornerShape(5.dp)
        )

    Box {
        Box(
            modifier = boxModifier.matchParentSize()
        )
        content()
    }
}