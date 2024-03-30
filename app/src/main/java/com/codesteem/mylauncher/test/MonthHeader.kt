/**
 * A class representing a header for a month in a calendar or launcher application.
 * This class extends the [MonthItem] abstract class and overrides the [name] and [type] properties.
 */
class MonthHeader(name: String) : MonthItem(name, MonthItemType.HEADER, 0) {

    /**
     * An enum class representing the different types of month items.
     */
    enum class MonthItemType {
        REGULAR, HEADER
    }

    /**
     * A data class representing a month item in a calendar or launcher application.
     * This class is abstract and cannot be instantiated.
     *
     * @property name The name of the month or event.
     * @property type The type of the month item.
     * @property openCounter An integer value that can be used to track the number of items or events open for that particular month or event.
     */
    abstract class MonthItem(open val name: String, open val type: MonthItemType, var openCounter: Int)
}
