// The MonthHeader class represents a header for a month in a calendar or similar display.
// It extends the MonthItem abstract class and overrides the 'name' and 'type' properties.

// The 'name' property specifies the name of the month (e.g. "January", "February", etc.).
// This property is required and must be provided when creating a new instance of MonthHeader.
class MonthHeader(override val name: String, override val openCounter: Int = 0) : MonthItem {

    // The 'type' property is a read-only property that specifies the type of MonthItem.
    // For MonthHeader, the type is set to MonthItem.MonthItemType.HEADER.
    override val type: MonthItem.MonthItemType
        get() = MonthItem.MonthItemType.HEADER
}

// The 'openCounter' property is an optional property that specifies the number of items currently open
// for the given month. By default, it is set to 0.
