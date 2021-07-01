package com.example.breakingbadchallenge.database

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room Database structure and version control
 * */
@Database(entities = [BreakingBadCharacter::class], version = 1)
abstract class AppDataBase :RoomDatabase() {
    abstract fun charactersDao(): BreakingBadCharacterDao
}