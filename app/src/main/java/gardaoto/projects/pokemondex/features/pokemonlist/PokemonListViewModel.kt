package gardaoto.projects.pokemondex.features.pokemonlist

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.palette.graphics.Palette
import dagger.hilt.android.lifecycle.HiltViewModel
import gardaoto.projects.pokemondex.domain.Pokemon
import gardaoto.projects.pokemondex.repository.PokemonRepository
import gardaoto.projects.pokemondex.util.Constants.PAGE_SIZE
import gardaoto.projects.pokemondex.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    private var curPage = 0

    var pokemonList = mutableStateOf<List<Pokemon>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var cachedPokemonList = listOf<Pokemon>()
    private var isSearchStarting = true
    var isSearching = mutableStateOf(false)

    init {
        loadPokemonList()
    }

    fun dominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bitmap = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bitmap).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }

    fun loadPokemonList() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getPokemonList(PAGE_SIZE, curPage * PAGE_SIZE)
            when (result) {
                is Resource.Success -> {
                    endReached.value = curPage * PAGE_SIZE >= result.data!!.count!!
                    val pokemonItems = result.data.results.mapIndexed { index, pokemon ->
                        val number = if (pokemon?.url?.endsWith("/") == true) {
                            pokemon.url.dropLast(1).takeLastWhile { it.isDigit() }
                        } else {
                            pokemon?.url?.takeLastWhile { it.isDigit() }
                        }

                        val url =
                            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${number}.png"
                        Pokemon(
                            pokemon?.name!!.replaceFirstChar(Char::titlecase),
                            url,
                            number!!.toInt()
                        )
                    }

                    curPage++
                    loadError.value = ""
                    isLoading.value = false
                    pokemonList.value += pokemonItems
                }

                is Resource.Error -> {
                    loadError.value = result.message!!
                    isLoading.value = false
                }

                else -> {}
            }
        }
    }

    fun searchPokemonList(query: String) {
        val listToSearch = if(isSearchStarting) {
            pokemonList.value
        } else {
            cachedPokemonList
        }
        viewModelScope.launch(Dispatchers.Default) {
            if(query.isEmpty()) {
                pokemonList.value = cachedPokemonList
                isSearching.value = false
                isSearchStarting = true
                return@launch
            }
            val results = listToSearch.filter {
                it.pokemonName.contains(query.trim(), ignoreCase = true) ||
                        it.number.toString() == query.trim()
            }
            if(isSearchStarting) {
                cachedPokemonList = pokemonList.value
                isSearchStarting = false
            }
            pokemonList.value = results
            isSearching.value = true
        }
    }

}