package com.nw.persistence

import com.nw.enums.PrintTypeEnum
import com.nw.enums.RatingEnum
import com.nw.enums.ReadStatusEnum
import com.nw.models.Book
import com.nw.models.BookTag
import java.time.OffsetDateTime

interface BookFacade {
    suspend fun allBooks(): List<Book>
    suspend fun book(id: Int): Book?
    suspend fun addNewBook(
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
        addedOnDate: OffsetDateTime
    ): Book?
    suspend fun editBook(
        id: Int,
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
        addedOnDate: OffsetDateTime
    ): Boolean
    suspend fun deleteBook(id: Int): Boolean
    suspend fun findBookById(id: List<Int>): List<Book>
    suspend fun getBookListFromBookTags(bookTags: List<BookTag>): List<Book>

    suspend fun findBookByIsbn10orIsbn13(isbn10: String, isbn13: String): Book?
}
