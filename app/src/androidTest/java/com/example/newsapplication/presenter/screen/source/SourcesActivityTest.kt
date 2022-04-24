package com.example.newsapplication.presenter.screen.source

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.example.newsapplication.data.response.BaseSource
import com.example.newsapplication.data.response.Source
import com.example.newsapplication.domain.Interactors
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SourcesActivityTest {
    private lateinit var scenario: ActivityScenario<SourcesActivity>
    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext

        val mockkCall = mockk<Call<BaseSource>>()
        mockkObject(Interactors)
        every { Interactors.getSources(any()).enqueue(any()) } answers {
            (firstArg() as Callback<BaseSource>).onResponse(mockkCall, Response.success(
                BaseSource(
                    status = "ok",
                    sources = listOf(
                        Source(
                            name = "Bola.com",
                            description = "Artikel olahrarga"
                        )
                    )
                )
            ))
        }
    }

    @Test
    fun success_GetSources() {
        scenario = launchActivity()

        onView(withText("Bola.com")).check(matches(isDisplayed()))
        onView(withText("Artikel olahrarga")).check(matches(isDisplayed()))
    }
}