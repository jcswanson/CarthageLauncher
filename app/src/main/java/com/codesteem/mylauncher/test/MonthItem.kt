// This is an interface for MonthItem, which represents an item in a month view.
interface MonthItem {

    // The type of the MonthItem. It can be either HEADER or MONTH.
    val type: MonthItemType

    // The name of the MonthItem. For HEADER, it's the name of the month. For MONTH, it's the name of the day.
    val name: String

    // The number of times the MonthItem has been opened.
    val openCounter: Int

    // Enum class representing the two types of MonthItem.
    enum class MonthItemType {
        // HEADER represents the title of the month.
        HEADER,
        // MONTH represents the individual days of the month.
        MONTH
    }
}
