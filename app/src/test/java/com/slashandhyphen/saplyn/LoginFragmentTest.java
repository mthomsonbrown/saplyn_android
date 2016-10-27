package com.slashandhyphen.saplyn;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.slashandhyphen.saplyn.Authentication.AuthenticationActivity;
import com.slashandhyphen.saplyn.Authentication.LoginFragment;
import com.slashandhyphen.saplyn.Authentication.WelcomeFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.util.ActivityController;
import org.robolectric.util.FragmentController;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Mike on 10/20/2016.
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = TestConfig.roboSdk,
        manifest  = "app/src/main/AndroidManifest.xml")
public class LoginFragmentTest {
    private FragmentController fragmentController;
    private Fragment loginFragment;
    private FragmentController<LoginFragment> theLoginFragment;

    private ActivityController activityController;
    private Activity authenticationActivity;

    /**
     * This creates variables to be used by the test functions
     */
    @Before
    public void setUp() {
        activityController = Robolectric.buildActivity(AuthenticationActivity.class).
                create().start().resume().visible();

        authenticationActivity = (Activity) activityController.get();

        loginFragment = authenticationActivity
                .getFragmentManager()
                .findFragmentById(R.id.main_fragment_container_authentication);

        FragmentTransaction ft = authenticationActivity
                .getFragmentManager()
                .beginTransaction();
        ft.add(R.id.main_fragment_container_authentication, new WelcomeFragment());
        ft.commit();

        // TODO how to fragment testing?
        theLoginFragment = Robolectric.buildFragment(LoginFragment.class)
                .create();
    }

    @Test
    public void SanityTest() {
        assertThat(1 == 1);
        assertThat(theLoginFragment).isNotNull();
//        loginFragment = (Fragment) fragmentController.get();
//        assertThat(loginFragment).isNotNull();
    }
}
