package com.example.breakingbadchallenge

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.breakingbadchallenge.database.AppDataBase
import com.example.breakingbadchallenge.database.CharacterRepository
import com.example.breakingbadchallenge.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//lateinit var appDataBase: AppDataBase

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var characterListAdapter:CharacterAdapter
    private val characterList = mutableListOf<CharacterResponse>()

    private lateinit var occupationArray: ArrayList<String>

//    val dataRepo = CharacterRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        appDataBase = Room.databaseBuilder(applicationContext, AppDataBase::class.java, "characters-database").allowMainThreadQueries().build()

//        Log.d("BBC", appDataBase.charactersDao().toString())

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        characterListAdapter = CharacterAdapter(characterList)

        setSupportActionBar(binding.toolbar)

        //val checkLocalDB = checkLocalDataBase()
        val ApiResult = consumeAPI()

//        if(!checkLocalDB){
//
//        }



        if(ApiResult){
            initRecycleView()
        }

    }
//
//    private fun checkLocalDataBase(): Boolean {
//        var returnResult = true
//        val localDBSize = dataRepo.getAll().size
//        if(localDBSize <= 0){
//            returnResult = false
//        }
//        return returnResult
//    }

    private fun consumeAPI(): Boolean {
        var returnResult: Boolean = true
        CoroutineScope(Dispatchers.IO).launch{
            Log.d("BBC","Launching Rest API")
            val call = getRetrofit().create(APIService::class.java).getCharacters(API_CALL_FIRST)
            val characters = call.body()
            runOnUiThread(){
                if(call.isSuccessful){
                    val charactersInfo:List<CharacterResponse> = characters ?: emptyList()
                    characterList.clear()
                    characterList.addAll(charactersInfo)
                    Log.d("BBC", "characterList: ${characterList.size}")
                    characterListAdapter.notifyDataSetChanged()
                } else {
                    showError()
                    returnResult = false
                }
            }
        }
        return returnResult
    }

    private fun showError() {
        Toast.makeText(this,"There's been an error", Toast.LENGTH_SHORT).show()
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

                occupationArray = arrayListOf<String>()

                characterList[position].occupation.forEach{
                    occupationArray.add(it)
                }

                activityIntent.putExtra(CHARACTER_OCCUPATION, occupationArray)
                activityIntent.putExtra(CHARACTER_STATUS, characterList[position].status)
                activityIntent.putExtra(CHARACTER_PORTRAYED, characterList[position].portrayed)
                activityIntent.putExtra(CHARACTER_IMAGE, characterList[position].img)
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