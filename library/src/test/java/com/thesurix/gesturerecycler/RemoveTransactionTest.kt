package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.RemoveTransaction
import org.junit.Test
import org.mockito.Mockito
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RemoveTransactionTest : BaseTransactionTest() {

    // This test function removes an item from the transactional data list without a header, 
