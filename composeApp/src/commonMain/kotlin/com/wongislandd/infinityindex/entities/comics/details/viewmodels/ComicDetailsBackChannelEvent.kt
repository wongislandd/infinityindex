package com.wongislandd.infinityindex.entities.comics.details.viewmodels

import androidx.paging.PagingData
import com.wongislandd.infinityindex.infra.util.EntityType
import com.wongislandd.infinityindex.infra.util.PillarModel
import com.wongislandd.infinityindex.infra.util.Resource
import com.wongislandd.infinityindex.infra.util.events.BackChannelEvent

sealed class ComicDetailsBackChannelEvent : BackChannelEvent {
    data class PagingDataResUpdate<T : PillarModel>(
        val update: PagingData<T>,
        val type: EntityType
    ) :
        BackChannelEvent

    data class SingleDataResUpdate<T : PillarModel>(val update: Resource<T>, val type: EntityType) :
        BackChannelEvent

}