package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.RevertReorderTransaction
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RevertReorderTransactionTest : BaseTransactionTest() { // Inherits from BaseTransactionTest class

    @Test
    fun `reorder item in transaction without header`() {
        // Arrange
        val transaction = RevertReorderTransaction<String>(3, 4, false) // Create a new RevertReorderTransaction object

        // Assert
        assertFalse(transaction.perform(transactional)) // Assert that the transaction cannot be performed
    }

    @Test
    fun `revert reorder item in transaction without header`() {
        // Arrange
        val item1 = transactional.data[3] // Save the original item at index 3
        val item2 = transactional.data[4] // Save the original item at index 4
        val transaction = RevertReorderTransaction<String>(3, 4, false) // Create a new RevertReorderTransaction object

        // Act
        assertTrue(transaction.revert(transactional)) // Revert the transaction

        // Assert
        assertEquals(transactional.data[3], item2) // Assert that the item at index 3 is now the original item at index 4
        assertEquals(transactional.data[4], item1) // Assert that the item at index 4 is now the original item at index 3
        Mockito.verify(transactional).notifyRemoved(4) // Verify that notifyRemoved is called with the correct index
        Mockito.verify(transactional).notifyInserted(3) // Verify that notifyInserted is called with the correct index
    }

    @Test
    fun `reorder item in transaction with header`() {
        // Arrange
        val transaction = RevertReorderTransaction<String>(3, 4, true) // Create a new RevertReorderTransaction object with a header

        // Assert
        assertFalse(transaction.perform(transactional)) // Assert that the transaction cannot be performed
    }

    @Test
    fun `revert reorder item in transaction with header`() {
        // Arrange
        val item1 = transactional.data[3] // Save the original item at index 3
        val item2 = transactional.data[4] // Save the original item at index 4
        val transaction = RevertReorderTransaction<String>(3, 4, true) // Create a new RevertReorderTransaction object with a header

        // Act
        assertTrue(transaction.revert(transactional)) // Revert the transaction

        // Assert
        assertEquals(transactional.data[3], item2) // Assert that the item at index
