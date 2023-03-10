package com.nw

import com.nw.persistence.DatabaseFactory
import com.nw.plugins.authRoutes
import com.nw.plugins.configureAuth
import com.nw.plugins.configureDocumentation
import com.nw.plugins.configureFavorites
import com.nw.plugins.configureGoogleBooksApiSearch
import com.nw.plugins.configureHTTP
import com.nw.plugins.configureMonitoring
import com.nw.plugins.configureRouting
import com.nw.plugins.configureSerialization
import com.nw.plugins.configureSession
import com.nw.plugins.configureStatusPages
import com.nw.plugins.configureTag
import com.nw.plugins.configureUser
import com.nw.plugins.versioning
import io.ktor.server.application.Application

fun main(args: Array<String>): Unit =
    io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureHTTP()
    configureSerialization()
    DatabaseFactory.init()
    configureMonitoring()
    configureStatusPages()
    configureDocumentation()
    configureAuth()
    configureSession()
    authRoutes()
    configureRouting()
    configureUser()
    configureTag()
    configureGoogleBooksApiSearch()
    configureFavorites()
    versioning()
}
