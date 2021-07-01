package com.example.breakingbadchallenge.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.breakingbadchallenge.R
import com.example.breakingbadchallenge.database.BreakingBadCharacter
import com.example.breakingbadchallenge.databinding.CharacterCardBinding
import com.squareup.picasso.Picasso

/**
 * CharacterViewHolder for the Character Card displayed on the RecyclerView
 * */

class CharacterViewHolder(view: View, listener: CharacterAdapter.onItemClickListener) : RecyclerView.ViewHolder(view){

    private val cardBinding = CharacterCardBinding.bind(view)
    private val characterNameText: TextView = cardBinding.textCharacterName
    private val characterNickName: TextView = cardBinding.textNickName
    private val favoriteImage: ImageView = cardBinding.imgFavorite

    init {
        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }

    fun bind(character: BreakingBadCharacter){
        Picasso.get().load(character.img).into(cardBinding.imgCharacter)
        characterNameText.text = character.name
        characterNickName.text = character.nickname
        if(character.isFavorite){
            favoriteImage.setImageResource(R.drawable.ic_baseline_favorite_24_red)
        } else {
            favoriteImage.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }
}