package com.wongislandd.infinityindex.entities.series.data

import com.wongislandd.infinityindex.entities.series.models.NetworkSeries
import com.wongislandd.infinityindex.entities.series.models.Series
import com.wongislandd.infinityindex.entities.series.transformers.SeriesTransformer
import com.wongislandd.infinityindex.infra.paging.BaseRepository
import com.wongislandd.infinityindex.infra.util.SupportedPillars
import io.ktor.client.HttpClient

class SeriesEntityRepository(
    seriesTransformer: SeriesTransformer,
    okHttpClient: HttpClient
) : BaseRepository<NetworkSeries, Series>(
    seriesTransformer,
    okHttpClient,
    SupportedPillars.SERIES,
    NetworkSeries.serializer()
)