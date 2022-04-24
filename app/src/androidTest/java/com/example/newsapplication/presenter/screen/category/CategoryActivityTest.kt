package com.example.newsapplication.presenter.screen.category

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.example.newsapplication.R
import com.example.newsapplication.presenter.screen.CategoryActivity
import org.junit.Before
import org.junit.Test

class CategoryActivityTest {
    private lateinit var scenario: ActivityScenario<CategoryActivity>
    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
    }

    @Test
    fun shouldShow_Proper_Category() {
        scenario = launchActivity()
        val categories = context.resources.getStringArray(R.array.list_category)
        categories.forEach { category ->
            onView(withText(category)).check(matches(isDisplayed()))
        }
    }
}