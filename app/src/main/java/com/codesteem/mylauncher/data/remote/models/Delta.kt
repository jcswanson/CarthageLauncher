import java.util.Objects

// Data class representing a delta update, which contains new or updated content and its corresponding role.
data class Delta<T>(

    // The updated or new content. This can be any data type or structure, but is typically a string.
    val content: T,

    // The role of the content, indicating its purpose or category. This can be used to differentiate between different types of content in the update.
    val role: String
) {

    init {
        Objects.requireNonNull(content, "Content cannot be null")
        Objects.requireNonNull(role, "Role cannot be null")
    }

    // Companion object to create a Delta instance with default role
    companion object {
        fun <T> create(content: T): Delta<T> {
            return Delta(content, "DEFAULT_ROLE")
        }
    }
}
