package gardaoto.projects.pokemondex.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import gardaoto.projects.pokemondex.frameworks.PokemonApi
import gardaoto.projects.pokemondex.repository.PokemonRepository
import gardaoto.projects.pokemondex.repository.PokemonRepositoryImpl
import gardaoto.projects.pokemondex.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePokemonRepository(api: PokemonApi) : PokemonRepository = PokemonRepositoryImpl(api)

    @Singleton
    @Provides
    fun providePokeApi(): PokemonApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
            .build()
            .create(PokemonApi::class.java)
    }
}