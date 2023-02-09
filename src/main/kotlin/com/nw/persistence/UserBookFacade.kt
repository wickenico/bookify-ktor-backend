package com.nw.persistence

import com.nw.models.UserBook

interface UserBookFacade {
    suspend fun getAllUserBooks(): List<UserBook>
    suspend fun findAllUserBooksByUserId(userId: Int): List<UserBook>
    suspend fun addBookToUser(userId: Int, bookId: Int): UserBook?
}
