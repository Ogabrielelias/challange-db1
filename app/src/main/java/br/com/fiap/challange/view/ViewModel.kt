import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import br.com.fiap.challange.model.UserWithExperiencesAndInterests

class SharedViewModel : ViewModel() {
    var user: UserWithExperiencesAndInterests? by mutableStateOf(null)
}
