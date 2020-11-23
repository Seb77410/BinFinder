package com.application.seb.binfinder

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.application.seb.binfinder.controllers.activities.cleanEventDetails.CleanEventDetailsActivity
import com.application.seb.binfinder.models.CleanEvent
import com.application.seb.binfinder.utils.Constants
import com.application.seb.binfinder.utils.Utils
import com.google.firebase.firestore.GeoPoint
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CleanEventDetailsActivityTest {

    private val participantsList : MutableList<String> = ArrayList()

    private val event = CleanEvent(
            "bzsnTKiaQQdSeZ8flhye",
            "20201115",
            "Seb",
            "RTWE6TfBvCOY3jyozglIQDUf9KNq1",
            20201130,
            addIdToParticipantsList(),
            "description",
            "title",
            "address",
            GeoPoint(48.9562018, 2.8884657),
            null
    )

    private val intent = Intent(ApplicationProvider.getApplicationContext(), CleanEventDetailsActivity::class.java)
            .putExtra(Constants.ARGS_EVENT, Utils.convertCleanEventToString(event))

    @get:Rule
    var intentsTestRule = ActivityScenarioRule<CleanEventDetailsActivity>(intent)

    @Test
    fun mainTest() {

        // Event Image
        onView(withId(R.id.clean_event_details_activity_image_container)).check(matches(isDisplayed()))

        // Event Title
        onView(withId(R.id.clean_event_details_activity_title)).check(matches(isDisplayed()))
        onView(withText(event.title)).check(matches(isDisplayed()))

        // Event Details
        onView(withId(R.id.clean_event_details_activity_details_container)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withText(R.string.details)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.clean_event_details_activity_date)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withText(Utils.convertDataDateToFormatString(event.eventDate))).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.clean_event_details_activity_address)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withText(event.address)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.clean_event_details_activity_participants_number)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withText(participantsList.size.toString())).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.clean_event_details_activity_participate_button)).perform(NestedScrollViewExtension()).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))

        // Event Description
        onView(withId(R.id.clean_event_details_activity_description_container)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withText(R.string.description)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.clean_event_details_activity_createBy)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withText("Seb")).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.clean_event_details_activity_createDate)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withText(Utils.convertDataDateToFormatString(event.createDate.toInt()))).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))

        // Event Comments
        onView(withId(R.id.clean_event_details_activity_comments_container)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withText(R.string.comments)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.clean_event_details_activity_comments_recycler_view)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.clean_event_details_activity_comments_editText_container)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.clean_event_details_activity_comments_editText)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.clean_event_details_activity_send_comment_button)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
    }

    private fun addIdToParticipantsList(): MutableList<String> {
        participantsList.add("RTWE6TfBvCOY3jyozgIQDUf9KNq1")
        return participantsList
    }
}
