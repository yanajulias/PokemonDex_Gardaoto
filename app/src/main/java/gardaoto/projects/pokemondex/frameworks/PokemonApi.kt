package gardaoto.projects.pokemondex.frameworks

import gardaoto.projects.pokemondex.frameworks.model.PokemonDetail
import gardaoto.projects.pokemondex.frameworks.model.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonApi {
    @GET("pokemon")
    suspend fun getPokemonList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): PokemonList

    @GET("pokemon/{name}")
    suspend fun getPokemonDetail(
        @Path("name") name: String,
    ): PokemonDetail
}