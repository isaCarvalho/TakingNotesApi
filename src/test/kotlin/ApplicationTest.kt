import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Assert
import org.junit.Test

class ApplicationTest {

    @Test
    fun emptyPath() {
        withTestApplication(Application::mainModule) {
            val call = handleRequest(HttpMethod.Get, "")
            Assert.assertEquals(HttpStatusCode.OK, call.response.status())
        }
    }

    @Test
    fun validValue() {
        withTestApplication(Application::mainModule) {
            val call = handleRequest(HttpMethod.Get, "/username")

            Assert.assertEquals("""{ "Username: ": "username" }""".asJson(), call.response.content?.asJson())
        }
    }
}

fun String.asJson() = ObjectMapper().readTree(this)