package com.example.breakingbadchallenge

import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.breakingbadchallenge.databinding.CharacterCardBinding
import com.squareup.picasso.Picasso

class CharacterViewHolder(view: View, listener: CharacterAdapter.onItemClickListener) : RecyclerView.ViewHolder(view){

    private val cardBinding = CharacterCardBinding.bind(view)
    val characterNameText: TextView = cardBinding.textCharacterName
    val characterNickName: TextView = cardBinding.textNickName
    val favorite: ImageView = cardBinding.imgFavorite

    init {
        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }



    fun bind(character: CharacterResponse){
        Picasso.get().load(character.img).into(cardBinding.imgCharacter)
        characterNameText.setText(character.name)
        characterNickName.setText(character.nickname)

    }
}