package com.dicoding.githubuser.view.main

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.dicoding.githubuser.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest {

    @Before
    fun setup() {
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun assertIsSearchMenuDisplayed() {
        onView(withId(R.id.search)).check(matches(isDisplayed()))
    }

    @Test
    fun assertIsFavoriteMenuDisplayed() {
        onView(withId(R.id.favorites)).check(matches(isDisplayed()))
    }
}