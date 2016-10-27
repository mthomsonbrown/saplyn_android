package com.slashandhyphen.saplyn;

import android.provider.Settings;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.slashandhyphen.saplyn.Authentication.AuthenticationActivity;
import com.slashandhyphen.saplyn.Authentication.RegisterFragment;
import com.slashandhyphen.saplyn.Models.Pojo.User;
import com.slashandhyphen.saplyn.Models.SaplynWebservice.SaplynService;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

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
public class RegisterFragmentIntegrationTest {

    /**
     * This rule spawns a RegisterFragment
    */
    @Rule
    public ActivityTestRule<AuthenticationActivity> activityTestRule =
            new ActivityTestRule<>(AuthenticationActivity.class);

    /**
     * pulls up the register fragment with a click in the authentication activity
     */
    @Before
    public void setup() {
        onView(withId(R.id.button_register_welcome)).perform(click());

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
        onView(withId(R.id.button_deregister)).perform(click());
    }

    /**
     * @test - SANITY
     * Make sure test framework is working
     */
    @Test
    public void RegisterUser() {
        onView(withId(R.id.error_text_register)).check(matches(not(isDisplayed())));
        onView(withId(R.id.register_button_register)).perform(click());
        onView(withId(R.id.activity_home)).check(matches(isDisplayed()));
    }

    /**
     * @test - SANITY
     * Really make sure test framework is working
     */
    @Test
    public void SecondRegisterUser() {
        onView(withId(R.id.error_text_register)).check(matches(not(isDisplayed())));
        onView(withId(R.id.register_button_register)).perform(click());
        onView(withId(R.id.activity_home)).check(matches(isDisplayed()));
    }


}
