package notes

import asJson
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import database.DB
import database.Notes
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import mainModule
import model.Note
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.UUID

class NoteTest {

    @Test
    fun `Create Note`() {
        withTestApplication(Application::mainModule) {
            val call = createNote(UUID.randomUUID(), "example")

            Assert.assertEquals(HttpStatusCode.Created, call.response.status())
        }
    }

    @Test
    fun `All Notes`() {
        withTestApplication(Application::mainModule) {
            val call = handleRequest(HttpMethod.Get, "/note")
            Assert.assertEquals("[]".asJson(), call.response.content?.asJson())

            val id = UUID.randomUUID()
            createNote(id, "example 1")

            val afterCreated = handleRequest(HttpMethod.Get, "/note")
            Assert.assertEquals("""[{"id": "$id", "content": "example 1"}]""".asJson(), afterCreated.response.content?.asJson())
        }
    }

    @Test
    fun `Note By ID`() {
        withTestApplication(Application::mainModule) {
            val id = UUID.randomUUID()
            createNote(id, "example 1")

            val afterCreated = handleRequest(HttpMethod.Get, "/note/${id}")
            Assert.assertEquals("""{"id": "$id", "content": "example 1"}""".asJson(), afterCreated.response.content?.asJson())
        }
    }

    @Test
    fun `Update Note`() {
        withTestApplication(Application::mainModule) {
            val id = UUID.randomUUID()
            createNote(id, "example 1")

            val afterCreated = handleRequest(HttpMethod.Get, "/note")
            Assert.assertEquals("""[{"id": "$id", "content": "example 1"}]""".asJson(), afterCreated.response.content?.asJson())

             updateNote(id, "example 2")

            val afterUpdated = handleRequest(HttpMethod.Get, "/note")
            Assert.assertEquals("""[{"id": "$id", "content": "example 2"}]""".asJson(), afterUpdated.response.content?.asJson())
        }
    }

    @Test
    fun `Delete Note`() {
        withTestApplication(Application::mainModule) {
            val id = UUID.randomUUID()
            createNote(id, "example 1")

            val afterCreated = handleRequest(HttpMethod.Get, "/note")
            Assert.assertEquals("""[{"id": "$id", "content": "example 1"}]""".asJson(), afterCreated.response.content?.asJson())

            deleteNote(id)

            val afterDeleted = handleRequest(HttpMethod.Get, "/note")
            Assert.assertEquals("[]".asJson(), afterDeleted.response.content?.asJson())
        }
    }

    @Before
    fun cleanUp() {
        DB.connect()
        transaction {
            SchemaUtils.drop(Notes)
        }
    }
}

fun TestApplicationEngine.createNote(id: UUID, content: String) = handleRequest(HttpMethod.Post, "/note") {
    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    setBody(jacksonObjectMapper().writeValueAsBytes(Note(id, content)))
}

fun TestApplicationEngine.updateNote(id: UUID, content: String) = handleRequest(HttpMethod.Put, "/note") {
    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    setBody(jacksonObjectMapper().writeValueAsBytes(Note(id, content)))
}

fun TestApplicationEngine.deleteNote(id: UUID) = handleRequest(HttpMethod.Delete, "/note/${id}")