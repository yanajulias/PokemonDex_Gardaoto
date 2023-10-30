package gardaoto.projects.pokemondex.repository

import gardaoto.projects.pokemondex.frameworks.model.PokemonDetail
import gardaoto.projects.pokemondex.frameworks.model.PokemonList
import gardaoto.projects.pokemondex.util.Resource

interface PokemonRepository {
    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList>

    suspend fun getPokemonDetail(pokemonName: String): Resource<PokemonDetail>
}