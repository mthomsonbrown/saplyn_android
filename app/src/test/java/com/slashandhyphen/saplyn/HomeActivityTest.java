package com.slashandhyphen.saplyn;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.view.View;

import com.slashandhyphen.saplyn.Authentication.AuthenticationActivity;
import com.slashandhyphen.saplyn.Authentication.WelcomeFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;

import static org.assertj.android.api.Assertions.assertThat;

/**
 * Created by Mike on 10/19/2016.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = TestConfig.roboSdk,
        manifest  = "app/src/main/AndroidManifest.xml")
public class HomeActivityTest {
    private ActivityController homeController;
    private Activity homeActivity;

    /**
     * This creates variables to be used by the test functions
     */
    @Before
    public void setUp() {
        // Need to call start and resume to get the fragment that's been added
        homeController = Robolectric.buildActivity(AuthenticationActivity.class).
                create().start().resume().visible();
        homeActivity = (Activity) homeController.get();
    }

    /**
     * Home activity should not be null
     */
    @Test
    public void homeActivityShouldNotBeNull() {
        assertThat(homeActivity).isNotNull();
    }

    /**
     * This should return true if we get a handle on the email_text_view_home
     * @bug This returns null, but I can reference the view in live code
     */
    @Test
    public void homeActivityShouldHaveAHello() {
        View hello = homeActivity.findViewById(R.id.email_text_view_home);
        assertThat(hello).isNotNull();
    }
}
