/**
 * A class representing a header for a month in a calendar or launcher application.
 *
 * This class extends the [MonthItem] abstract class and overrides the [name] and [type] properties.
 * The [name] property represents the name of the month, while the [type] property is a constant
 * value of [MonthItem.MonthItemType.HEADER], indicating that this is a header item and not a regular
 * month item.
 *
 * The [openCounter] property is also overridden with a default value of 0, which could be used to
 * track the number of items or events open for that particular month.
 *
 * @property name The name of the month.
 * @property openCounter An integer value that could be used to track the number of items or events open for that particular month. The default value is 0.
 */
class MonthHeader(override val name: String, override val openCounter: Int = 0) : MonthItem {

    /**
     * A constant value of [MonthItem.MonthItemType.HEADER], indicating that this is a header item and not a regular month item.
     */
    override val type: MonthItem.MonthItemType
        get() = MonthItem.MonthItemType.HEADER
}
