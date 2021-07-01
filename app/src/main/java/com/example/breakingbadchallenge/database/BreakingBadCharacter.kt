package com.example.breakingbadchallenge.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Breaking Bad Character Entity for Database interaction
 * */

@Entity
data class BreakingBadCharacter (
    @PrimaryKey val char_id: Int,
    val name: String,
    val birthday: String,
    val occupation: String,
    val img: String,
    val status: String,
    val nickname: String,
    val appearance: String,
    val portrayed: String,
    val category: String,
    val better_call_saul_appearance: String,
    var isFavorite: Boolean = false
)