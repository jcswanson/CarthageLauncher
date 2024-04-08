package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.RevertReorderTransaction
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RevertReorderTransactionTest : BaseTransactionTest() {

    // Test the scenario where a reorder transaction is performed without a header
    @Test
    fun `reorder item in transaction without header`() {
        // Create a new RevertReorderTransaction instance with the 'from' index as 3, the 'to' index as 4, and 'withHeader' as false
        val transaction = RevertReorderTransaction<String>(3, 4, false)

        // Assert that the transaction cannot be performed on the transactional object
        assertFalse(transaction.perform(transactional))
    }

    // Test the scenario where a reorder transaction is reverted without a header
    @Test

