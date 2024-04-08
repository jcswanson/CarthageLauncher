// Data class representing a delta update, which contains new or updated content and its corresponding role.
data class Delta(

    // The updated or new content. This can be any data type or structure, but is typically a string.
    val content: String,

    // The role of the content, indicating its purpose or category. This can be used to differentiate between different types of content in the update.
    val role: String
)
