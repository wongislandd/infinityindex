package com.wongislandd.infinityindex.entities.comics.models

enum class LinkType(
    val displayName: String
) {
    DETAILS("Marvel Details Page"),
    PURCHASE("Purchase"),
    IN_APP_LINK("Marvel Unlimited App Link"),
    WIKI("Wiki"),
    COMIC_LINK("Additional Comic Details"),
    READER("Read Online"),
    UNKNOWN("Unknown")
}