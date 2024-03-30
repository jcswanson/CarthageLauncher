import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun additionIsCorrect() {
        // Perform the addition
        val sum = 2 + 2

        // Verify the result
        assertEquals(4, sum)
    }

    @Test
    fun subtractionIsCorrect() {
        // Perform the subtraction
        val difference = 4 - 2

        // Verify the result
        assertEquals(2, difference)
    }

    @Test
    fun multiplicationIsCorrect() {
        // Perform the multiplication
        val product = 2 * 2

        // Verify the result
        assertEquals(4, product)
    }

    @Test
    fun divisionIsCorrect() {
        // Perform the division
        val quotient = 4.0 / 2.0

        // Verify the result
        assertEquals(2.0, quotient, 0.001)
    }
}
