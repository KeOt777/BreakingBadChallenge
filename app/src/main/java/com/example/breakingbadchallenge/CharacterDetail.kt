package com.example.breakingbadchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.breakingbadchallenge.databinding.ActivityCharacterDetailBinding
import com.squareup.picasso.Picasso

class CharacterDetail : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterDetailBinding

    private var char_id: Int = DEFAULT_CHARACTER_ID
    private var name: String = EMPTY_STRING
    private var nickname: String = EMPTY_STRING
    private var portrayed: String = EMPTY_STRING
    private var status: String = EMPTY_STRING
    private var img: String = EMPTY_STRING
    private lateinit var occupation: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCharacterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        char_id = intent.getIntExtra(CHARACTER_ID, DEFAULT_CHARACTER_ID)
        name = intent.getStringExtra(CHARACTER_NAME).toString()
        nickname = intent.getStringExtra(CHARACTER_NICKNAME).toString()
        portrayed = intent.getStringExtra(CHARACTER_PORTRAYED).toString()
        status = intent.getStringExtra(CHARACTER_STATUS).toString()
        img = intent.getStringExtra(CHARACTER_IMAGE).toString()

        occupation = arrayListOf<String>()

        occupation = intent.getStringArrayListExtra(CHARACTER_OCCUPATION) as ArrayList<String>

        if(char_id > DEFAULT_CHARACTER_ID){
            displayCharacter()
        }

    }

    private fun displayCharacter() {
        Picasso.get().load(img).into(binding.imgCharacterDetail)
        binding.detailToolbar.setTitle(name)
        binding.textNicknameDetail.setText(nickname)
        binding.textPortrayed.setText(portrayed)
        binding.textStatus.setText(status)

        var occupations:String = EMPTY_STRING

        occupation.forEach {
            if(it.equals(occupation.last())) {
                occupations = occupations.plus(it)
            } else {
                occupations = occupations.plus(it).plus(", ")
            }
        }
        binding.textOccupation.setText(occupations)
    }
}