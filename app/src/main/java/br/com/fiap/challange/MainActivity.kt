package br.com.fiap.challange

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.compose.rememberNavController
import br.com.fiap.challange.screens.TabNavigationScreen
import br.com.fiap.challange.ui.theme.ChallangeTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val CHANNELID = "canal_dus_guri"
    private val notificationId = 69

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "notificações educo"
            val descriptionText = "Educo"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNELID, name, importance).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    @SuppressLint("MissingPermission")
    private fun sendNotification(role:String, person:String, subject:String) {

        val notificationTexts = listOf(
            "Você encontrou um novo ${role}!",
            "Um novo ${role} foi encontrado!",
            "Parabéns! Você conseguiu um match!",
            "Você conseguiu um novo ${role}!",
            "Você encontrou um ${role} brilhante!"
            )

        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        var roleText = if(role == "mentor") "ensinar" else "aprender"

        val builder = NotificationCompat.Builder(this, CHANNELID)
            .setSmallIcon(R.drawable.compasswhite)
            .setContentTitle(notificationTexts.random())
            .setContentText("${person} gostaria de ${roleText} ${subject} a você.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createNotificationChannel()

        setContent {
            ChallangeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    val navController = rememberNavController()
                    TabNavigationScreen(navController, sendNotification = { role, person, subject -> sendNotification(role, person, subject) });
                }
            }
        }
    }
}