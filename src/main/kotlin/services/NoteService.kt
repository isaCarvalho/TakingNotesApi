package services

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

interface NoteService {

    suspend fun create(id: UUID, content: String)
}

class NoteServiceImpl : NoteService {
    override suspend fun create(id: UUID, content: String) {
        transaction {
            Notes.insert {
                it[Notes.id] = id
                it[Notes.content] = content
            }
        }
    }

}