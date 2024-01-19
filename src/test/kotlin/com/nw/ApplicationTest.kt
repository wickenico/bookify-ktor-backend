package com.nw

import com.nw.plugins.configureBooks
import com.nw.utils.bookFacade
import com.nw.utils.bookTagFacade
import com.nw.utils.tagFacade
import com.nw.utils.userFacade
import io.ktor.server.testing.testApplication
import org.junit.Test

class ApplicationTest {
    @Test
    fun testRoot() =
        testApplication {
            application {
                configureBooks(bookFacade, bookTagFacade, tagFacade, userFacade)
            }
//        client.get("/").apply {
//            assertEquals(HttpStatusCode.OK, status)
//            assertEquals("Hello World!", bodyAsText())
//        }
        }
}
