package com.nw

import com.nw.plugins.configureRouting
import io.ktor.client.request.get
import io.ktor.server.testing.testApplication
import org.junit.Test

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
//        client.get("/").apply {
//            assertEquals(HttpStatusCode.OK, status)
//            assertEquals("Hello World!", bodyAsText())
//        }
    }
}
