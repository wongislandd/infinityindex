package com.wongislandd.infinityindex.entities.comics.details.di

import com.wongislandd.infinityindex.entities.comics.details.helpers.NetworkFieldTypeMapper
import com.wongislandd.infinityindex.entities.comics.details.transformers.DetailedComicTransformer
import com.wongislandd.infinityindex.entities.comics.details.transformers.RelatedDatesTransformer
import com.wongislandd.infinityindex.entities.comics.details.transformers.RelatedLinksTransformer
import com.wongislandd.infinityindex.entities.comics.details.transformers.RelatedPricesTransformer
import com.wongislandd.infinityindex.entities.comics.details.transformers.RelatedTextsTransformer
import com.wongislandd.infinityindex.entities.comics.details.ui.ComicDetailsUiEvent
import com.wongislandd.infinityindex.entities.comics.details.viewmodels.ComicDetailsBackChannelEvent
import com.wongislandd.infinityindex.entities.comics.details.viewmodels.ComicDetailsCharactersResolutionSlice
import com.wongislandd.infinityindex.entities.comics.details.viewmodels.ComicDetailsCreatorsResolutionSlice
import com.wongislandd.infinityindex.entities.comics.details.viewmodels.ComicDetailsEventsResolutionSlice
import com.wongislandd.infinityindex.entities.comics.details.viewmodels.ComicDetailsScreenStateSlice
import com.wongislandd.infinityindex.entities.comics.details.viewmodels.ComicDetailsSeriesResolutionSlice
import com.wongislandd.infinityindex.entities.comics.details.viewmodels.ComicDetailsResolutionSlice
import com.wongislandd.infinityindex.entities.comics.details.viewmodels.ComicDetailsStoriesResolutionSlice
import com.wongislandd.infinityindex.entities.comics.details.viewmodels.ComicDetailsViewModel
import com.wongislandd.infinityindex.infra.util.events.eventBusFactory
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val comicDetailsModule = module {
    eventBusFactory<ComicDetailsUiEvent>()
    eventBusFactory<ComicDetailsBackChannelEvent>()
    singleOf(::RelatedDatesTransformer)
    singleOf(::RelatedLinksTransformer)
    singleOf(::RelatedPricesTransformer)
    singleOf(::RelatedTextsTransformer)
    singleOf(::DetailedComicTransformer)
    singleOf(::NetworkFieldTypeMapper)
    singleOf(::RelatedDatesTransformer)
    factoryOf(::ComicDetailsResolutionSlice)
    factoryOf(::ComicDetailsSeriesResolutionSlice)
    factoryOf(::ComicDetailsScreenStateSlice)
    factoryOf(::ComicDetailsCharactersResolutionSlice)
    factoryOf(::ComicDetailsCreatorsResolutionSlice)
    factoryOf(::ComicDetailsEventsResolutionSlice)
    factoryOf(::ComicDetailsStoriesResolutionSlice)
    viewModelOf(::ComicDetailsViewModel)
}
