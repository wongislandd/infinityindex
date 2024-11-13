package com.wongislandd.infinityindex.pillars.comics.details.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import com.wongislandd.infinityindex.GlobalTopAppBar
import com.wongislandd.infinityindex.composables.MarvelImage
import com.wongislandd.infinityindex.composables.SectionedList
import com.wongislandd.infinityindex.networking.util.Resource
import com.wongislandd.infinityindex.pillars.comics.details.models.Comic
import com.wongislandd.infinityindex.pillars.comics.details.viewmodels.ComicDetailsViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
fun ComicDetailsScreen(
    comicId: Int
) {
    val viewModel = koinViewModel<ComicDetailsViewModel>()
    LaunchedEffect(Unit) {
        viewModel.uiEventBus.sendEvent(ComicDetailsUiEvent.PageInitialized(comicId))
    }
    val screenState by viewModel.comicDetailsScreenStateSlice.screenState.collectAsState()
    val pagedCharacters = screenState.characterData.collectAsLazyPagingItems()
    val pagedCreators = screenState.creatorsData.collectAsLazyPagingItems()
    val pagedEvents = screenState.eventsData.collectAsLazyPagingItems()
    val pagedStories = screenState.storiesData.collectAsLazyPagingItems()
    val pagedSeries = screenState.seriesData.collectAsLazyPagingItems()
    Scaffold(topBar = {
        GlobalTopAppBar()
    }) {
        Box(modifier = Modifier.fillMaxSize()) {
            when (val comicRes = screenState.comicRes) {
                is Resource.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        item {
                            ComicDetails(
                                comicRes.data,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        item {
                            SectionedList(
                                title = "Characters",
                                pagedItems = pagedCharacters,
                            )
                        }
                        item {
                            SectionedList(
                                title = "Creators",
                                pagedItems = pagedCreators,
                            )
                        }
                        item {
                            SectionedList(
                                title = "Events",
                                pagedItems = pagedEvents,
                            )
                        }
                        item {
                            SectionedList(
                                title = "Stories",
                                pagedItems = pagedStories,
                            )
                        }
                        item {
                            SectionedList(
                                title = "Series",
                                pagedItems = pagedSeries,
                            )
                        }
                    }
                }

                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is Resource.Error -> {
                    Text(
                        text = "Error loading comic details",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
private fun ComicDetails(comic: Comic, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MarvelImage(imageUrl = comic.imageUrl, modifier = Modifier.fillMaxWidth())
        Text(
            text = comic.title,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}