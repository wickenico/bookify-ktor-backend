package com.nw.persistence

import com.nw.models.Book
import com.nw.models.User
import com.nw.models.UserBook

interface UserFacade {
    suspend fun allUsers(): List<User>

    suspend fun user(id: Int): User?

    suspend fun findUserByUsername(username: String): User?

    suspend fun findUserByUsernameAndPassword(
        user: String,
        password: String,
    ): User?

    suspend fun addNewUser(user: User): User?

    suspend fun editUser(
        userId: Int,
        fullName: String,
        email: String,
    ): Boolean

    suspend fun deleteUser(id: Int): Boolean

    suspend fun getUser(
        username: String,
        password: String,
    ): User?

    suspend fun checkIfUsernameExists(username: String): Boolean

    suspend fun verifyUser(
        userId: Int,
        password: String,
    ): Boolean

    suspend fun changePassword(
        userId: Int,
        newPassword: String,
    ): Boolean

    suspend fun getBookListFromUserBooks(userBooks: List<UserBook>): List<Book>
}
