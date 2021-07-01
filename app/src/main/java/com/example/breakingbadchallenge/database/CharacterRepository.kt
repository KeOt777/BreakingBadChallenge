package com.example.breakingbadchallenge.database

import com.example.breakingbadchallenge.appDataBase

/**
 * Character Repository
 * */

class CharacterRepository : IRepository<BreakingBadCharacter>{
    override fun getAll(): List<BreakingBadCharacter> {
        return appDataBase.charactersDao().getAll()
    }

    override fun getCharacter(id: Int): BreakingBadCharacter {
        return appDataBase.charactersDao().getCharacter(id)
    }

    override fun insertAll(character: BreakingBadCharacter) {
        val characterExists = getCharacter(character.char_id)
        if(characterExists == null){
            appDataBase.charactersDao().insertAll(character)
        }
    }

    override fun updateFavorite(character: BreakingBadCharacter) {
        appDataBase.charactersDao().update(character)
    }

    override fun queryFavorites(): List<BreakingBadCharacter> {
        return appDataBase.charactersDao().queryFavorites()
    }

    /**
     * To be used only for cleaning the DB for Unit Testing
     * */
    override fun deleteAll() {
        appDataBase.charactersDao().deleteAll()
    }

}