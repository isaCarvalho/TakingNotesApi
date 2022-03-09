package model

import java.util.UUID

data class Note(
    val id: UUID,
    val content: String
)