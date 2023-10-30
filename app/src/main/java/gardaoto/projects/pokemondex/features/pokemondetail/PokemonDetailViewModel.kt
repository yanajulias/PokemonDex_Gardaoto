package gardaoto.projects.pokemondex.features.pokemondetail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import gardaoto.projects.pokemondex.frameworks.model.PokemonDetail
import gardaoto.projects.pokemondex.repository.PokemonRepository
import gardaoto.projects.pokemondex.util.Resource
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {
    suspend fun getPokemonDetail(pokemonName: String): Resource<PokemonDetail> =
        repository.getPokemonDetail(pokemonName)
}