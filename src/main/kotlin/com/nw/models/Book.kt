package com.nw.models

import com.nw.enums.PrintTypeEnum
import com.nw.enums.RatingEnum
import com.nw.enums.ReadStatusEnum
import com.nw.utils.offsetDateTime
import org.jetbrains.exposed.sql.Table
import java.time.OffsetDateTime
import java.util.concurrent.atomic.AtomicInteger

data class Book(
    val id: Int,
    val isbn10: String,
    val isbn13: String,
    var title: String,
    var subtitle: String,
    var author: String,
    var publisher: String,
    var pages: Int,
    var imageUrl: String,
    var selfLink: String,
    var publishedDate: OffsetDateTime,
    var description: String,
    var printType: PrintTypeEnum,
    var category: String,
    var maturityRating: String,
    var language: String, // Enum?
    var infoLink: String,
    var rating: RatingEnum,
    var comment: String,
    var readStatus: ReadStatusEnum,
    var addedOnDate: OffsetDateTime,
    var userId: Int
) {
    companion object {
        private val idCounter = AtomicInteger()
        fun newBook(
            isbn10: String,
            isbn13: String,
            title: String,
            subtitle: String,
            author: String,
            publisher: String,
            pages: Int,
            imageUrl: String,
            selfLink: String,
            publishedDate: OffsetDateTime,
            description: String,
            printType: PrintTypeEnum,
            category: String,
            maturityRating: String,
            language: String,
            infoLink: String,
            rating: RatingEnum,
            comment: String,
            readStatus: ReadStatusEnum,
            addedOnDate: OffsetDateTime,
            userId: Int
        ) = Book(
            idCounter.getAndIncrement(),
            isbn10,
            isbn13,
            title,
            subtitle,
            author,
            publisher,
            pages,
            imageUrl,
            selfLink,
            publishedDate,
            description,
            printType,
            category,
            maturityRating,
            language,
            infoLink,
            rating,
            comment,
            readStatus,
            addedOnDate,
            userId
        )
    }
}

object Books : Table() {
    val id = integer("id").autoIncrement()
    val isbn10 = varchar("isbn10", 128)
    val isbn13 = varchar("isbn13", 128)
    val title = varchar("title", 128)
    val subtitle = varchar("subtitle", 128)
    val author = varchar("author", 128)
    val publisher = varchar("publisher", 128)
    val pages = integer("pages")
    val imageUrl = varchar("imageUrl", 128)
    val selfLink = varchar("selfLink", 128)
    val publishedDate = offsetDateTime("publishedDate")
    val description = varchar("description", 5000)
    val printType = enumerationByName("printType", 128, PrintTypeEnum::class)
    val category = varchar("category", 128)
    val maturityRating = varchar("maturityRating", 128)
    val language = varchar("language", 128)
    val infoLink = varchar("infoLink", 128)
    val rating = enumeration("rating", RatingEnum::class)
    val comment = varchar("comment", 128)
    val readStatus = enumerationByName("readStatus", 128, ReadStatusEnum::class)
    val addedOnDate = offsetDateTime("addedOnDate")
    val userId = integer("user_id")

    override val primaryKey = PrimaryKey(id)
}
