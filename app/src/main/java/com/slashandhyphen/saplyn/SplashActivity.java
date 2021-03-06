package com.slashandhyphen.saplyn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.slashandhyphen.saplyn.HomeRealm.HomeActivity;

/**
 * Created by Mike on 9/30/2016.
 *
 * This is the first activity that's presented.  It should act as a loading screen while the user
 * object is populated for the home activity.  It's probably superfluous and will be removed at
 * some point.
 */
public class SplashActivity extends Activity implements View.OnClickListener {
    Button start;

    /**
     * Adds a button to get to user space.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        start = (Button) findViewById(R.id.home_button_splash);
        start.setOnClickListener(this);
    }

    /**
     * May at some point preload user data.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Starts the home activity.
     */
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
