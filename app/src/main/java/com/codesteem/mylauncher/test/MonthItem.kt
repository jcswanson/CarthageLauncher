// Interface for MonthItem, which represents an item in a month view.
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

// Data class for MonthHeaderItem, which is a concrete implementation of MonthItem for the header.
data class MonthHeaderItem(
    override val type: MonthItemType = MonthItemType.HEADER,
    override val name: String,
    override val openCounter: Int = 0
) : MonthItem

// Data class for MonthDayItem, which is a concrete implementation of MonthItem for the individual days.
data class MonthDayItem(
    override val type: MonthItemType = MonthItemType.MONTH,
    override val name: String,
    override val openCounter: Int = 0
) : MonthItem
