package br.com.fiap.challange.manager

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserManager(context: Context) {

    // Create the dataStore and give it a name same as shared preferences
    private val dataStore = context.createDataStore(name = "user_prefs")

    // Create some keys we will use them to store and retrieve the data
    companion object {
        val USER_NAME_KEY = preferencesKey<String>("USER_NAME")
        val USER_IS_MENTOR_KEY = preferencesKey<Int>("USER_IS_MENTOR_KEY")
        val USER_IS_STUDENT_KEY = preferencesKey<Int>("USER_IS_STUDENT_KEY")
    }

    // Store user data
    // refer to the data store and using edit
    // we can store values using the keys
    suspend fun storeUser(name: String, isMentor: Int, isStudent: Int) {
        dataStore.edit {
            it[USER_NAME_KEY] = name
            it[USER_IS_MENTOR_KEY] = isMentor
            it[USER_IS_STUDENT_KEY] = isStudent

            // here it refers to the preferences we are editing

        }
    }

    // Create an age flow to retrieve age from the preferences
    // flow comes from the kotlin coroutine

    val userIsMentorFlow: Flow<Int> = dataStore.data.map {
        it[USER_IS_MENTOR_KEY] ?: 0
    }

    val userIsStudentFlow: Flow<Int> = dataStore.data.map {
        it[USER_IS_STUDENT_KEY] ?: 0
    }

    // Create a name flow to retrieve name from the preferences
    val userNameFlow: Flow<String> = dataStore.data.map {
        it[USER_NAME_KEY] ?: ""
    }
}
