import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.jackson
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import routers.noteRouter
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import services.NoteServiceImpl
import java.util.*

fun main() {
    val port = 8080

    val server = embeddedServer(Netty, port, module = Application::mainModule)

    server.start()
}

fun Application.mainModule() {
    val host = "localhost"
    val port = 5555
    val dbName = "taking_notes_db"
    val dbUser = "taking_notes"
    val dbPassword = "123456"

    val db = Database.connect("jdbc:postgresql://$host:$port/$dbName", driver = "org.postgresql.Driver",
    user = dbUser, password = dbPassword)

    transaction {
        SchemaUtils.create(Notes)
    }

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

        noteRouter(NoteServiceImpl())
    }
}