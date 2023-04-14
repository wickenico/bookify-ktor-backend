package com.nw.plugins

import com.nw.enums.PrintTypeEnum
import com.nw.enums.RatingEnum
import com.nw.enums.ReadStatusEnum
import com.nw.models.Book
import com.nw.persistence.bookFacade
import com.nw.persistence.userFacade
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.util.getOrFail
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.Year
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun Application.configureGoogleBooksApiSearch() {
    routing {
        authenticate {
            get("/api/v1/search") {
                val isbn = call.request.queryParameters.getOrFail("isbn").toLong()
                val client = HttpClient(CIO) {
                    expectSuccess = true
                    install(ContentNegotiation) {
                        json(
                            Json {
                                prettyPrint = true
                                isLenient = true
                            }
                        )
                    }
                }
                val response: HttpResponse =
                    client.get("https://www.googleapis.com/books/v1/volumes?q=isbn:$isbn&country=DE&maxResults=2")
                println(response.status)
                val stringBody: String = response.body()

                val json: Map<String, JsonElement> = Json.parseToJsonElement(stringBody).jsonObject

                if (json["items"] == null) {
                    client.close()
                    call.respondRedirect("/error")
                }

                val items = json["items"]!!.jsonArray

                val item = items[0]

                val itemInfo = item.jsonObject

                val volumeInfo = itemInfo["volumeInfo"]

                val volumeInfoObject = volumeInfo?.jsonObject

                // ISBN
                var isbnArray = volumeInfoObject?.get("industryIdentifiers")?.jsonArray

                var isbn10Object = isbnArray?.get(0)?.jsonObject
                var isbn10 = isbn10Object?.get("identifier").toString()
                isbn10 = isbn10.replace("\"", "").trim()

                var isbn13: String

                if ((isbnArray?.size ?: 0) > 1) {
                    var isbn13Object = isbnArray?.get(1)?.jsonObject
                    isbn13 = isbn13Object?.get("identifier").toString()
                    isbn13 = isbn13.replace("\"", "").trim()
                } else {
                    isbn13 = isbn10
                }

                // Get userId
                val userName = call.principal<UserIdPrincipal>()?.name
                val user = userName?.let { it1 -> userFacade.findUserByUsername(it1) }
                val userId = user!!.id

                if (bookFacade.findBookByIsbn10orIsbn13AndUserId(isbn10, isbn13, userId) != null) {
                    val status = HttpStatusCode.Conflict
                    val message = "The book $isbn10 / $isbn13 already exists."
                    call.respondText(message, status = status)
                } else {
                    // Title
                    var title = volumeInfoObject?.get("title").toString()
                    title = title.replace("\"", "")

                    // Subtitle
                    var subtitle = volumeInfoObject?.get("subtitle").toString()
                    subtitle = subtitle.replace("\"", "")

                    // Author
                    val authors = volumeInfoObject?.get("authors")?.jsonArray

                    // var author = authors?.get(0).toString()
                    // author = author.replace("\"", "")
                    val authorsList = mutableListOf<String>()

                    authors?.forEach {
                        var author = it.toString()
                        author = author.replace("\"", "")
                        authorsList.add(author)
                    }

                    // Publisher
                    var publisher = volumeInfoObject?.get("publisher").toString()
                    publisher = publisher.replace("\"", "")

                    // Pages
                    var pageCount = volumeInfoObject?.get("pageCount")
                    var pages = pageCount.toString().toInt()

                    // Cover Image
                    val imageLinks = volumeInfoObject?.get("imageLinks")?.jsonObject

                    var imageUrl = imageLinks?.get("thumbnail").toString()
                    imageUrl = imageUrl.replace("\"", "").trim()
                    imageUrl = imageUrl.replace("http://", "https://")

                    // Self Link
                    var selfLink = itemInfo["selfLink"].toString()
                    selfLink = selfLink.replace("\"", "").trim()

                    // Published Date
                    var publishedDate = volumeInfoObject?.get("publishedDate")?.jsonPrimitive?.content
                    val formatterYear = DateTimeFormatter.ofPattern("yyyy")
                    val formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    val year = try {
                        Year.parse(publishedDate, formatterYear)
                    } catch (ex: Exception) {
                        null
                    }
                    val localDate = year?.atDay(1) ?: LocalDate.parse(publishedDate, formatterDate)
                    val localDateTime = localDate.atStartOfDay()
                    val offset = OffsetTime.of(0, 0, 0, 0, ZoneOffset.UTC).offset
                    val publishedDateOffsetDateTime = OffsetDateTime.of(localDateTime, offset)

                    // var pDate = OffsetDateTime.now()

                    // Description
                    var description = volumeInfoObject?.get("description").toString()
                    description = description.replace("\"", "")

                    // Print Type
                    var printType = volumeInfoObject?.get("printType").toString()
                    printType = printType.replace("\"", "")

                    // Category
                    var category = ""

                    // Maturity Rating
                    var maturityRating = volumeInfoObject?.get("maturityRating").toString()
                    maturityRating = maturityRating.replace("\"", "")

                    // Language
                    var language = volumeInfoObject?.get("language").toString()
                    language = language.replace("\"", "")

                    // Info Link
                    var infoLink = volumeInfoObject?.get("infoLink").toString()
                    infoLink = infoLink.replace("\"", "")

                    // Rating
                    var rating = 0

                    // Comment
                    var comment = ""

                    // Read Status
                    var readStatus = "UNREAD"

                    // Added On Date
                    var addedOnDate = OffsetDateTime.now()

                    client.close()

                    val book = Book.newBook(
                        isbn10,
                        isbn13,
                        title,
                        subtitle,
                        authorsList,
                        publisher,
                        pages,
                        imageUrl,
                        selfLink,
                        publishedDateOffsetDateTime,
                        description,
                        PrintTypeEnum.getByValue(printType),
                        category,
                        maturityRating,
                        language,
                        infoLink,
                        RatingEnum.getByValue(rating),
                        comment,
                        ReadStatusEnum.getByValue(readStatus),
                        addedOnDate,
                        userId
                    )

                    call.respond(book)
                }
            }
        }
    }
}
