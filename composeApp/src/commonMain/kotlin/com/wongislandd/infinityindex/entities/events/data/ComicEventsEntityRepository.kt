package com.wongislandd.infinityindex.entities.events.data

import com.wongislandd.infinityindex.entities.events.models.ComicEvent
import com.wongislandd.infinityindex.entities.events.models.NetworkComicEvent
import com.wongislandd.infinityindex.entities.events.transformers.EventTransformer
import com.wongislandd.infinityindex.infra.paging.BaseRepository
import com.wongislandd.infinityindex.infra.util.EntityType
import io.ktor.client.HttpClient

class ComicEventsEntityRepository(
    eventTransformer: EventTransformer,
    okHttpClient: HttpClient,
) : BaseRepository<NetworkComicEvent, ComicEvent>(
    eventTransformer,
    okHttpClient,
    EntityType.COMIC_EVENTS,
    NetworkComicEvent.serializer()
)