package com.nw.persistence

import com.nw.models.User

interface UserFacade {
    suspend fun allUsers(): List<User>
    suspend fun user(id: Int): User?
    suspend fun findUserByUsername(username: String): User?
    suspend fun findUserByUsernameAndPassword(user: String, password: String): User?
    suspend fun addNewUser(user: User): User?
    suspend fun editUser(user: User): Boolean
    suspend fun deleteUser(id: Int): Boolean
}
