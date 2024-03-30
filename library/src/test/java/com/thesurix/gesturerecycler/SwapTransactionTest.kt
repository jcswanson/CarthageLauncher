package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.SwapTransaction
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SwapTransactionTest : BaseTransactionTest() {

    // This test checks if the item swap operation works correctly without a header
    @Test
    fun `move item in transaction without header`() {
        val firstItem = transactional.data[1] // Store the first item to be swapped
        val secondItem = transactional.data[3] // Store the second item to be swapped
        val transaction = SwapTransaction<String>(1, 3, false) // Create a new SwapTransaction instance

        // Perform the transaction and check if it's successful
        assertTrue(transaction.perform(transactional))

        // Verify if the items at the specified indices have been swapped
        assertEquals(transactional.data[1], secondItem)
        assertEquals(transactional.data[3], firstItem)

        // Verify if the 'notifyChanged' method has been called for the corresponding indices
        Mockito.verify(transactional).notifyChanged(1)
        Mockito.verify(transactional).notifyChanged(3)
    }

    // This test checks if the item swap operation can be reverted correctly without a header
    @Test
    fun `revert move item in transaction without header`() {
        val firstItem = transactional.data[1] // Store the first item to be swapped
        val secondItem = transactional.data[3] // Store the second item to be swapped
        val transaction = SwapTransaction<String>(1, 3, false) // Create a new SwapTransaction instance

        // Perform the transaction and check if it's successful
        assertTrue(transaction.revert(transactional))

        // Verify if the items at the specified indices have been swapped back to their original positions
        assertEquals(transactional.data[1], secondItem)
        assertEquals(transactional.data[3], firstItem)

        // Verify if the 'notifyChanged' method has been called for the corresponding indices
        Mockito.verify(transactional).notifyChanged(3)
        Mockito.verify(transactional).notifyChanged(1)
    }

    // This test checks if the item swap operation works correctly with a header
    @Test
    fun `move item in transaction with header`() {
        val firstItem = transactional.data[1] // Store the first item to be swapped
        val secondItem = transactional.data[3] // Store the second item to be swapped
        val transaction = SwapTransaction<String>(1, 3, true) // Create a new SwapTransaction instance with a header

        // Perform the transaction and check if it's successful
        assertTrue(transaction.perform(transactional))

        // Verify if the items at the specified indices have been swapped
        assertEquals(transactional.data[1], secondItem)
        assertEquals(transactional.data[3], firstItem)

        // Verify if the 'notifyChanged' method has been called for the corresponding indices + header
        Mockito.verify(transactional).notifyChanged(2)
        Mockito.verify(transactional).notifyChanged(4)
    }

    // This test checks if the item swap operation can be reverted correctly with a header
    @Test
    fun `revert move item in transaction with header`() {
        val firstItem = transactional.data[1] // Store the first item to be swapped
        val secondItem = transactional.data[3] // Store the second item to be swapped
        val transaction = SwapTransaction<String>(1, 3, true) // Create a new SwapTransaction instance with a header

        // Perform the transaction and check if
