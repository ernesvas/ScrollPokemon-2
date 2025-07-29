package com.codepath.scrollpokemon

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import androidx.recyclerview.widget.GridLayoutManager

class MainActivity : AppCompatActivity() {

    private lateinit var rvPokemon: RecyclerView
    private val client = AsyncHttpClient()
    private val pokemonList = mutableListOf<Pokemon>()
    private lateinit var adapter: PokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvPokemon = findViewById(R.id.rvPokemon)
        adapter = PokemonAdapter(pokemonList)
        rvPokemon.layoutManager = GridLayoutManager(this, 2)
        rvPokemon.adapter = adapter

        fetchPokemonList()
    }

    private fun fetchPokemonList() {
        val url = "https://pokeapi.co/api/v2/pokemon?limit=20"

        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int, headers: Array<out Header>?, response: JSONObject
            ) {
                val results = response.getJSONArray("results")
                for (i in 0 until results.length()) {
                    val pokemonObject = results.getJSONObject(i)
                    val name = pokemonObject.getString("name")
                    val detailsUrl = pokemonObject.getString("url")
                    fetchPokemonDetails(name, detailsUrl)
                }
            }

            override fun onFailure(
                statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?
            ) {
                Log.e("MainActivity", "Failed to fetch Pokémon list", throwable)
            }
        })
    }

    private fun fetchPokemonDetails(name: String, url: String) {
        client.get(url, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject) {
                val imageUrl = response.getJSONObject("sprites").getString("front_default")

                val typeArray = response.getJSONArray("types")
                val type = typeArray.getJSONObject(0).getJSONObject("type").getString("name")

                val pokemon = Pokemon(name, imageUrl, type)
                pokemonList.add(pokemon)
                adapter.notifyItemInserted(pokemonList.size - 1)
            }

            override fun onFailure(
                statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?
            ) {
                Log.e("MainActivity", "Failed to fetch Pokémon details", throwable)
            }
        })
    }
}
