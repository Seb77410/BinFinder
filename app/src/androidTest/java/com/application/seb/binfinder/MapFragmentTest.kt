package com.application.seb.binfinder

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.application.seb.binfinder.controllers.activities.MainActivity
import com.application.seb.binfinder.controllers.fragments.MapFragment
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MapFragmentTest {

    @get:Rule
    var intentsTestRule = IntentsTestRule(MainActivity::class.java)

    @Test
    fun mapFragmentTest(){

        val fragment = MapFragment()
        intentsTestRule.activity.supportFragmentManager.beginTransaction().add(R.id.activity_main_frameLayout, fragment).commit()

        // Base view
        //Espresso.onView(ViewMatchers.withId(R.id.fragment_map)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        //Espresso.onView(ViewMatchers.withId(R.id.map_fragment_constraint_layout)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.map)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withId(R.id.map_fragment_fab_container)).check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
        Espresso.onView(ViewMatchers.withId(R.id.map_fragment_fab_menu)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        // Add button and alert dialog
        Espresso.onView(ViewMatchers.withId(R.id.map_fragment_fab_add)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withText(R.string.alert_dialog_add_bin_title)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(R.string.alert_dialog_add_bin_content)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(R.string.alert_dialog_yes_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(ViewMatchers.withText(R.string.alert_dialog_no_button)).check(ViewAssertions.matches(ViewMatchers.isDisplayed())).perform(ViewActions.click())

    }
}
