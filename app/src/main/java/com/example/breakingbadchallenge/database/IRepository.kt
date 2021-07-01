package com.example.breakingbadchallenge.database

interface IRepository<T> {
    fun getAll(): kotlin.collections.List<T>
    fun getCharacter(id: Int): BreakingBadCharacter
    fun insertAll(character: BreakingBadCharacter)
    fun updateFavorite(character: BreakingBadCharacter)
    fun queryFavorites(): List<T>
}