package com.codepath.scrollpokemon

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class PokemonAdapter(private val pokemonList: List<Pokemon>) :
    RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPokemon: ImageView = itemView.findViewById(R.id.ivPokemon)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvType: TextView = itemView.findViewById(R.id.tvType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.tvName.text = pokemon.name.replaceFirstChar { it.uppercase() }
        holder.tvType.text = "Type: ${pokemon.type.replaceFirstChar { it.uppercase() }}"
        Glide.with(holder.itemView.context).load(pokemon.imageUrl).into(holder.ivPokemon)
    }

    override fun getItemCount(): Int = pokemonList.size
}
