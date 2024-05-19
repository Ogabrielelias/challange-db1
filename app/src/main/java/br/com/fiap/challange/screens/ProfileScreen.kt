package br.com.fiap.challange.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.com.fiap.challange.constants.ExperiencesLevelList
import br.com.fiap.challange.constants.InterestsLevelList
import br.com.fiap.challange.database.repository.UserRepository
import br.com.fiap.challange.model.UserWithExperiencesAndInterests
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavHostController, userId: String?) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val userRepository = UserRepository(context)
    val user = remember {
        mutableStateOf<UserWithExperiencesAndInterests?>(null)
    }

    val profileId = userId!!.toLong()

    scope.launch {
        user.value = userRepository.getUserById(profileId)
    }

    if (user.value != null) {
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
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                Text(
                    user.value!!.user.name,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Start
                )
                if (user.value!!.experiences.isNotEmpty()){
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    )
                    {
                        Text(
                            "Experiências:",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Start
                        )
                        user.value!!.experiences.forEach{ experience ->
                            ExperiencesOrInterests(
                                experienceorknow = "experiência",
                                level = ExperiencesLevelList[experience.level - 1],
                                describe = experience.description,
                                subject = experience.experience
                            )
                        }
                    }
                }
                if (user.value!!.interests.isNotEmpty()){
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    )
                    {
                        Text(
                            "Interesse:",
                            fontSize = 30.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Start
                        )
                        user.value!!.interests.forEach{ interest ->
                            ExperiencesOrInterests(
                                experienceorknow = "conhecimento",
                                level = InterestsLevelList[interest.level - 1],
                                describe = interest.description,
                                subject = interest.interest
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(60.dp))
            }
        }
    }

}

@Composable
fun ExperiencesOrInterests(
    experienceorknow: String,
    level: String,
    describe: String,
    subject:String
) {

    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp)
    )
    {
        Text(
            "${subject}:",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Start
        )

        Row() {
            Text(
                "Nível de ${experienceorknow}:",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start
            )
            Text(
                " ${level}",
                fontSize = 18.sp
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
        }
        Text(
            "${describe}",
            fontSize = 16.sp
        )
    }
}