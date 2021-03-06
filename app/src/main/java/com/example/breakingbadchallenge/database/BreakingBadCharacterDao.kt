package com.example.breakingbadchallenge.database

import androidx.room.*

/**
 * Character Data Access Object
 * */

@Dao
interface BreakingBadCharacterDao {
    @Query("SELECT * FROM breakingbadcharacter")
    fun getAll(): List<BreakingBadCharacter>

    @Transaction
    @Query("SELECT * FROM breakingbadcharacter WHERE char_id=:id")
    fun getCharacter(id: Int): BreakingBadCharacter

    @Insert
    fun insertAll(vararg character: BreakingBadCharacter)

    @Update (entity = BreakingBadCharacter::class)
    fun update(value: BreakingBadCharacter)

    @Query("SELECT * FROM breakingbadcharacter ORDER BY isFavorite DESC")
    fun queryFavorites(): List<BreakingBadCharacter>

    /**
     * To be used only for cleaning the DB for Unit Testing
     * */
    @Query("DELETE FROM breakingbadcharacter")
    fun deleteAll()
}