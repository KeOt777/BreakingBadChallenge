package com.example.breakingbadchallenge.database

interface IRepository<T> {
    fun getAll(): kotlin.collections.List<T>
    fun getCharacter(id: Int): BreakingBadCharacter
    fun insertAll(characters: BreakingBadCharacter)
    fun updateFavorite(id: Int, favorite: Boolean = true)
}