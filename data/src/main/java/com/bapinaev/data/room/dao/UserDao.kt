package com.bapinaev.data.room.dao

import androidx.room.*
import com.bapinaev.data.room.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE login = :login LIMIT 1")
    suspend fun getUserByLogin(login: String): UserEntity?

    @Query("DELETE FROM users WHERE login = :login")
    suspend fun removeUserByLogin(login: String): Int
}