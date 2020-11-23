package com.application.seb.binfinder

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.application.seb.binfinder.controllers.activities.MainActivity
import com.application.seb.binfinder.controllers.activities.binDetails.BinDetailsActivity
import com.application.seb.binfinder.utils.Constants
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BinDetailsActivityTest {

    private val intent = Intent(ApplicationProvider.getApplicationContext(), BinDetailsActivity::class.java)
            .putExtra(Constants.BIN_ID, "nJxVEhzk36jAHrMGeH4i")

    @get:Rule
    var intentsTestRule = ActivityScenarioRule<BinDetailsActivity>(intent)

    @Test
    fun mainTest() {

        onView(withId(R.id.bin_details_activity_appbar)).check(matches(isDisplayed()))
        onView(withId(R.id.bin_details_activity_cardView)).check(matches(isDisplayed()))
        onView(withId(R.id.bin_details_activity_photo)).check(matches(isDisplayed()))
        onView(withId(R.id.bin_details_activity_like_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.bin_details_activity_like_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.bin_details_activity_like_button)).check(matches(isDisplayed()))
        onView(withId(R.id.bin_details_activity_dislike_button)).check(matches(isDisplayed()))
        onView(withId(R.id.bin_details_activity_address_title)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.bin_details_activity_address_content)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.bin_details_activity_bin_waste_title)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.bin_details_activity_bin_comment_title)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.bin_details_activity_bin_comment_content)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))

    }
}