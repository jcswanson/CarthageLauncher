package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.AddTransaction
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AddTransactionTest : BaseTransactionTest() {

    // This test checks if an item can be added to the transactional object without a header
    @Test
    fun `add item in transaction without header`() {
        val transaction = AddTransaction("F", false) // Create a new AddTransaction object with value "F" and header flag set to false

        // Perform the transaction and check if it's successful
        assertTrue(transaction.perform(transactional))

        // Check if the last element in the transactional object's data is "F"
        assertEquals(transactional.data.last(), "F")

        // Verify that the notifyInserted method was called with the correct index
        Mockito.verify(transactional).notifyInserted(data.size - 1)
    }

    // This test checks if an item can be reverted from the transactional object without a header
    @Test
    fun `revert add item in transaction without header`() {
        val lastElement = transactional.data.last() // Save the last element in the transactional object's data
        val transaction = AddTransaction("F", false) // Create a new AddTransaction object with value "F" and header flag set to false
        transaction.perform(transactional) // Perform the transaction

        // Check if the last element in the transactional object's data is "F"
        assertEquals(transactional.data.last(), "F")

        // Revert the transaction and check if it's successful
        assertTrue(transaction.revert(transactional))

        // Check if the last element in the transactional object's data is the same as the original last element
        assertEquals(transactional.data.last(), lastElement)

        // Verify that the notifyRemoved method was called with the correct index
        Mockito.verify(transactional).notifyRemoved(data.size)
    }

    // This test checks if an item can be added to the transactional object with a header
    @Test
    fun `add item in transaction with header`() {
        val transaction = AddTransaction("F", true) // Create a new AddTransaction object with value "F" and header flag set to true

        // Perform the transaction and check if it's successful
        assertTrue(transaction.perform(transactional))

        // Check if the last element in the transactional object's data is "F"
        assertEquals(transactional.data.last(), "F")

        // Verify that the notifyInserted method was called with the correct index
        Mockito.verify(transactional).notifyInserted(data.size)
    }

    // This test checks if an item can be reverted from the transactional object with a header
    @Test
