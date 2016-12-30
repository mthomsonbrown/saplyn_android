package com.slashandhyphen.saplyn;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.slashandhyphen.saplyn.Authentication.AuthenticationActivity;

import org.junit.*;
import org.junit.runner.*;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Mike on 10/24/2016.
 *
 * This class tests registration scenarios.
 */
@RunWith(AndroidJUnit4.class)
public class RegisterFragmentIntegrationTest extends AuthenticationIntegrationTests {

    /**
     * This rule spawns an authentication activity
    */
    @Rule
    public ActivityTestRule<AuthenticationActivity> activityTestRule =
            new ActivityTestRule<>(AuthenticationActivity.class);

    /**
     * pulls up the register fragment with a click in the authentication activity
     */
    @Before
    public void setup() {
        onView(withId(R.id.register_button_welcome)).perform(click());

        onView(withId(R.id.fragment_register)).check(matches(isDisplayed()));
        onView(withId(R.id.fragment_login)).check(doesNotExist());
    }

    /**
     * This checks if we got to a view that has a deregister button and click it so we can
     * do new tests.
     * TODO It doesn't actually do that, but works for positive tests.
     */
    @After
    public void tearDown() {
        // If we got a successful registration, destroy it so we can continue testing...

        waitFor(R.id.fragment_dashboard);

        onView(withId(R.id.fragment_dashboard)).check(matches(isDisplayed()));
        onView(withId(R.id.button_deregister)).perform(click());
    }

    private void waitFor(int viewId) {
        waitFor(viewId, null);
    }

    private void waitFor(int viewId, Integer selectedSeconds) {
        int sleepTime = 1000;
        int frequency = 100;

        if(selectedSeconds != null) {
            sleepTime = selectedSeconds * 1000;
        }

        for(int i = 0; i < sleepTime; i += frequency) {
            try {
                onView(withId(viewId)).check(matches(isDisplayed()));
                return;
            } catch(NoMatchingViewException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(frequency);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        onView(withId(viewId)).check(matches(isDisplayed()));
    }

    /**
     * @test
     * Registering user should take them to the home activity
     */
    @Test
    public void RegisterUser() {
        fillRegistrationFields();
        onView(withId(R.id.error_text_register)).check(matches(not(isDisplayed())));
        onView(withId(R.id.register_button_register)).check(matches(isDisplayed()));
        Espresso.closeSoftKeyboard();  // THIS WAS NECESSARY
        onView(withId(R.id.register_button_register)).perform(click());
        onView(withId(R.id.activity_home)).check(matches(isDisplayed()));
    }

}
