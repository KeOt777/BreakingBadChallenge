package com.example.breakingbadchallenge

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.breakingbadchallenge.database.AppDataBase
import com.example.breakingbadchallenge.database.BreakingBadCharacter
import com.example.breakingbadchallenge.database.CharacterRepository
import com.example.breakingbadchallenge.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

lateinit var appDataBase: AppDataBase

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var characterListAdapter:CharacterAdapter
    private val characterResponseList = mutableListOf<CharacterResponse>()

    private val characterList = mutableListOf<BreakingBadCharacter>()

    private lateinit var occupationArray: ArrayList<String>

    val dataRepo = CharacterRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appDataBase = Room.databaseBuilder(applicationContext, AppDataBase::class.java, "characters-database").allowMainThreadQueries().build()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        characterListAdapter = CharacterAdapter(characterResponseList)

        setSupportActionBar(binding.toolbar)

        val checkLocalDB = checkLocalDataBase()

        var apiResult: String = API_OK

        if(!checkLocalDB){
            Log.d("BBC", "No Info in Local DB")
        } else {
            Log.d("BBC",  "Info in Local DB")
            Log.d("BBC","First Character: ${dataRepo.getCharacter(1).name}")
        }

        apiResult = consumeAPI()

        initRecycleView()

        if(apiResult != API_OK) {
            showError(apiResult)
        }

    }

    private fun checkLocalDataBase(): Boolean {
        var returnResult = true
        val localDBSize = dataRepo.getAll().size
        if(localDBSize <= 0){
            returnResult = false
        }
        return returnResult
    }

    private fun consumeAPI(): String {

        var returnString = API_OK

        val connectionManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectionManager.activeNetwork
        if (networkInfo != null) {
            CoroutineScope(Dispatchers.IO).launch{
                val call = getRetrofit().create(APIService::class.java).getCharacters(API_CALL_FIRST)
                val characters = call.body()
                runOnUiThread(){
                    if(call.isSuccessful){
                        val charactersInfo:List<CharacterResponse> = characters ?: emptyList()
                        characterResponseList.clear()
                        characterResponseList.addAll(charactersInfo)

//                        charactersInfo.forEach {
//                            val characterOccupations = it.occupation.joinToString(separator = "|")
//                            val characterAppearances = it.appearance.joinToString(separator = "|")
//                            val characterBCSAppearances = it.better_call_saul_appearance.joinToString(separator = "|")
//
//                            val character = BreakingBadCharacter(it.char_id, it.name, it.birthday, characterOccupations, it.img, it.status, it.nickname, characterAppearances, it.portrayed, it.category, characterBCSAppearances)
//
//                            dataRepo.insertAll(character)
//                        }

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
        Toast.makeText(this,errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun initRecycleView() {
        characterListAdapter = CharacterAdapter(characterResponseList)
        binding.rvCharacters.layoutManager = LinearLayoutManager(this)
        binding.rvCharacters.adapter = characterListAdapter

        characterListAdapter.setOnItemClickListener(object: CharacterAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val activityIntent = Intent(this@MainActivity, CharacterDetail::class.java)
                activityIntent.putExtra(CHARACTER_ID, characterResponseList[position].char_id)
                activityIntent.putExtra(CHARACTER_NAME, characterResponseList[position].name)
                activityIntent.putExtra(CHARACTER_NICKNAME, characterResponseList[position].nickname)

                occupationArray = arrayListOf<String>()

                characterResponseList[position].occupation.forEach{
                    occupationArray.add(it)
                }

                activityIntent.putExtra(CHARACTER_OCCUPATION, occupationArray)
                activityIntent.putExtra(CHARACTER_STATUS, characterResponseList[position].status)
                activityIntent.putExtra(CHARACTER_PORTRAYED, characterResponseList[position].portrayed)
                activityIntent.putExtra(CHARACTER_IMAGE, characterResponseList[position].img)
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