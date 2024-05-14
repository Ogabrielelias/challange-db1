package br.com.fiap.challange.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import br.com.fiap.challange.model.Interest

interface MatchDAO {
    @Insert
    fun save(user: Interest): Long

    @Update
    fun update(user: Interest): Int

    @Delete
    fun delete(user: Interest): Int
}