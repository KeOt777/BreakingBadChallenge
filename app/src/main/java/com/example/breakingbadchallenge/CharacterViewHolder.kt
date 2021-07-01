package com.example.breakingbadchallenge

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.breakingbadchallenge.database.BreakingBadCharacter
import com.example.breakingbadchallenge.databinding.CharacterCardBinding
import com.squareup.picasso.Picasso

class CharacterViewHolder(view: View, listener: CharacterAdapter.onItemClickListener) : RecyclerView.ViewHolder(view){

    private val cardBinding = CharacterCardBinding.bind(view)
    val characterNameText: TextView = cardBinding.textCharacterName
    val characterNickName: TextView = cardBinding.textNickName
    val favoriteImage: ImageView = cardBinding.imgFavorite

    init {
        itemView.setOnClickListener {
            listener.onItemClick(adapterPosition)
        }
    }

    fun bind(character: BreakingBadCharacter){
        Picasso.get().load(character.img).into(cardBinding.imgCharacter)
        characterNameText.setText(character.name)
        characterNickName.setText(character.nickname)
        if(character.isFavorite){
            favoriteImage.setImageResource(R.drawable.ic_baseline_favorite_24_red)
        } else {
            favoriteImage.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }
}