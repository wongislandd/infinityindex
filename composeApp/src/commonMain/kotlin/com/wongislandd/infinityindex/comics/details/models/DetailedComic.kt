package com.wongislandd.infinityindex.comics.details.models

data class DetailedComic(
    val title: String,
    val imageUrl: String,
    val pageCount: Int?,
    val issueNumber: Double?,
    val lastModified: String?,
    val relatedDates: List<RelatedDate>,
    val relatedTexts: List<RelatedText>,
    val relatedPrices: List<RelatedPrice>,
    val relatedLinks: List<RelatedLink>,
    val variantDescription: String?,
    val description: String?,
    val upc: String?,
    val diamondCode: String?,
    val ean: String?,
    val issn: String?,
    val format: String?
)