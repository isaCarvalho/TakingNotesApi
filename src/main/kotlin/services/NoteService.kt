package services

import database.Notes
import model.Note
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

interface NoteService {

    suspend fun create(id: UUID, content: String)

    suspend fun update(id: UUID, content: String)

    suspend fun delete(id: UUID)

    suspend fun all() : List<Note>

    suspend fun findById(id: UUID) : Note?
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

    override suspend fun update(id: UUID, content: String) {
        transaction {
            Notes.update({Notes.id eq id}) {
                it[Notes.id] = id
                it[Notes.content] = content
            }
        }
    }

    override suspend fun delete(id: UUID) {
        transaction {
            Notes.deleteWhere { Notes.id eq id }
        }
    }

    override suspend fun all(): List<Note> {
        return transaction {
            Notes.selectAll().map {
                it.asNote()
            }
        }
    }

    override suspend fun findById(id: UUID): Note? {
        val row = transaction {
            Notes.select {
                addLogger(StdOutSqlLogger)
                Notes.id eq id
            }
        }.firstOrNull()

        return row?.asNote()
    }

}

private fun ResultRow.asNote() = Note(
    this[Notes.id],
    this[Notes.content]
)