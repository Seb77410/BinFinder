package com.application.seb.binfinder

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.application.seb.binfinder.conrollers.activities.MainActivity
import com.application.seb.binfinder.conrollers.fragments.AddBinFragment
import com.application.seb.binfinder.conrollers.fragments.MapFragment
import org.hamcrest.Matcher
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var intentsTestRule = IntentsTestRule(MainActivity::class.java)

    @Test
    fun mainTest() {
        onView(withId(R.id.coordinatorLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.bottomAppBar)).check(matches(isDisplayed()))
        onView(withId(R.id.activity_main_map_button)).check(matches(isDisplayed()))
        onView(withId(R.id.activity_main_frameLayout)).check(matches(isDisplayed()))
    }

    @Test
    fun mapFragmentTest(){
        val fragment = MapFragment()
        intentsTestRule.activity.supportFragmentManager.beginTransaction().add(R.id.activity_main_frameLayout, fragment).commit()

        // Base view
        onView(withId(R.id.fragment_map)).check(matches(isDisplayed()))
        onView(withId(R.id.map_fragment_constraint_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.map)).check(matches(isDisplayed()))
        onView(withId(R.id.map_fragment_fab_container)).check(matches(not(isDisplayed())))
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
        val fragment = AddBinFragment()
        intentsTestRule.activity.supportFragmentManager.beginTransaction().add(R.id.activity_main_frameLayout, fragment).commit()

        // Base view
        onView(withId(R.id.add_bin_fragment_constraintLayout)).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_cardView)).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_imageView)).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_type_title)).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_radioButton_household_waste)).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_radioButton_plastic)).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_radioButton_glass)).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_radioButton_green_waste)).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_radioButton_wild_deposit)).check(matches(isDisplayed()))
        onView(withId(R.id.add_bin_fragment_radioButton_recycling_center)).check(matches(isDisplayed()))
        // Save button and no photo alert dialog
        onView(withId(R.id.add_bin_fragment_save_button)).check(matches(isDisplayed())).perform(click())
        onView(withText(R.string.alert_dialog_no_photo_title)).check(matches(isDisplayed()))
        onView(withText(R.string.alert_dialog_no_photo_content)).check(matches(isDisplayed()))
        onView(withText(R.string.alert_dialog_ok_button)).check(matches(isDisplayed())).perform(click())

    }

}