package com.example.breakingbadchallenge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.breakingbadchallenge.database.BreakingBadCharacter

class CharacterAdapter (val characterList:List<BreakingBadCharacter>):RecyclerView.Adapter<CharacterViewHolder>() {

    private lateinit var cListener : onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position : Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        cListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val layoutInflater =LayoutInflater.from(parent.context)
        return CharacterViewHolder(layoutInflater.inflate(R.layout.character_card, parent, false), cListener)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val item = characterList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return characterList.size
    }
}