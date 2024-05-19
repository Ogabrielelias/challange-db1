package br.com.fiap.challange.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.challange.model.Interest

@Dao
interface InterestDAO {

    @Insert
    fun save(user: Interest): Long

    @Update
    fun update(user: Interest): Int

    @Delete
    fun delete(user: Interest): Int

    @Query("SELECT * FROM tb_interest WHERE id = :id")
    fun getInterestById(id: Int): Interest

    @Query("SELECT * FROM tb_interest WHERE userId = :id")
    fun getInterestByUserId(id: Long): List<Interest>
}