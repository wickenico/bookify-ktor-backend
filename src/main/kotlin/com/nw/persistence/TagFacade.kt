package com.nw.persistence

import com.nw.models.BookTag
import com.nw.models.Tag

interface TagFacade {
    suspend fun allTags(): List<Tag>
    suspend fun tag(id: Int): Tag?
    suspend fun findTagById(id: List<Int>): List<Tag>
    suspend fun findTagByName(name: String): Tag?
    suspend fun addNewTag(name: String): Tag?
    suspend fun deleteTag(id: Int): Boolean
    suspend fun getTagListFromBookTags(bookTags: List<BookTag>): List<Tag>
}
