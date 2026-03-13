package com.insa.mygameslist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.insa.mygameslist.data.Game
import com.insa.mygameslist.data.IGDB

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetails(jeu: Game, data: IGDB){
    val cover = data.covers[jeu.cover]?.url
    val genres = jeu.genres.mapNotNull { data.genres[it]?.name }
    val platformLogosIds = jeu.platforms.mapNotNull { data.platforms[it]?.platform_logo }
    val platformLogosUrls = platformLogosIds.mapNotNull { data.platformLogos[it]?.url }
    val isFavori = data.favoris[jeu.id] ?: false
    val resource = if (isFavori) R.drawable.etoile_pleine else R.drawable.etoile_vide
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = Color(71,107,27),
                    titleContentColor = Color(228,215,183),
                ),
                title = { Text(jeu.name) },
                navigationIcon = {
                    IconButton(onClick = {backStack.removeLastOrNull()}) {
                        Icon(painter = painterResource(R.drawable.baseline_arrow_back_24), contentDescription = "retour")
                    }
                }
            )
        },
        contentWindowInsets = WindowInsets.systemBars,
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Row(
            modifier = Modifier.padding(innerPadding).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(30.dp))
                Row(){
                    Text(
                        text = jeu.name,
                        style = TextStyle(Color.Black,fontSize=24.sp),
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    Box(
                        modifier = Modifier
                            .width(32.dp)
                            .height(32.dp)
                            .clickable{
                                data.favoris[jeu.id] = !isFavori
                            },
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Icon(
                            painter = painterResource(resource),
                            contentDescription = null,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                AsyncImage(
                    model = cover?.let { "https:$it" },
                    contentDescription = "Image de jeu",
                    modifier = Modifier.height(200.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))
                Text(
                    text = genres.joinToString(", "),
                    style = TextStyle(Color.Black,fontSize = 12.sp,fontStyle = FontStyle.Italic),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(20.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(platformLogosUrls) { url ->
                        AsyncImage(
                            model = "https:$url",
                            contentDescription = "Platform logo",
                        )
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = jeu.summary,
                    modifier = Modifier.padding(10.dp).verticalScroll(rememberScrollState()),
                    color = Color.Black
                )
            }
        }
    }
}
