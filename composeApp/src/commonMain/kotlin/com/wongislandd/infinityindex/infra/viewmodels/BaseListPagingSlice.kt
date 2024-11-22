package com.wongislandd.infinityindex.infra.viewmodels

import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import app.cash.paging.Pager
import com.wongislandd.infinityindex.ComicConstants
import com.wongislandd.infinityindex.infra.DetailsBackChannelEvent
import com.wongislandd.infinityindex.infra.DetailsUiEvent
import com.wongislandd.infinityindex.infra.PagingBackChannelEvent
import com.wongislandd.infinityindex.infra.networking.models.DataWrapper
import com.wongislandd.infinityindex.infra.paging.BasePagingSource
import com.wongislandd.infinityindex.infra.paging.BaseRepository
import com.wongislandd.infinityindex.infra.paging.EntityPagingSource
import com.wongislandd.infinityindex.infra.paging.PaginationContextWrapper
import com.wongislandd.infinityindex.infra.paging.PagingSourceCallbacks
import com.wongislandd.infinityindex.infra.paging.RelatedEntityPagingSource
import com.wongislandd.infinityindex.infra.util.Empty
import com.wongislandd.infinityindex.infra.util.EntityModel
import com.wongislandd.infinityindex.infra.util.EntityType
import com.wongislandd.infinityindex.infra.util.Resource
import com.wongislandd.infinityindex.infra.util.SortOption
import com.wongislandd.infinityindex.infra.util.ViewModelSlice
import com.wongislandd.infinityindex.infra.util.events.BackChannelEvent
import com.wongislandd.infinityindex.infra.util.events.UiEvent
import com.wongislandd.infinityindex.infra.util.getDefaultSortOption
import com.wongislandd.infinityindex.infra.util.safeLet
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * General paging list slice
 */
enum class PagedListUseCase {
    // I'm looking for comics
    ALL_AVAILABLE,

    // I'm looking for comics that are related to characters
    RELATED_ENTITIES
}

abstract class BaseListPagingSlice<NETWORK_TYPE, LOCAL_TYPE : EntityModel>(
    private val repository: BaseRepository<NETWORK_TYPE, LOCAL_TYPE>,
    private val entityType: EntityType,
    private val useCase: PagedListUseCase
) : ViewModelSlice() {

    private var currentPagingSource: BasePagingSource<LOCAL_TYPE>? = null
    private var currentSearchQuery: String? = null
    private var currentSortOption: SortOption? = entityType.getDefaultSortOption()
    private var currentDigitalAvailabilityFilterEnabled: Boolean = false
    private var currentVariantsEnabled: Boolean = false
    private var pagingConfig: PagingConfig = getDefaultPagingConfig()
    private var maxPageLimit: Int? = null

    override fun afterInit() {
        // If this is a list for a root entity, we don't need to wait on anything
        if (useCase == PagedListUseCase.ALL_AVAILABLE) {
            initializePaging()
        }
    }

    private fun getPagingSource(
        relatedEntityType: EntityType?,
        relatedEntityId: Int?,
        sortKeyOverride: String? = null
    ): BasePagingSource<LOCAL_TYPE> {
        return when (useCase) {
            PagedListUseCase.ALL_AVAILABLE -> {
                EntityPagingSource(
                    repository = repository,
                    searchQuery = currentSearchQuery,
                    sortKey = sortKeyOverride ?: currentSortOption?.sortKey,
                    digitalAvailabilityFilterEnabled =  currentDigitalAvailabilityFilterEnabled,
                    isVariantsEnabled = currentVariantsEnabled
                )
            }

            PagedListUseCase.RELATED_ENTITIES -> {
                safeLet(relatedEntityType, relatedEntityId) { relatedType, relatedId ->
                    RelatedEntityPagingSource(
                        repository,
                        relatedType,
                        relatedId,
                        currentSearchQuery,
                        sortKeyOverride ?: currentSortOption?.sortKey,
                        currentDigitalAvailabilityFilterEnabled,
                        currentVariantsEnabled
                    )
                }
                    ?: throw IllegalArgumentException("Attempted to page related entities before providing a related entity type and id")
            }
        }
    }

    private fun initializePaging(
        relatedEntityType: EntityType? = null,
        relatedEntityId: Int? = null,
        sortKeyOverride: String? = null
    ) {
        sliceScope.launch {
            Pager(
                config = pagingConfig
            ) {
                val newPagingSource = getPagingSource(
                    relatedEntityType,
                    relatedEntityId,
                    sortKeyOverride
                ).also { currentPagingSource = it }
                newPagingSource.apply {
                    maxPageLimit?.also {
                        setMaxNumberOfPages(it)
                    }
                    registerCallbacks(object : PagingSourceCallbacks {
                        override fun onResponse(response: Resource<DataWrapper<*>>) {

                        }

                        override fun onSuccess(paginationContextWrapper: PaginationContextWrapper<*>) {
                            sliceScope.launch {
                                backChannelEvents.sendEvent(
                                    PagingBackChannelEvent.EntityCountsUpdate(
                                        totalCount = paginationContextWrapper.total,
                                        entityType = entityType
                                    )
                                )
                                backChannelEvents.sendEvent(
                                    PagingBackChannelEvent.PagingResponseReceived(
                                        Resource.Success(Empty),
                                        entityType
                                    )
                                )
                            }
                        }

                        override fun onFailure(error: Throwable?) {
                            sliceScope.launch {
                                backChannelEvents.sendEvent(
                                    PagingBackChannelEvent.PagingResponseReceived(
                                        Resource.Error(error = null, throwable = error),
                                        entityType
                                    )
                                )
                            }
                        }
                    })
                }
                newPagingSource
            }.flow.cachedIn(sliceScope).collectLatest {
                backChannelEvents.sendEvent(
                    PagingBackChannelEvent.PagingDataResUpdate(it, entityType)
                )
            }
        }
    }

    override fun handleUiEvent(event: UiEvent) {
        when (event) {
            // For related lists
            is DetailsUiEvent.RelatedListInitialized -> {
                initializePaging(event.primaryEntityType, event.primaryId)
            }
        }
    }

    override fun handleBackChannelEvent(event: BackChannelEvent) {
        when (event) {
            is PagingBackChannelEvent.SubmitSearchQuery -> updatePagingParameters(
                searchQuery = event.query,
            )

            is PagingBackChannelEvent.SubmitSortSelection -> updatePagingParameters(
                sortOption = event.sortOption
            )

            is PagingBackChannelEvent.SubmitDigitalAvailabilityFilterChange -> updatePagingParameters(
                digitalAvailabilityFilter = event.filterOn
            )

            is PagingBackChannelEvent.VariantsFilterChange -> updatePagingParameters(
                isVariantsEnabled = event.allowVariants
            )

            is DetailsBackChannelEvent.RequestForPagination -> {
                if (event.relatedEntityTypeToPageFor == entityType) {
                    initializePaging(event.rootEntityType, event.rootEntityId, event.sortKey)
                }
            }
        }
    }

    private fun updatePagingParameters(
        searchQuery: String? = null,
        sortOption: SortOption? = null,
        digitalAvailabilityFilter: Boolean = false,
        isVariantsEnabled: Boolean = true
    ) {
        var dataUpdateRequired = false
        if (searchQuery != currentSearchQuery) {
            currentSearchQuery = searchQuery
            dataUpdateRequired = true
        }
        if (sortOption != currentSortOption) {
            currentSortOption = sortOption
            dataUpdateRequired = true
        }
        if (digitalAvailabilityFilter != currentDigitalAvailabilityFilterEnabled) {
            currentDigitalAvailabilityFilterEnabled = digitalAvailabilityFilter
            dataUpdateRequired = true
        }
        if (isVariantsEnabled != currentVariantsEnabled) {
            currentVariantsEnabled = isVariantsEnabled
            dataUpdateRequired = true
        }
        if (dataUpdateRequired) {
            currentPagingSource?.invalidate()
        }
    }

    /**
     * If the user case is ROOT_ENTITY, should probably call this before registering the slice or else
     * it may not take effect.
     *
     * Max requested total items is useful for limiting the total pagination separately
     * from the total results.
     */
    fun setPagingConfig(pagingConfig: PagingConfig, maxPageLimit: Int? = null) {
        this.pagingConfig = pagingConfig
        this.maxPageLimit = maxPageLimit
    }

    private fun getDefaultPagingConfig(): PagingConfig {
        return when (useCase) {
            PagedListUseCase.ALL_AVAILABLE -> {
                PagingConfig(
                    initialLoadSize = ComicConstants.LIST_PAGE_SIZE * 2,
                    pageSize = ComicConstants.LIST_PAGE_SIZE,
                    enablePlaceholders = false,
                    prefetchDistance = ComicConstants.LIST_PAGE_SIZE / 2
                )
            }

            PagedListUseCase.RELATED_ENTITIES -> {
                PagingConfig(
                    initialLoadSize = ComicConstants.LIST_PAGE_SIZE * 2,
                    pageSize = ComicConstants.LIST_PAGE_SIZE,
                    enablePlaceholders = false,
                    prefetchDistance = ComicConstants.LIST_PAGE_SIZE / 2
                )
            }
        }
    }
}