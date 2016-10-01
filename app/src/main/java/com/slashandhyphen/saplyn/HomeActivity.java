package com.slashandhyphen.saplyn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.slashandhyphen.saplyn.Models.Pojo.User;
import com.slashandhyphen.saplyn.Models.SaplynWebservice.SaplynService;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeActivity extends AppCompatActivity {
    private static String TAG = "~Home~";
    TextView fakeText;
    TextView fakeCreated;
    private Observable<User> userListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        SaplynService saplynService = new SaplynService();

        fakeText = (TextView) findViewById(R.id.hello);
        fakeCreated = (TextView) findViewById(R.id.created);

        userListener = saplynService.viewUser();
        populateUser();
    }

    private void populateUser() {
        userListener.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                            fakeText.setText(user.getUsername());
                            fakeCreated.setText(user.getCreatedAt());
                        },
                        throwable -> Log.e(TAG, "onErrorFromPopulateUser: "
                                + throwable.getMessage()));
    }

}
