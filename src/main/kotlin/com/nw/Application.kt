package com.nw

import com.nw.persistence.DatabaseFactory
import com.nw.plugins.configureDocumentation
import com.nw.plugins.configureGoogleBooksApiSearch
import com.nw.plugins.configureHTTP
import com.nw.plugins.configureMonitoring
import com.nw.plugins.configureRouting
import com.nw.plugins.configureSecurity
import com.nw.plugins.configureSerialization
import com.nw.plugins.configureStatusPages
import com.nw.plugins.configureTag
import com.nw.plugins.configureUser
import io.ktor.server.application.Application

fun main(args: Array<String>): Unit =
    io.ktor.server.cio.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    configureHTTP()
    configureSerialization()
    // configureDatabases()
    DatabaseFactory.init()
    configureMonitoring()
    configureStatusPages()
    configureDocumentation()
    configureSecurity()
    configureRouting()
    configureUser()
    configureTag()
    configureGoogleBooksApiSearch()
}
