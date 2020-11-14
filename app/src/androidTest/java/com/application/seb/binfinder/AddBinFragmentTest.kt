package com.application.seb.binfinder

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.application.seb.binfinder.controllers.activities.MainActivity
import com.application.seb.binfinder.controllers.fragments.addBin.AddBinFragment
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AddBinFragmentTest {

    @get:Rule
    var intentsTestRule = IntentsTestRule(MainActivity::class.java)

    @Test
    fun addBinFragmentTest(){

        val fragment = AddBinFragment()
        intentsTestRule.activity.supportFragmentManager.beginTransaction().add(R.id.activity_main_frameLayout, fragment).commit()

        // Base view
        Espresso.onView(ViewMatchers.withId(R.id.add_bin_fragment_constraintLayout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.add_bin_fragment_cardView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.add_bin_fragment_imageView)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.add_bin_fragment_type_title)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.add_bin_fragment_radioButton_household_waste)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.add_bin_fragment_radioButton_plastic)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.add_bin_fragment_radioButton_glass)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.add_bin_fragment_radioButton_green_waste)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.add_bin_fragment_radioButton_wild_deposit)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.add_bin_fragment_radioButton_recycling_center)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        // Save button and no photo alert dialog
        Espresso.onView(ViewMatchers.withId(R.id.add_bin_fragment_save_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText(R.string.alert_dialog_no_photo_content)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(R.string.alert_dialog_ok_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(ViewActions.click())
    }
}