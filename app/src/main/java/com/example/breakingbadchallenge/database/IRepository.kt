package com.example.breakingbadchallenge.database

/**
 * Interface for Repository Pattern
 * */

interface IRepository<T> {
    fun getAll(): List<T>
    fun getCharacter(id: Int): BreakingBadCharacter
    fun insertAll(character: BreakingBadCharacter)
    fun updateFavorite(character: BreakingBadCharacter)
    fun queryFavorites(): List<T>
    /**
     * To be used only for cleaning the DB for Unit Testing
     * */
    fun deleteAll()
}