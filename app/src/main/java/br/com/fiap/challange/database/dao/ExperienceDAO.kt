package br.com.fiap.challange.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.challange.model.Experience
import br.com.fiap.challange.model.Interest

@Dao
interface ExperienceDAO {

    @Insert
    fun save(user: Experience): Long

    @Update
    fun update(user: Experience): Int

    @Delete
    fun delete(user: Experience): Int

    @Query("SELECT * FROM tb_experience WHERE id = :id")
    fun getExperienceById(id: Int): Experience

    @Query("SELECT * FROM tb_experience WHERE userId = :id")
    fun getExperiencesByUserId(id: Long): List<Experience>
}