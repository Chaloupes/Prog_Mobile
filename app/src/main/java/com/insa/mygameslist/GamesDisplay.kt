package com.insa.mygameslist

import android.graphics.drawable.Icon
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.insa.mygameslist.data.Game
import com.insa.mygameslist.data.IGDB
import kotlin.math.roundToInt


class GameViewModel(val data: IGDB) : ViewModel() {
    var isSearchActive by mutableStateOf(false)
    var searchQuery by mutableStateOf("")

    private val allGames = data.games.values.toList()

    val filteredGames: List<Game>
        get() {
            return if (searchQuery.isBlank()) {
                allGames
            } else {
                allGames.filter { game ->
                    val matchName = game.name.startsWith(searchQuery, ignoreCase = true)
                    val matchGenre = game.genres.any { genreId ->
                        data.genres[genreId]?.name?.startsWith(searchQuery, ignoreCase = true) == true
                    }
                    val matchPlatform = game.platforms.any { platformId ->
                        data.platforms[platformId]?.name?.startsWith(searchQuery, ignoreCase = true) == true
                    }
                    matchName || matchGenre || matchPlatform
                }
            }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListGameCell(data: IGDB, viewModel: GameViewModel = viewModel { GameViewModel(data) }) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            Crossfade(
                modifier = Modifier.animateContentSize(),
                targetState = viewModel.isSearchActive,
                label = "SearchAnim"
            ) { searching ->
                if (!searching) {
                    TopAppBar(
                        title = { Text("My Games List") },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(71, 107, 27),
                            titleContentColor = Color(228, 215, 183),
                        ),
                        actions = {
                            IconButton(onClick = { viewModel.isSearchActive = true }) {
                                Icon(painterResource(R.drawable.loupe), "Search", tint = Color.Unspecified)
                            }
                        },
                        scrollBehavior = scrollBehavior,
                    )
                } else {
                    SearchBar(
                        modifier = Modifier.fillMaxWidth(),
                        query = viewModel.searchQuery,
                        onQueryChange = { viewModel.searchQuery = it },
                        onSearch = { },
                        active = viewModel.isSearchActive,
                        onActiveChange = { viewModel.isSearchActive = it },
                        placeholder = { Text("Search game") },
                        leadingIcon = {
                            IconButton(onClick = {
                                viewModel.isSearchActive = false
                                viewModel.searchQuery = ""
                            }) {
                                Icon(painterResource(R.drawable.loupe), "Search", tint = Color.Unspecified)
                            }
                        }
                    ) {
                        GameListContent(
                            games = viewModel.filteredGames,
                            data = data,
                        )
                    }
                }
            }
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        if (!viewModel.isSearchActive) {
            GameListContent(
                games = viewModel.filteredGames,
                data = data,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun GameCell(jeu: Game, data: IGDB){
    val cover = data.covers[jeu.cover]?.url
    val genres = jeu.genres.mapNotNull { data.genres[it]?.name }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(228, 215, 183), RoundedCornerShape(20.dp))
            .padding(8.dp)
            .clickable { backStack.add(jeu)}
    ) {
        AsyncImage(
            model = cover?.let { "https:$it" },
            contentDescription = "Image de jeu",
            modifier = Modifier
                .height(70.dp)
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = jeu.name,
                style = MaterialTheme.typography.titleMedium,
                color=Color(58,86,22),
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Genres : ${genres.joinToString(", ")}",
                color=Color(141,109,72),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Box(
            modifier = Modifier.width(50.dp).height(70.dp),
            contentAlignment = Alignment.Center
        ) {
            if(data.favoris[jeu.id] == false) {
                Icon(
                    painter = painterResource(R.drawable.etoile_vide),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(32.dp)
                )
            }else{
                Icon(
                    painter = painterResource(R.drawable.etoile_pleine),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun GameListContent(
    games: List<Game>,
    data: IGDB,
    modifier: Modifier = Modifier
) {
    if (games.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(176, 184, 62)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No match :(",
                style = MaterialTheme.typography.headlineMedium,
                color = Color(58, 86, 22),
                fontWeight = FontWeight.Bold
            )
        }
    } else {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .background(Color(176, 184, 62)),
        ) {
            items(games) { jeu ->
                Spacer(modifier = Modifier.height(4.dp))
                GameCell(jeu = jeu, data = data)
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Preview
@Composable
fun GameCellPreview(){
}