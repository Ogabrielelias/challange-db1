package br.com.fiap.challange.utils

import SharedViewModel
import androidx.navigation.NavHostController

fun logoutUser(
    navController: NavHostController,
    sharedViewModel: SharedViewModel
) {
    navController.navigate("login")
    sharedViewModel.user = null
}