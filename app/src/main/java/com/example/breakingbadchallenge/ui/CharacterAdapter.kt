package com.example.breakingbadchallenge.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.breakingbadchallenge.R
import com.example.breakingbadchallenge.database.BreakingBadCharacter

/**
 * Character Adapter for the Character Card displayed on the RecyclerView
 * */

class CharacterAdapter (private val characterList:List<BreakingBadCharacter>):RecyclerView.Adapter<CharacterViewHolder>() {

    private lateinit var cListener : OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position : Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
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