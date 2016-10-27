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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mike on 10/20/2016.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = TestConfig.roboSdk,
        manifest  = "app/src/main/AndroidManifest.xml")
public class AuthenticationActivityTest {
    private ActivityController activityController;
    private Activity authenticationActivity;

    /**
     * This creates variables to be used by the test functions
     */
    @Before
    public void setUp() {
        // Need to call start and resume to get the fragment that's been added
        activityController = Robolectric.buildActivity(AuthenticationActivity.class).
                create().start().resume().visible();
        authenticationActivity = (Activity) activityController.get();
    }

    @Test
    public void FragmentContainerShouldExist() {
        View fragment = authenticationActivity.findViewById(R.id.main_fragment_container_authentication);
        assertThat(fragment).isNotNull();
    }
    /**
     * Calling login fragment should work
     */
    @Test
    public void GetLoginFragmentFromActivity() {
        Fragment loginFragment;

        loginFragment = authenticationActivity
                .getFragmentManager()
                .findFragmentById(R.id.main_fragment_container_authentication);

        FragmentTransaction ft = authenticationActivity
                .getFragmentManager()
                .beginTransaction();
        ft.add(R.id.main_fragment_container_authentication, new WelcomeFragment());
        ft.commit();

        assertThat(loginFragment).isNotNull();
    }
}
