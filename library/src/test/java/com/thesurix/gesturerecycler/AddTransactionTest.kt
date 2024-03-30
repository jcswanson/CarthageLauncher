package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.AddTransaction
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddTransactionTest : BaseTransactionTest() {

    // Test if an item can be added to the transactional object without a header
    @Test
    fun testAddItemWithoutHeader() {
        val value = "F"
        val transaction = AddTransaction(value, false)

        // Perform the transaction and check if it's successful
        assertTrue(transaction.perform(transactional))

        // Check if the last element in the transactional object's data is the value
        assertEquals(transactional.data.last(), value)

        // Verify that the notifyInserted method was called with the correct index
        Mockito.verify(transactional).notifyInserted(data.size - 1)

        // Revert the transaction and check if it's successful
        assertTrue(transaction.revert(transactional))

        // Check if the last element in the transactional object's data is the same as the original last element
        assertEquals(transactional.data.last(), originalData.last())

        // Verify that the notifyRemoved method was called with the correct index
        Mockito.verify(transactional).notifyRemoved(data.size)
    }

    // Test if an item can be added to the transactional object with a header
    @Test
    fun testAddItemWithHeader() {
        val value = "F"
        val transaction = AddTransaction(value, true)

        // Perform the transaction and check if it's successful
        assertTrue(transaction.perform(transactional))

        // Check if the last element in the transactional object's data is the value
        assertEquals(transactional.data.last(), value)

        // Verify that the notifyInserted method was called with the correct index
        Mockito.verify(transactional).notifyInserted(data.size)

        // Revert the transaction and check if it's successful
        assertTrue(transaction.revert(transactional))

        // Check if the last element in the transactional object's data is the same as the original last element
        assertEquals(transactional.data.last(), originalData.last())

        // Verify that the notifyRemoved method was called with the correct index
        Mockito.verify(transactional).notifyRemoved(data.size - 1)
    }
}
