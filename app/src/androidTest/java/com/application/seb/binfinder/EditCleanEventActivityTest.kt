package com.application.seb.binfinder

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.application.seb.binfinder.controllers.activities.addCleanEvent.AddCleanEventActivity
import com.application.seb.binfinder.models.CleanEvent
import com.application.seb.binfinder.utils.Constants
import com.application.seb.binfinder.utils.Utils
import com.google.firebase.firestore.GeoPoint
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class EditCleanEventActivityTest {

    private val event = CleanEvent(
            "bzsnTKiaQQdSeZ8flhye",
            "20201115",
            "Seb",
            "RTWE6TfBvCOY3jyozglIQDUf9KNq1",
            20201130,
            null,
            "description",
            "title",
            "address",
            GeoPoint(48.9562018, 2.8884657),
            null
    )
    private val intent = Intent(ApplicationProvider.getApplicationContext(), AddCleanEventActivity::class.java)
            .putExtra(Constants.ARGS_EVENT, Utils.convertCleanEventToString(event))

    @get:Rule
    var intentsTestRule = ActivityScenarioRule<AddCleanEventActivity>(intent)

    @Test
    fun mainTest() {
        // Toolbar
        onView(withId(R.id.add_clean_event_activity_toolbar)).check(matches(ViewMatchers.isDisplayed()))
        onView(ViewMatchers.withText(R.string.edit_clean_event_activity_title)).check(matches(ViewMatchers.isDisplayed()))
        // Image
        onView(withId(R.id.add_clean_event_activity_image)).perform(NestedScrollViewExtension()).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.add_clean_event_activity_title_value)).perform(NestedScrollViewExtension()).check(matches(ViewMatchers.isDisplayed()))
        // Title
        onView(ViewMatchers.withText(event.title)).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.add_clean_event_activity_date)).perform(NestedScrollViewExtension()).check(matches(ViewMatchers.isDisplayed()))
        // Event Date
        onView(ViewMatchers.withText(Utils.convertDataDateToFormatString(event.eventDate))).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.add_clean_event_activity_address)).perform(NestedScrollViewExtension()).check(matches(ViewMatchers.isDisplayed()))
        // Address
        onView(ViewMatchers.withText(event.address)).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.add_clean_event_activity_description)).perform(NestedScrollViewExtension()).check(matches(ViewMatchers.isDisplayed()))
        // Description
        onView(ViewMatchers.withText(event.description)).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.add_clean_event_activity_save_fab)).perform(NestedScrollViewExtension()).check(matches(ViewMatchers.isDisplayed()))

    }

}