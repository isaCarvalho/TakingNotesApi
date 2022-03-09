package routers

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.routing.*
import model.Note
import services.NoteService

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
    }
}