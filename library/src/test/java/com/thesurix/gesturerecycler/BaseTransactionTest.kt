package com.thesurix.gesturerecycler

import com.thesurix.gesturerecycler.transactions.Transactional
import org.junit.Before
import org.junit.runners.MockitoJUnitRunner
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class) // Use Mockito JUnit Runner to initialize mock objects
abstract class BaseTransactionTest {

    @Mock
    private lateinit var transactional: Transactional<String> // Use private access modifier for better encapsulation

    private val data = mutableListOf("A", "B", "C", "D", "E") // Use private access modifier for better encapsulation

    @Before
    fun setUp() {
        Mockito.`when`(transactional.data)
            .thenReturn(data)
    }

    protected fun <T> any(): T = Mockito.any<T>() // Define a utility function to create a mockito any() object

    protected fun <T> eq(value: T): T = Mockito.eq(value) // Define a utility function to create a mockito eq() object
}
