package gardaoto.projects.pokemondex.features.pokemondetail

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import gardaoto.projects.pokemondex.frameworks.model.PokemonDetail
import gardaoto.projects.pokemondex.util.Resource
import gardaoto.projects.pokemondex.util.parseStatToAbbr
import gardaoto.projects.pokemondex.util.parseStatToColor
import gardaoto.projects.pokemondex.util.parseTypeToColor

@Composable
fun PokemonDetailScreen(
    dominantColor: Color,
    pokemonName: String,
    navController: NavController,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val pokemonDetail = produceState<Resource<PokemonDetail>>(initialValue = Resource.Loading()) {
        value = viewModel.getPokemonDetail(pokemonName = pokemonName)
    }.value

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(dominantColor)
            .padding(bottom = 16.dp)
    ) {
        PokemonTopDetail(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )

        PokemonDetailState(
            pokemonDetail = pokemonDetail,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 20.dp + 200.dp / 2f, start = 16.dp, end = 16.dp, bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(20.dp))
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .padding(
                    top = 20.dp + 200.dp / 2f, start = 16.dp, end = 16.dp, bottom = 16.dp
                )
        )
        Box(
            contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()
        ) {
            if (pokemonDetail is Resource.Success) {
                pokemonDetail.data?.sprites.let {
                    AsyncImage(
                        model = it?.frontDefault,
                        contentDescription = pokemonDetail.data?.name,
                        modifier = Modifier
                            .size(180.dp)
                            .offset(y = 20.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonTopDetail(
    navController: NavController, modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart, modifier = modifier.background(
            Brush.verticalGradient(
                listOf(
                    Color.White, Color.Transparent
                )
            )
        )
    ) {
        Icon(imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 32.dp)
                .clickable {
                    navController.popBackStack()
                })
    }
}

@Composable
fun PokemonDetailState(
    pokemonDetail: Resource<PokemonDetail>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {
    when (pokemonDetail) {
        is Resource.Success -> {
            pokemonDetail.data?.let {
                PokemonDetailSection(
                    pokemonDetail = it, modifier = modifier.offset(y = (-20).dp)
                )
            }
        }

        is Resource.Error -> {
            Text(
                text = pokemonDetail.message!!, color = Color.Red, modifier = modifier
            )
        }

        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary, modifier = loadingModifier
            )
        }
    }
}

@Composable
fun PokemonDetailSection(
    pokemonDetail: PokemonDetail, modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 80.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "#${pokemonDetail.id} ${pokemonDetail.name?.replaceFirstChar(Char::titlecase)}",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        PokemonTypeSection(types = pokemonDetail.types!!)
        PokemonDetailDataSection(
            pokemonWeight = pokemonDetail.weight!!, pokemonHeight = pokemonDetail.height!!
        )
        PokemonAbilitiesSection(abilities = pokemonDetail.abilities!!)
        PokemonBaseStats(pokemonDetail = pokemonDetail)
    }
}

@Composable
fun PokemonTypeSection(types: List<PokemonDetail.Type>) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)
    ) {
        for (type in types) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(parseTypeToColor(type))
                    .height(35.dp)
            ) {
                Text(
                    text = type.type?.name!!.replaceFirstChar(Char::titlecase),
                    color = Color.White,
                    fontSize = 18.sp,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun PokemonAbilitiesSection(abilities: List<PokemonDetail.Ability>) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Abilities",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 4.dp),
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
        ) {

            for (ability in abilities) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.onSurfaceVariant)
                        .height(35.dp)
                        .fillMaxHeight()
                ) {
                    Text(
                        text = ability.ability?.name!!.replaceFirstChar(Char::titlecase),
                        color = Color.White,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .align(Alignment.Center),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun PokemonDetailDataSection(
    pokemonWeight: Int, pokemonHeight: Int, sectionHeight: Dp = 50.dp
) {
    val pokemonWeightInKg = remember {
        StrictMath.round(pokemonWeight * 100f) / 1000f
    }
    val pokemonHeightInMeters = remember {
        StrictMath.round(pokemonHeight * 100f) / 1000f
    }
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        PokemonDetailDataItem(
            dataValue = pokemonWeightInKg,
            dataUnit = " Kilograms",
            weightText = "Weight",
            modifier = Modifier.weight(1f)
        )
        Spacer(
            modifier = Modifier
                .size(2.dp, sectionHeight)
                .background(Color.LightGray)
        )
        PokemonDetailDataItem(
            dataValue = pokemonHeightInMeters,
            dataUnit = " Meters",
            weightText = "Height",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PokemonDetailDataItem(
    dataValue: Float, dataUnit: String, weightText: String, modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$dataValue$dataUnit", color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = weightText,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.SemiBold

        )
    }
}

@Composable
fun PokemonStat(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    height: Dp = 28.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            statValue / statMaxValue.toFloat()
        } else {
            0f
        }, animationSpec = tween(animDuration, animDelay), label = ""
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(
                if (isSystemInDarkTheme()) {
                    Color(0xFF505050)
                } else {
                    Color.LightGray
                }
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(curPercent.value)
                .fillMaxHeight()
                .clip(CircleShape)
                .background(statColor)
                .padding(horizontal = 8.dp)
        ) {

            Text(
                text = statName, fontWeight = FontWeight.Bold
            )
            Text(
                text = (curPercent.value * statValue).toString(), fontWeight = FontWeight.Bold
            )
        }

    }

}

@SuppressLint("RememberReturnType")
@Composable
fun PokemonBaseStats(
    pokemonDetail: PokemonDetail,
    animDelayPerItem: Int = 100,
) {

    val maxBaseStat = remember {
        pokemonDetail.stats?.maxOf { it.baseStat }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Base Stats",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(12.dp))


        for (i in pokemonDetail.stats!!.indices) {
            val stat = pokemonDetail.stats[i]

            PokemonStat(
                statName = parseStatToAbbr(stat.stat!!),
                statValue = stat.baseStat,
                statMaxValue = maxBaseStat!!,
                statColor = parseStatToColor(stat.stat),
                animDelay = i * animDelayPerItem
            )
            Spacer(modifier = Modifier.height(8.dp))

        }

    }
}