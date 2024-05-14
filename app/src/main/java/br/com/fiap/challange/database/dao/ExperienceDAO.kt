package br.com.fiap.challange.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.challange.model.Interest

@Dao
interface ExperienceDAO {

    @Insert
    fun save(user: Interest): Long

    @Update
    fun update(user: Interest): Int

    @Delete
    fun delete(user: Interest): Int

    @Query("SELECT * FROM tb_experience WHERE id = :id")
    fun getExperienceById(id: Int): Interest
}