package com.example.breakingbadchallenge.database

//import com.example.breakingbadchallenge.appDataBase
lateinit var appDataBase: AppDataBase
class CharacterRepository : IRepository<BreakingBadCharacter>{
    override fun getAll(): List<BreakingBadCharacter> {
        return appDataBase.charactersDao().getAll()
    }

    override fun getCharacter(id: Int): BreakingBadCharacter {
        return appDataBase.charactersDao().getCharacter(id)
    }

    override fun insertAll(character: BreakingBadCharacter) {
        appDataBase.charactersDao().insertAll(character)
    }

    override fun updateFavorite(id: Int, favorite: Boolean) {
        var character = getCharacter(id)
        character.isFavorite = !character.isFavorite
        appDataBase.charactersDao().update(character)
    }

}