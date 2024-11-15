package com.wongislandd.infinityindex.infra.viewmodels

import androidx.paging.PagingData
import com.wongislandd.infinityindex.entities.characters.models.Character
import com.wongislandd.infinityindex.entities.comics.details.models.Comic
import com.wongislandd.infinityindex.entities.creators.models.Creator
import com.wongislandd.infinityindex.entities.events.models.Event
import com.wongislandd.infinityindex.entities.series.models.Series
import com.wongislandd.infinityindex.entities.stories.models.Story
import com.wongislandd.infinityindex.infra.DetailsBackChannelEvent
import com.wongislandd.infinityindex.infra.ListBackChannelEvent
import com.wongislandd.infinityindex.infra.util.EntityType
import com.wongislandd.infinityindex.infra.util.ViewModelSlice
import com.wongislandd.infinityindex.infra.util.events.BackChannelEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseEntityPagingDataConsumerSlice : ViewModelSlice() {

    protected val entityCountsData: MutableStateFlow<EntityCountsData> =
        MutableStateFlow(EntityCountsData())

    protected val characterPagingData: MutableStateFlow<PagingData<Character>> =
        MutableStateFlow(PagingData.empty())

    protected val creatorsPagingData: MutableStateFlow<PagingData<Creator>> =
        MutableStateFlow(PagingData.empty())


    protected val eventsPagingData: MutableStateFlow<PagingData<Event>> =
        MutableStateFlow(PagingData.empty())


    protected val storiesPagingData: MutableStateFlow<PagingData<Story>> =
        MutableStateFlow(PagingData.empty())


    protected val seriesPagingData: MutableStateFlow<PagingData<Series>> =
        MutableStateFlow(PagingData.empty())


    protected val comicPagingData: MutableStateFlow<PagingData<Comic>> =
        MutableStateFlow(PagingData.empty())


    override fun handleBackChannelEvent(event: BackChannelEvent) {
        when (event) {
            is ListBackChannelEvent.PagingDataResUpdate<*> -> {
                handlePagingUpdate(event)
            }
            is ListBackChannelEvent.EntityCountsUpdate -> {
                handleEntityCountsUpdate(event)
            }
        }
    }

    /**
     * Find a better way around the genericness here, so that you don't need an unchecked cast.
     * Although this is kind of safe, as long as the right entity type is passed!
     */
    @Suppress("UNCHECKED_CAST")
    private fun handlePagingUpdate(event: ListBackChannelEvent.PagingDataResUpdate<*>) {
        when (event.entityType) {
            EntityType.CHARACTERS -> {
                characterPagingData.value = event.update as PagingData<Character>
            }

            EntityType.EVENTS -> {
                eventsPagingData.value = event.update as PagingData<Event>
            }

            EntityType.CREATORS -> {
                creatorsPagingData.value = event.update as PagingData<Creator>
            }

            EntityType.STORIES -> {
                storiesPagingData.value = event.update as PagingData<Story>
            }

            EntityType.COMICS -> {
                comicPagingData.value = event.update as PagingData<Comic>
            }

            EntityType.SERIES -> {
                seriesPagingData.value = event.update as PagingData<Series>
            }
        }
    }

    private fun handleEntityCountsUpdate(event: ListBackChannelEvent.EntityCountsUpdate) {
        when (event.entityType) {
            EntityType.CHARACTERS -> {
                entityCountsData.update {
                    it.copy(
                        charactersCount = event.totalCount
                    )
                }
            }
            EntityType.EVENTS -> {
                entityCountsData.update {
                    it.copy(
                        eventsCount = event.totalCount
                    )
                }
            }
            EntityType.CREATORS -> {
                entityCountsData.update {
                    it.copy(
                        creatorsCount = event.totalCount
                    )
                }
            }
            EntityType.STORIES -> {
                entityCountsData.update {
                    it.copy(
                        storiesCount = event.totalCount
                    )
                }
            }
            EntityType.COMICS -> {
                entityCountsData.update {
                    it.copy(
                        comicCount = event.totalCount
                    )
                }
            }
            EntityType.SERIES -> {
                entityCountsData.update {
                    it.copy(
                        seriesCount = event.totalCount
                    )
                }
            }
        }
    }
}