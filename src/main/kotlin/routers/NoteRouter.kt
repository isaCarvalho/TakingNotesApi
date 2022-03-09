package routers

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import model.Note
import services.NoteService
import java.util.UUID

fun Route.noteRouter(noteService: NoteService) {
    route("/note") {
        post {
            with(call) {
                val parameters = receive<Note>()
                val id = parameters.id
                val content = parameters.content

                noteService.create(id, content)
                call.response.status(HttpStatusCode.Created)
            }
        }

        put {
            with(call) {
                val parameters = receive<Note>()
                val id = parameters.id
                val content = parameters.content

                noteService.update(id, content)
                call.response.status(HttpStatusCode.OK)
            }
        }

        delete("/{id}") {
            with(call) {
                val id = UUID.fromString(parameters["id"])

                noteService.delete(id)
                call.response.status(HttpStatusCode.OK)
            }
        }

        get {
            call.respond(noteService.all())
        }

        get("/{id}") {
            with(call) {
                val id = UUID.fromString(parameters["id"])
                val note = noteService.findById(id)

                if (note == null) {
                    respond(HttpStatusCode.NotFound)
                } else {
                    respond(note)
                }
            }
        }
    }
}