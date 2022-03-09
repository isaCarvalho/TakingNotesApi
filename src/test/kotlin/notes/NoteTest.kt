package notes

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import mainModule
import model.Note
import org.junit.Assert
import org.junit.Test
import java.util.UUID

class NoteTest {

    @Test
    fun `Create Note`() {
        withTestApplication(Application::mainModule) {
            val call = handleRequest(HttpMethod.Post, "/note") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                setBody(jacksonObjectMapper().writeValueAsBytes(Note(UUID.randomUUID(), "example")))
            }

            Assert.assertEquals(HttpStatusCode.Created, call.response.status())
        }
    }
}