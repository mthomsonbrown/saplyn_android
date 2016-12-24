package com.slashandhyphen.saplyn;

import android.support.test.rule.ActivityTestRule;

import com.slashandhyphen.saplyn.Authentication.AuthenticationActivity;
import com.slashandhyphen.saplyn.Models.Pojo.User;

import org.junit.Rule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by Mike on 10/28/2016.
 *
 * Holds common authentication test resources
 */

public class AuthenticationIntegrationTests {

    private User testUser = new User("test@user.com", "qwertyuiop", "qwertyuiop");

    /**
     * This rule spawns an AuthenticationActivity
     */
    @Rule
    public ActivityTestRule<AuthenticationActivity> activityTestRule =
            new ActivityTestRule<>(AuthenticationActivity.class);

    protected void fillRegistrationFields() {
        onView(withId(R.id.email_edit_text_register)).perform(typeText(testUser.getEmail()));
        onView(withId(R.id.password_edit_text_register)).perform(typeText(testUser.getPassword()));
        onView(withId(R.id.password_confirmation_edit_text_register)).perform(typeText(testUser.getPasswordConfirmation()));
    }

    protected void fillLoginFields() {
        onView(withId(R.id.email_edit_text_login)).perform(typeText(testUser.getEmail()));
        onView(withId(R.id.password_edit_text_login)).perform(typeText(testUser.getPassword()));
    }
}
