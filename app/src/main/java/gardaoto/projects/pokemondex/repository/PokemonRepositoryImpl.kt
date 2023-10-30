package gardaoto.projects.pokemondex.repository

import gardaoto.projects.pokemondex.frameworks.PokemonApi
import gardaoto.projects.pokemondex.frameworks.model.PokemonDetail
import gardaoto.projects.pokemondex.frameworks.model.PokemonList
import gardaoto.projects.pokemondex.util.Resource
import javax.inject.Inject

class PokemonRepositoryImpl @Inject constructor(
    private val pokeApi: PokemonApi
) : PokemonRepository {
    override suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        val response = try {
            pokeApi.getPokemonList(limit, offset)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occured ")
        }
        return Resource.Success(response)
    }

    override suspend fun getPokemonDetail(pokemonName: String): Resource<PokemonDetail> {
        val response = try {
            pokeApi.getPokemonDetail(pokemonName)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occured ")
        }
        return Resource.Success(response)
    }
}