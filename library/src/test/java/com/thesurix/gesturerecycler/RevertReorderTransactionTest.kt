package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.RevertReorderTransaction
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RevertReorderTransactionTest : BaseTransactionTest() {

    @Test
    fun `reorder item in transaction without header - cannot perform`() {
        val transaction = RevertReorderTransaction<String>(3, 4, false)
        assertFalse(transaction.perform(transactional))
    }

    @Test

