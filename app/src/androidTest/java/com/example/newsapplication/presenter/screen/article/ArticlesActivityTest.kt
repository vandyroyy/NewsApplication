package com.example.newsapplication.presenter.screen.article

import android.content.Context
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.platform.app.InstrumentationRegistry
import com.example.newsapplication.data.response.Article
import com.example.newsapplication.data.response.BaseArticles
import com.example.newsapplication.domain.Interactors
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticlesActivityTest {
    private lateinit var scenario: ActivityScenario<ArticleActivity>
    private lateinit var context: Context

    @Before
    fun setup() {
        context = InstrumentationRegistry.getInstrumentation().targetContext

        val mockkCall = mockk<Call<BaseArticles>>()
        mockkObject(Interactors)
        every { Interactors.getTopHeadline(any(), any(), any(), any()).enqueue(any()) } answers {
            (firstArg() as Callback<BaseArticles>).onResponse(mockkCall, Response.success(
                BaseArticles(
                    status = "ok",
                    articles = listOf(
                        Article(
                            title = "7 Cara menghilangkan stress dengan olah raga",
                            content = "Berikut cara menghilangkan stres dengan berolahraga, yang patut untuk Anda coba."
                        )
                    )
                )
            ))
        }
    }

    @Test
    fun success_GetSources() {
        scenario = launchActivity()

        onView(withText("7 Cara menghilangkan stress dengan olah raga")).check(matches(isDisplayed()))
        onView(withText("Berikut cara menghilangkan stres dengan berolahraga, yang patut untuk Anda coba.")).check(matches(isDisplayed()))
    }
}