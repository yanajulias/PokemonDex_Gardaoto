package gardaoto.projects.pokemondex.util

import androidx.compose.ui.graphics.Color
import gardaoto.projects.pokemondex.frameworks.model.PokemonDetail
import gardaoto.projects.pokemondex.ui.theme.AtkColor
import gardaoto.projects.pokemondex.ui.theme.DefColor
import gardaoto.projects.pokemondex.ui.theme.HPColor
import gardaoto.projects.pokemondex.ui.theme.SpAtkColor
import gardaoto.projects.pokemondex.ui.theme.SpDefColor
import gardaoto.projects.pokemondex.ui.theme.SpdColor
import gardaoto.projects.pokemondex.ui.theme.TypeBug
import gardaoto.projects.pokemondex.ui.theme.TypeDark
import gardaoto.projects.pokemondex.ui.theme.TypeDragon
import gardaoto.projects.pokemondex.ui.theme.TypeElectric
import gardaoto.projects.pokemondex.ui.theme.TypeFairy
import gardaoto.projects.pokemondex.ui.theme.TypeFighting
import gardaoto.projects.pokemondex.ui.theme.TypeFire
import gardaoto.projects.pokemondex.ui.theme.TypeFlying
import gardaoto.projects.pokemondex.ui.theme.TypeGhost
import gardaoto.projects.pokemondex.ui.theme.TypeGrass
import gardaoto.projects.pokemondex.ui.theme.TypeGround
import gardaoto.projects.pokemondex.ui.theme.TypeIce
import gardaoto.projects.pokemondex.ui.theme.TypeNormal
import gardaoto.projects.pokemondex.ui.theme.TypePoison
import gardaoto.projects.pokemondex.ui.theme.TypePsychic
import gardaoto.projects.pokemondex.ui.theme.TypeRock
import gardaoto.projects.pokemondex.ui.theme.TypeSteel
import gardaoto.projects.pokemondex.ui.theme.TypeWater

fun parseTypeToColor(type: PokemonDetail.Type): Color {
    return when(type.type?.name?.replaceFirstChar(Char::lowercase)) {
        "normal" -> TypeNormal
        "fire" -> TypeFire
        "water" -> TypeWater
        "electric" -> TypeElectric
        "grass" -> TypeGrass
        "ice" -> TypeIce
        "fighting" -> TypeFighting
        "poison" -> TypePoison
        "ground" -> TypeGround
        "flying" -> TypeFlying
        "psychic" -> TypePsychic
        "bug" -> TypeBug
        "rock" -> TypeRock
        "ghost" -> TypeGhost
        "dragon" -> TypeDragon
        "dark" -> TypeDark
        "steel" -> TypeSteel
        "fairy" -> TypeFairy
        else -> Color.Black
    }
}

fun parseStatToColor(stat: PokemonDetail.Stat.Stat): Color {
    return when(stat.name?.replaceFirstChar(Char::lowercase)) {
        "hp" -> HPColor
        "attack" -> AtkColor
        "defense" -> DefColor
        "special-attack" -> SpAtkColor
        "special-defense" -> SpDefColor
        "speed" -> SpdColor
        else -> Color.White
    }
}

fun parseStatToAbbr(stat: PokemonDetail.Stat.Stat): String {
    return when(stat.name) {
        "hp" -> "HP"
        "attack" -> "Atk"
        "defense" -> "Def"
        "special-attack" -> "SpAtk"
        "special-defense" -> "SpDef"
        "speed" -> "Spd"
        else -> ""
    }
}