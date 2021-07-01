package com.example.breakingbadchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.example.breakingbadchallenge.database.BreakingBadCharacter
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
    private var occupation: String = EMPTY_STRING
    private var isFavorite: Boolean = false


    private lateinit var favoriteListener : OnImageClickListener

    interface OnImageClickListener{
        fun onItemClick()
    }

    fun setOnImageClickListener(listener: OnImageClickListener){
        favoriteListener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCharacterDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        char_id = savedInstanceState?.getInt(CHARACTER_ID) ?: intent.getIntExtra(CHARACTER_ID, DEFAULT_CHARACTER_ID)
        name = savedInstanceState?.getString(CHARACTER_NAME) ?: intent.getStringExtra(CHARACTER_NAME).toString()
        nickname = savedInstanceState?.getString(CHARACTER_NICKNAME) ?: intent.getStringExtra(CHARACTER_NICKNAME).toString()
        portrayed = savedInstanceState?.getString(CHARACTER_PORTRAYED) ?: intent.getStringExtra(CHARACTER_PORTRAYED).toString()
        status = savedInstanceState?.getString(CHARACTER_STATUS) ?: intent.getStringExtra(CHARACTER_STATUS).toString()
        img = savedInstanceState?.getString(CHARACTER_IMAGE) ?: intent.getStringExtra(CHARACTER_IMAGE).toString()
        occupation = savedInstanceState?.getString(CHARACTER_OCCUPATION) ?: intent.getStringExtra(CHARACTER_OCCUPATION).toString().replace("|",", ")
        isFavorite = savedInstanceState?.getBoolean(CHARACTER_FAVORITE) ?: intent.getBooleanExtra(CHARACTER_FAVORITE, false)

        if(char_id > DEFAULT_CHARACTER_ID){
            displayCharacter()
        }

        binding.imgFavoriteDetail.setOnClickListener(object: CharacterDetail.OnImageClickListener,
            View.OnClickListener {
            override fun onItemClick() {
                //dataRepo.updateFavorite(char_id)
            }

            override fun onClick(v: View?) {
                Log.d("BBC","Click")
                val character = dataRepo.getCharacter(char_id)
                character.isFavorite = !character.isFavorite
                isFavorite = !isFavorite
                dataRepo.updateFavorite(character)
                updateImage()
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.putInt(CHARACTER_ID, char_id)
        outState?.putString(CHARACTER_NAME, name)
        outState?.putString(CHARACTER_NICKNAME, nickname)
        outState?.putString(CHARACTER_OCCUPATION, occupation)
        outState?.putString(CHARACTER_STATUS, status)
        outState?.putString(CHARACTER_PORTRAYED, portrayed)
        outState?.putString(CHARACTER_IMAGE, img)
        outState?.putBoolean(CHARACTER_FAVORITE, isFavorite)
    }

    private fun displayCharacter() {
        Picasso.get().load(img).into(binding.imgCharacterDetail)
        binding.detailToolbar.setTitle(name)
        binding.textNicknameDetail.setText(nickname)
        binding.textPortrayed.setText(portrayed)
        binding.textStatus.setText(status)
        binding.textOccupation.setText(occupation)

        updateImage()
    }

    private fun updateImage() {
        if(isFavorite){
            binding.imgFavoriteDetail.setImageResource(R.drawable.ic_baseline_favorite_24_red)
        } else {
            binding.imgFavoriteDetail.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }


    }
}