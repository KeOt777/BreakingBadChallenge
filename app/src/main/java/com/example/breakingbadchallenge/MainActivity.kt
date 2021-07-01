/**
 *      Project: Breaking Bad Challenge
 *      Developer: Kevin Ruiz
 *      Email: kevin.ruiz.gt@gmail.com
 *      Linkedin: https://www.linkedin.com/in/kevin-ruiz-ramos/
 *      Github: https://github.com/KeOt777/BreakingBadChallenge
 *      Created: June-29-2021
 *      Last Modified: July-01-2021
 * */

package com.example.breakingbadchallenge

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room

//import com.example.breakingbadchallenge.apihandler.APIService
//import com.example.breakingbadchallenge.apihandler.CharacterResponse

import com.example.breakingbadchallenge.apihandler.*

import com.example.breakingbadchallenge.database.AppDataBase
import com.example.breakingbadchallenge.database.BreakingBadCharacter
import com.example.breakingbadchallenge.database.CharacterRepository
import com.example.breakingbadchallenge.databinding.ActivityMainBinding
import com.example.breakingbadchallenge.ui.CharacterAdapter
import com.example.breakingbadchallenge.ui.CharacterDetail
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

lateinit var appDataBase: AppDataBase
val dataRepo = CharacterRepository()

/**
 * Main Activity
 * */

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var characterListAdapter: CharacterAdapter

    private val characterResponseList = mutableListOf<CharacterResponse>()
    private var characterList = mutableListOf<BreakingBadCharacter>()

    private var apiResult: String = API_OK

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appDataBase = Room.databaseBuilder(applicationContext, AppDataBase::class.java, CHARACTER_DATABASE).allowMainThreadQueries().build()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        characterListAdapter = CharacterAdapter(characterList)

        setSupportActionBar(binding.toolbar)

        /**
         * Should only be used when total clear of the Datatable is needed or wanted
         * */
        if(CLEAR_DATABASE) {
            clearAllData()
        }

        initData()

        initRecycleView()

        /**
         * In case of any error on the API consumption, show a Snackbar with the corresponding error message
         * */
        if(apiResult != API_OK) {
            showSnack(apiResult)
        }

    }

    /**
     * To Ensure a Clean Database during testing
     * */
    private fun clearAllData() {
        CoroutineScope(Dispatchers.IO).launch{
            appDataBase.clearAllTables()

            runOnUiThread(){
//                setContentView(R.layout.activity_main)
//                binding = ActivityMainBinding.inflate(layoutInflater)
                characterListAdapter.notifyDataSetChanged()
                initRecycleView()
                //showSnack(CLEAR_DATABASE_MESSAGE)
            }
        }
    }

    /**
     * Ensure that application always checks local Database before fetching data from the API
     * */
    private fun initData() {

        checkLocalDataBase()

        if(characterList.size == 0){
            apiResult = consumeAPI()
        } else {
            characterListAdapter.notifyDataSetChanged()
        }
    }

    /**
     * Ensure all data and RecycleView are properly reloaded when Activity is resumed
     * */
    override fun onResume() {
        super.onResume()
        checkLocalDataBase()
        characterListAdapter.notifyDataSetChanged()
        initRecycleView()
    }

    /**
     * Check Local Database for information
     * */
    private fun checkLocalDataBase() {
        characterList = dataRepo.queryFavorites() as MutableList<BreakingBadCharacter>
    }

    /**
     * Consumption of API Asynchronously and populate local data base with the response
     * */
    private fun consumeAPI(): String {

        var returnString = API_OK

        val connectionManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectionManager.activeNetwork
        if (networkInfo != null) {
            CoroutineScope(Dispatchers.IO).launch{
                val call = getRetrofit().create(APIService::class.java).getCharacters(API_CALL_ALL)
                val characters = call.body()
                runOnUiThread(){
                    if(call.isSuccessful){
                        val charactersInfo:List<CharacterResponse> = characters ?: emptyList()
                        characterResponseList.clear()
                        characterResponseList.addAll(charactersInfo)

                        charactersInfo.forEach {
                            val characterOccupations = it.occupation.joinToString(separator = JOIN_TO_STRING_SEPARATOR)
                            val characterAppearances = it.appearance.joinToString(separator = JOIN_TO_STRING_SEPARATOR)
                            val characterBCSAppearances = it.better_call_saul_appearance.joinToString(separator = JOIN_TO_STRING_SEPARATOR)

                            val character = BreakingBadCharacter(it.char_id, it.name, it.birthday, characterOccupations, it.img, it.status, it.nickname, characterAppearances, it.portrayed, it.category, characterBCSAppearances)

                            dataRepo.insertAll(character)

                            characterList.add(character)
                        }

                        characterListAdapter.notifyDataSetChanged()
                    } else {
                        returnString = API_ERROR
                    }
                }
            }
        } else {
            returnString = NO_NETWORK
        }

        return returnString

    }

    /**
     * Reusable showError method
     * */
    private fun showSnack(errorMessage: String) {
        Snackbar.make(View(this), errorMessage, Snackbar.LENGTH_LONG).show()
    }

    /**
     * Separate function to init Recycle View ensures it always reloads whenever the character collection/list is upddated
     * */
    private fun initRecycleView() {
        characterListAdapter = CharacterAdapter(characterList)
        binding.rvCharacters.layoutManager = LinearLayoutManager(this)
        binding.rvCharacters.adapter = characterListAdapter

        characterListAdapter.setOnItemClickListener(object: CharacterAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                val activityIntent = Intent(this@MainActivity, CharacterDetail::class.java)
                activityIntent.putExtra(CHARACTER_ID, characterList[position].char_id)
                activityIntent.putExtra(CHARACTER_NAME, characterList[position].name)
                activityIntent.putExtra(CHARACTER_NICKNAME, characterList[position].nickname)
                activityIntent.putExtra(CHARACTER_OCCUPATION, characterList[position].occupation)
                activityIntent.putExtra(CHARACTER_STATUS, characterList[position].status)
                activityIntent.putExtra(CHARACTER_PORTRAYED, characterList[position].portrayed)
                activityIntent.putExtra(CHARACTER_IMAGE, characterList[position].img)
                activityIntent.putExtra(CHARACTER_FAVORITE, characterList[position].isFavorite)
                startActivity(activityIntent)
            }

        })
    }

    /**
     * API Setup
     * */
    private fun getRetrofit() :Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}