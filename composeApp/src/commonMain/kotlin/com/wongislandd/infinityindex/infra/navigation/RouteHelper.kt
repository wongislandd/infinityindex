package com.wongislandd.infinityindex.infra.navigation

object RouteHelper {
    fun getComicDetailsRouteForId(comicId: Int): String {
        return NavigationItem.ComicDetails.route.replace("{comicId}", comicId.toString())
    }

    fun getCreatorDetailsRouteForId(creatorId: Int): String {
        return NavigationItem.CreatorDetails.route.replace("{creatorId}", creatorId.toString())
    }

    fun getCharacterDetailsRouteForId(characterId: Int): String {
        return NavigationItem.CharacterDetails.route.replace("{characterId}", characterId.toString())
    }

    fun getSeriesDetailsRouteForId(seriesId: Int): String {
        return NavigationItem.SeriesDetails.route.replace("{seriesId}", seriesId.toString())
    }

    fun getEventDetailsRouteForId(eventId: Int): String {
        return NavigationItem.EventDetails.route.replace("{eventId}", eventId.toString())
    }

    fun getStoryDetailsRouteForId(storyId: Int): String {
        return NavigationItem.StoryDetails.route.replace("{storyId}", storyId.toString())
    }
}