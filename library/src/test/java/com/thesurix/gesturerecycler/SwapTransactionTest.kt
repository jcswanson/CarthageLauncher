package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.SwapTransaction
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SwapTransactionTest : BaseTransactionTest() {

    @Test
    fun testMoveItemWithoutHeader() {
        setUpData(4) // Initialize the data array with 4 elements

        val firstItem = transactional.data[1]
        val secondItem = transactional.data[3]
        val transaction = SwapTransaction<String>(1, 3, false)

        transaction.perform(transactional)
        assertTrue(transaction.isSuccessful)

        assertEquals(transactional.data[1], secondItem)
        assertEquals(transactional.data[3], firstItem)

        Mockito.verify(transactional).notifyChanged(1)
        Mockito.verify(transactional).notifyChanged(3)
    }

    @Test

