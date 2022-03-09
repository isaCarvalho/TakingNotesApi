package database

import org.jetbrains.exposed.sql.Database

object DB {
    private const val host = "localhost"
    private const val port = 5555
    private const val dbName = "taking_notes_db"
    private const val dbUser = "taking_notes"
    private const val dbPassword = "123456"

    fun connect() = Database.connect("jdbc:postgresql://$host:$port/$dbName", driver = "org.postgresql.Driver",
        user = dbUser, password = dbPassword
    )
}