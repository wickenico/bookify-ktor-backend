package com.nw.auth

import io.ktor.server.auth.Principal

data class UserSession(val name: String) : Principal
