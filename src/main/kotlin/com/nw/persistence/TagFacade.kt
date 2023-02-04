package com.nw.persistence

import com.nw.models.Tag

interface TagFacade {
    suspend fun allTags(): List<Tag>
    suspend fun tag(id: Int): Tag?
    suspend fun addNewTag(tag: Tag): Tag?
    suspend fun deleteTag(id: Int): Boolean
}
