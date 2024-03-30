package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.MoveTransaction
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MoveTransactionTest : BaseTransactionTest() {

    // Test method to move an item in a transaction without a header
    @Test
    fun `move item in transaction without header`() {
        // Define the item to be moved
        val item = transactional.data[1]
        
        // Create a MoveTransaction object to move the item from index 1 to index 4
        val transaction = MoveTransaction<String>(1, 4, false)

        // Perform the transaction and check if it's successful
        assertTrue(transaction.perform(transactional))

        // Check if the item has been moved to the correct index
        assertEquals(transactional.data[4], item)

        // Verify that the RecyclerView has been notified of the move
        Mockito.verify(transactional).notifyMoved(1, 4)
    }

    // Test method to revert a move item in a transaction without a header
    @Test
    fun `revert move item in transaction without header`() {
        // Define the item to be moved
        val item = transactional.data[4]

        // Create a MoveTransaction object to move the item from index 4 to index 1
        val transaction = MoveTransaction<String>(1, 4, false)

        // Revert the transaction and check if it's successful
        assertTrue(transaction.revert(transactional))

        // Check if the item has been moved back to the original index
        assertEquals(transactional.data[1], item)

        // Verify that the RecyclerView has been notified of the move
        Mockito.verify(transactional).notifyMoved(4, 1)
    }

    // Test method to move an item in a transaction with a header
    @Test
    fun `move item in transaction with header`() {
        // Define the item to be moved
        val item = transactional.data[1]

        // Create a MoveTransaction object to move the item from index 1 to index 4 with a header
        val transaction = MoveTransaction<String>(1, 4, true)

        // Perform the transaction and check if it's successful
        assertTrue(transaction.perform(transactional))

        // Check if the item has been moved to the correct index
        assertEquals(transactional.data[4], item)

        // Verify that the RecyclerView has been notified of the move
        Mockito.verify
