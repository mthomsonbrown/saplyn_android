package com.slashandhyphen.saplyn;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.slashandhyphen.saplyn.Authentication.AuthenticationActivity;

import org.junit.*;
import org.junit.runner.*;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by Mike on 10/27/2016.
 *
 * This class tests login scenarios
 */
@RunWith(AndroidJUnit4.class)
public class LoginFragmentIntegrationTest {

    /**
     * This rule spawns an authentication activity
     */
    @Rule
    public ActivityTestRule<AuthenticationActivity> activityTestRule =
            new ActivityTestRule<>(AuthenticationActivity.class);

    /**
     * registers the user, logs them out, and then brings up the login fragment
     */
    @Before
    public void setup() {
        // TODO Registration should happen in a @BeforeClass
        onView(withId(R.id.register_button_welcome)).perform(click());
        onView(withId(R.id.register_button_register)).perform(click());
        onView(withId(R.id.button_logout)).perform(click());
        onView(withId(R.id.login_button_welcome)).perform(click());
    }

    /**
     * This logs the user out and also deregisters them.  Deregistration will be done in a class
     * level function in the future, but right now there's only one test, so why bother?
     */
    @After
    public void tearDown() {
        onView(withId(R.id.button_logout)).perform(click());
    }

    /**
     * @test
     * Logging in user should take them to home activity
     */
    @Test
    public void LoginUser() {
        onView(withId(R.id.error_text_login)).check(matches(not(isDisplayed())));
        onView(withId(R.id.login_button_login)).perform(click());
        onView(withId(R.id.activity_home)).check(matches(isDisplayed()));
    }
}
