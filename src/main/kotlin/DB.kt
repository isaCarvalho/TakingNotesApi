import org.jetbrains.exposed.sql.Table

object Notes : Table() {
    val id = uuid("id").primaryKey().uniqueIndex()
    val content = varchar("content", length = 500)
}