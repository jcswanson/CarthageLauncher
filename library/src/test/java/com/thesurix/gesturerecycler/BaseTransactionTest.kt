package com.thesurix.gesturerecycler;

import com.thesurix.gesturerecycler.transactions.Transactional;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class) // This annotation is used to run this test class with Mockito JUnit Runner
abstract class BaseTransactionTest {

    @Mock // This annotation indicates that the following variable should be mock object
    lateinit var transactional: Transactional<String> // A mock object that implements the Transactional interface for String type

    val data = mutableListOf("A", "B", "C", "D", "E") // A mutable list of Strings

    @Before // This annotation indicates that the following method should be run before each test method
    fun setUp() {
        Mockito.`when`(transactional.data) // This line sets up a behavior for the mock object
            .thenReturn(data) // When the data property of the mock object is accessed, return the data list
    }
}
