package com.insa.mygameslist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.insa.mygameslist.data.Game
import com.insa.mygameslist.data.IGDB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListGameCell(data : IGDB){
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color(71,107,27),
                    titleContentColor = Color(228,215,183),
                ),
                title = { Text("My Games List") })
        },
        contentWindowInsets = WindowInsets.systemBars,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .background(Color(176, 184, 62)),
        ) {
            items(data.games.values.toList()) { jeu ->
                Spacer(modifier = Modifier.height(4.dp))
                GameCell(jeu = jeu, data = data)
                Spacer(modifier = Modifier.height(4.dp))
            }
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
        Column {
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
    }
}

@Preview
@Composable
fun GameCellPreview(){
}