package br.com.fiap.challange.Components

import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import br.com.fiap.challange.ui.theme.Gray
import br.com.fiap.challange.ui.theme.MainBlue

import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.window.PopupProperties

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Select(
    items: List<String>,
    label: String? = null,
    onSelect: ((value: String) -> Unit),
    writableSelect: Boolean = false
) {

    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember {
        mutableStateOf(
            if (label != null) "" else items[0]
        )
    }

    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth(),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        OutlinedTextField(
            value = selectedText,
            onValueChange = { value ->
                if (writableSelect) {
                    selectedText = value
                    expanded = true
                }
            },
            label = {
                if (label != null) {
                    Text(label)
                }
            },
            readOnly = !writableSelect,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = Color.Black,
                focusedIndicatorColor = MainBlue,
                containerColor = Color.Transparent,
                unfocusedIndicatorColor = Gray
            ),
        )

        DropdownMenu(
            modifier = Modifier
                .exposedDropdownSize()
                .fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = { expanded = false },
            properties = PopupProperties(focusable = false)
        ) {
            items.filter { item ->
                if (writableSelect && selectedText.length > 0) {
                    (item.contains(selectedText, ignoreCase = true))
                } else {
                    true
                }
            }.forEach { item ->
                DropdownMenuItem(
                    modifier = Modifier.fillMaxWidth(),
                    text = { Text(text = item) },
                    onClick = {
                        onSelect(item)
                        if(writableSelect){
                            selectedText = ""
                        }else{
                            selectedText = item
                        }
                        expanded = false
                    }
                )
            }
        }
    }
}
