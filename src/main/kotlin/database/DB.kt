import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.UUID

object Notes : Table() {
    val id : Column<UUID> = uuid("id").primaryKey().uniqueIndex()
    val content = varchar("content", length = 500)
}