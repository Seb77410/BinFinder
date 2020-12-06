package com.application.seb.binfinder

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.application.seb.binfinder.controllers.activities.AddCleanEventActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class AddCleanEventActivityTest {

    private val intent = Intent(ApplicationProvider.getApplicationContext(), AddCleanEventActivity::class.java)

    @get:Rule
    var intentsTestRule = ActivityScenarioRule<AddCleanEventActivity>(intent)

    @Test
    fun addCleanEventTest() {
        ActivityScenario.launch(AddCleanEventActivity::class.java)

        onView(withId(R.id.add_clean_event_activity_toolbar)).check(matches(isDisplayed()))
        onView(withText(R.string.add_clean_event_activity_title)).check(matches(isDisplayed()))

        onView(withId(R.id.add_clean_event_activity_image)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.add_clean_event_activity_title_value)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.add_clean_event_activity_date)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.add_clean_event_activity_address)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.add_clean_event_activity_description)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.add_clean_event_activity_save_fab)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))

    }

}