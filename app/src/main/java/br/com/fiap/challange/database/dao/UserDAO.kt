package br.com.fiap.challange.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.challange.model.User

@Dao
interface UserDAO {

    @Insert
    fun save(user: User): Long

    @Update
    fun update(user: User): Int

    @Delete
    fun delete(user: User): Int

    @Query("SELECT * FROM tb_user WHERE id = :id")
    fun getUserById(id: Int): User

    @Query("SELECT * FROM tb_user WHERE email = :email and password = :senha")
    fun getUserByLogin(email:String, senha:String): User

    @Query("SELECT * FROM tb_user ORDER BY name ASC")
    fun listUser(): List<User>
}