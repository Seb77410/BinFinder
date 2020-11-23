package com.application.seb.binfinder

import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.application.seb.binfinder.controllers.activities.MainActivity
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    private val intent = Intent(ApplicationProvider.getApplicationContext(), MainActivity::class.java)

    @get:Rule
    var intentsTestRule = ActivityScenarioRule<MainActivity>(intent)

    @Test
    fun mainTest() {
        onView(withId(R.id.coordinatorLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.bottomAppBar)).check(matches(isDisplayed()))
        onView(withId(R.id.activity_main_map_button)).check(matches(isDisplayed()))
        onView(withId(R.id.activity_main_frameLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun mapFragmentTest(){
        // Base view
        onView(withId(R.id.fragment_map)).check(matches(isDisplayed()))
        onView(withId(R.id.map_fragment_constraint_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.map_fragment_fab_container)).check(matches(Matchers.not(isDisplayed())))
        onView(withId(R.id.map_fragment_fab_menu)).check(matches(isDisplayed()))

        // Add button and alert dialog
        onView(withId(R.id.map_fragment_fab_add)).check(matches(isDisplayed())).perform(click())
        onView(withText(R.string.alert_dialog_add_bin_title)).check(matches(isDisplayed()))
        onView(withText(R.string.alert_dialog_add_bin_content)).check(matches(isDisplayed()))
        onView(withText(R.string.alert_dialog_yes_button)).check(matches(isDisplayed()))
        onView(withText(R.string.alert_dialog_no_button)).check(matches(isDisplayed())).perform(click())
    }

    @Test
    fun addBinFragmentTest(){
        onView(withId(R.id.map_fragment_fab_add)).check(matches(isDisplayed())).perform(click())
        onView(withText(R.string.alert_dialog_yes_button)).check(matches(isDisplayed())).perform(click())
        onView(withId(R.id.fragment_add_bin)).check(matches(isDisplayed()))

        // Base view tests
        onView(withId(R.id.add_bin_fragment_constraintLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_cardView)).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_imageView)).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_type_title)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_radioButton_household_waste)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_radioButton_plastic)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_radioButton_glass)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_radioButton_green_waste)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_radioButton_wild_deposit)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_radioButton_recycling_center)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))

        // Save button and no photo alert dialog tests
        onView(withId(R.id.add_bin_fragment_save_button)).check(matches(isDisplayed())).perform(click())
        onView(withText(R.string.alert_dialog_no_photo_content)).check(matches(isDisplayed()))
        onView(withText(R.string.alert_dialog_ok_button)).check(matches(isDisplayed())).perform(click())

        // Radio buttons selection tests
        onView(withId(R.id.add_bin_fragment_radioButton_household_waste)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_radioButton_plastic)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_radioButton_glass)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_radioButton_green_waste)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_radioButton_wild_deposit)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_radioButton_recycling_center)).perform(NestedScrollViewExtension()).check(matches(isDisplayed()))
    }

    @Test
    fun cleanEventFragmentTest(){
        // Start Fragment
        onView(withId(R.id.menu_main_clean_events)).check(matches(isDisplayed())).perform(click())
        // Title
        onView(withId(R.id.clean_event_frag_activity_title)).check(matches(isDisplayed()))
        // Add event Button
        onView(withId(R.id.clean_event_frag_fab_add)).check(matches(isDisplayed()))
        // Tab layout and ViewPager
        onView(withId(R.id.clean_event_frag_table_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.clean_event_frag_viewpager)).check(matches(isDisplayed()))
        onView(withText(R.string.page_title_soon)).check(matches(isDisplayed()))
        onView(withText(R.string.page_title_my_clean_events)).check(matches(isDisplayed())).perform(click())


    }

}