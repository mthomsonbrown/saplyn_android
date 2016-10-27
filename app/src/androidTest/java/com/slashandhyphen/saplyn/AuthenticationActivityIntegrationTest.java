package com.slashandhyphen.saplyn;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.slashandhyphen.saplyn.Authentication.AuthenticationActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.*;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

/**
 * Created by Mike on 10/24/2016.
 *
 * This test suite covers the basics of authentication: register, deregister, login, logout.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class AuthenticationActivityIntegrationTest {

    /**
     * This rule spawns an AuthenticationActivity
     */
    @Rule
    public ActivityTestRule<AuthenticationActivity> activityTestRule =
            new ActivityTestRule<>(AuthenticationActivity.class);

    /**
     * Basic sanity check
     */
    @Test
    public void UseEspresso() {
        onView(withId(R.id.fragment_welcome))
                .check(matches(isDisplayed()));
    }

    /**
     * Click the register button
     * - The register fragment is presented
     */
    @Test
    public void CallRegisterFragment() {
        onView(withId(R.id.button_register_welcome)).perform(click());
        onView(withId(R.id.fragment_register)).check(matches(isDisplayed()));
    }

    /**
     * Click the login button
     * - The login fragment is presented
     */
    @Test
    public void CallLoginFragment() {
        onView(withId(R.id.button_login_welcome)).perform(click());
        onView(withId(R.id.fragment_login)).check(matches(isDisplayed()));
    }

    /**
     * Click the register button
     * - The register fragment should be presented
     * TODO This should be in a register fragment testing class
     */
    @Test
    public void ClickRegisterButtonFromRegister() {
        onView(withId(R.id.button_register_welcome)).perform(click());
        onView(withId(R.id.register_button_register)).check(matches(isDisplayed()));

        onView(withId(R.id.register_button_register)).perform(click());
        onView(withId(R.id.fragment_register)).check(matches(isDisplayed()));
    }

}
