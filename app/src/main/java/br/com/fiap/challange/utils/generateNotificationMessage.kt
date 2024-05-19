package br.com.fiap.challange.utils

fun generateNotificationMessage(role: String): String {
    return listOf(
        "Você encontrou um novo ${role}!",
        "Um novo ${role} foi encontrado!",
        "Parabéns! Você conseguiu um match!",
        "Você conseguiu um novo ${role}!",
        "Você encontrou um ${role} brilhante!"
    ).random()
}