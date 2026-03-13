package com.insa.mygameslist.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.insa.mygameslist.R
import java.util.HashMap

object IGDB {

    lateinit var covers: Map<Long,Cover>
    lateinit var games: Map<Long,Game>
    lateinit var genres: Map<Long,Genre>
    lateinit var platformLogos : Map<Long,PlatformLogo>
    lateinit var platforms : Map<Long,Platform>

    lateinit var favoris : Map<Long,Boolean>

    fun load(context: Context) {
        val coversFromJson: List<Cover> = Gson().fromJson(
            context.resources.openRawResource(R.raw.covers).bufferedReader(),
            object : TypeToken<List<Cover>>() {}.type
        )

        val gamesFromJson : List<Game> = Gson().fromJson(
            context.resources.openRawResource(R.raw.games).bufferedReader(),
            object : TypeToken<List<Game>>() {}.type
        )

        val genresFromJson : List<Genre> = Gson().fromJson(
            context.resources.openRawResource(R.raw.genres).bufferedReader(),
            object : TypeToken<List<Genre>>() {}.type
        )

        val platformLogosFromJson : List<PlatformLogo> = Gson().fromJson(
            context.resources.openRawResource(R.raw.platform_logos).bufferedReader(),
            object : TypeToken<List<PlatformLogo>>() {}.type
        )

        val platformsFromJson : List<Platform> = Gson().fromJson(
            context.resources.openRawResource(R.raw.platforms).bufferedReader(),
            object : TypeToken<List<Platform>>() {}.type
        )

        covers = coversFromJson.associateBy{ it.id }
        games = gamesFromJson.associateBy{ it.id }
        genres = genresFromJson.associateBy { it.id }
        platformLogos = platformLogosFromJson.associateBy { it.id }
        platforms = platformsFromJson.associateBy { it.id }

        favoris = games.keys.associateWith { false }.toMutableMap()
    }
}

data class Cover(val id: Long, val url: String)
data class Game(val id: Long, val cover : Long, val firstReleaseDate : Long, val genres : List<Long>, val name : String, val platforms : List<Long>, val summary : String, val totalRating : Double)
data class Genre(val id: Long, val name : String)
data class PlatformLogo(val id: Long, val url: String)
data class Platform(val id: Long, val name: String, val platform_logo : Long)