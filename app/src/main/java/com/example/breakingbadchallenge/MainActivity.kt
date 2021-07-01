package com.example.breakingbadchallenge

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.breakingbadchallenge.database.AppDataBase
import com.example.breakingbadchallenge.database.BreakingBadCharacter
import com.example.breakingbadchallenge.database.CharacterRepository
import com.example.breakingbadchallenge.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

lateinit var appDataBase: AppDataBase
val dataRepo = CharacterRepository()

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    var apiResult: String = API_OK

    private lateinit var characterListAdapter:CharacterAdapter
    private val characterResponseList = mutableListOf<CharacterResponse>()

    private var characterList = mutableListOf<BreakingBadCharacter>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appDataBase = Room.databaseBuilder(applicationContext, AppDataBase::class.java, CHARACTER_DATABASE).allowMainThreadQueries().build()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        characterListAdapter = CharacterAdapter(characterList)

        setSupportActionBar(binding.toolbar)

        initData()

        initRecycleView()

        if(apiResult != API_OK) {
            showError(apiResult)
        }

    }

    private fun initData() {

        checkLocalDataBase()

        if(characterList.size == 0){
            apiResult = consumeAPI()
        } else {
            characterListAdapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        checkLocalDataBase()
        characterListAdapter.notifyDataSetChanged()
        initRecycleView()
    }


    private fun checkLocalDataBase() {
        characterList = dataRepo.queryFavorites() as MutableList<BreakingBadCharacter>
    }

    private fun consumeAPI(): String {

        var returnString = API_OK

        val connectionManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectionManager.activeNetwork
        if (networkInfo != null) {
            CoroutineScope(Dispatchers.IO).launch{
                val call = getRetrofit().create(APIService::class.java).getCharacters(API_CALL_INITIAL_TEN)
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

    private fun showError(errorMessage: String) {
        Snackbar.make(View(this), errorMessage, Snackbar.LENGTH_LONG).show()
    }

    private fun initRecycleView() {
        characterListAdapter = CharacterAdapter(characterList)
        binding.rvCharacters.layoutManager = LinearLayoutManager(this)
        binding.rvCharacters.adapter = characterListAdapter

        characterListAdapter.setOnItemClickListener(object: CharacterAdapter.onItemClickListener{
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

    private fun getRetrofit() :Retrofit {
        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}