import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.jackson
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.util.*

fun main() {
    val port = 8080

    val server = embeddedServer(Netty, port, module = Application::mainModule)

    server.start()
}

fun Application.mainModule() {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        trace {
            application.log.trace(it.buildText())
        }

        get("/{name}") {
            val name = call.parameters["name"]

            context.respond(mapOf("Username: " to name))
        }

        get {
            context.respond(mapOf<Date, String>(Pair(Date(), "Welcome to Taking Notes API")))
        }
    }
}