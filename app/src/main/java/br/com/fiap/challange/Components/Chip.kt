import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import br.com.fiap.challange.ui.theme.MainBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chip(
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
                imageVector = Icons.Default.Close,
                contentDescription = "Localized description",
                modifier = Modifier.size(InputChipDefaults.AvatarSize),
                tint = MainBlue
            )
        }
    )
}
