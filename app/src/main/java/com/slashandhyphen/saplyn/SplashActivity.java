package com.slashandhyphen.saplyn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
     *
     * @param savedInstanceState used to reload state information during lifecycle transitions
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        start = (Button) findViewById(R.id.splash_button);
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
     *
     * @param view Right now, the start button.
     */
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
