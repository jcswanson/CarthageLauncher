package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.InsertTransaction
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class InsertTransactionTest : BaseTransactionTest() {

    // Test the insert transaction without a header
    @Test
    fun `insert item in transaction without header`() {
        val itemToInsert = "X" // Define the item to be inserted
        val transaction = InsertTransaction(itemToInsert, 4, false) // Create a new insert transaction with the item and position

        // Perform the transaction and check if it's successful
        assertTrue(transaction.perform(transactional))
        assertEquals(transactional.data[4], itemToInsert) // Verify that the inserted item is at the correct position
        Mockito.verify(transactional).notifyInserted(4) // Check if the notifyInserted method is called with the correct position
    }

    // Test the revert operation for the insert transaction without a header
    @Test
    fun `revert insert item in transaction without header`() {
        val itemToInsert = "X" // Define the item to be inserted
        val itemBeforeInsert = transactional.data[4] // Save the item at the position before insertion
        val transaction = InsertTransaction(itemToInsert, 4, false) // Create a new insert transaction with the item and position
        transaction.perform(transactional) // Perform the transaction

        // Revert the transaction and check if it's successful
        assertEquals(transactional.data[4], itemToInsert) // Verify that the inserted item is at the correct position
        assertTrue(transaction.revert(transactional))
        assertEquals(transactional.data[4], itemBeforeInsert) // Verify that the original item is restored
        Mockito.verify(transactional).notifyRemoved(4) // Check if the notifyRemoved method is called with the correct position
    }

    // Test the insert transaction with a header
    @Test
    fun `insert item in transaction with header`() {
        val itemToInsert = "X" // Define the item to be inserted
        val transaction = InsertTransaction(itemToInsert, 4, true) // Create a new insert transaction with the item, position, and header

        // Perform the transaction and check if it's successful
        assertTrue(transaction.perform(transactional))
        assertEquals(transactional.data[4], itemToInsert) // Verify that the inserted item is at the correct position
        Mockito.verify(transactional).notifyInserted(5) // Check if the notifyInserted method is called with the correct position (5, considering the header)
    }

    // Test the revert operation for the insert transaction with a header
    @Test
    fun `revert insert item in transaction with header`() {
        val itemToInsert = "X" // Define the item to be inserted
        val itemBeforeInsert = transactional.data[4] // Save the item at the position before insertion
        val transaction = InsertTransaction(itemToInsert, 4, true) // Create a new insert transaction with the item, position, and header
        transaction.perform(transactional) // Perform the transaction

        // Revert the transaction and check if it'
