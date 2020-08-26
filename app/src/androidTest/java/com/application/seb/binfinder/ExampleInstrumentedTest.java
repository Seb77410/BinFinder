package com.application.seb.binfinder;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.application.seb.binfinder.conrollers.activities.SignInActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public IntentsTestRule<SignInActivity> intentsTestRule = new IntentsTestRule<>(SignInActivity.class);


    @Test
    public void sign_in_test(){
        onView(withId(R.id.sign_in_activity_google_button)).check(matches(isDisplayed()));
        onView(withId(R.id.sign_in_activity_image)).check(matches(isDisplayed()));
        onView(withId(R.id.sign_in_activity_mail_button)).check(matches(isDisplayed()));
        onView(withId(R.id.sign_in_activity_skip_button)).check(matches(isDisplayed()));

    }
}