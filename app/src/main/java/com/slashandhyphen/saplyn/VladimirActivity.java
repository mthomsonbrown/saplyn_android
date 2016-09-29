package com.slashandhyphen.saplyn;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.slashandhyphen.saplyn.Models.Pojo.User;
import com.slashandhyphen.saplyn.Models.SaplynWebservice.SaplynService;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.slashandhyphen.saplyn.R.id.mr_button;

public class VladimirActivity extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "~Vladimir~";
    Button mrButton;
    Button mrCreateTime;
    TextView fakeText;
    TextView fakeCreated;
    private Observable<User> userListener;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vladimir);
        SaplynService saplynService = new SaplynService();

        fakeText = (TextView) findViewById(R.id.hello);
        mrButton = (Button) findViewById(mr_button);
        mrButton.setOnClickListener(this);

        fakeCreated = (TextView) findViewById(R.id.created);
        mrCreateTime = (Button) findViewById(R.id.mr_create_time);
        mrCreateTime.setOnClickListener(this);

        userListener = saplynService.viewUser();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mr_button:
                doMrButton();
                break;
            case R.id.mr_create_time:
                doCreatedTime();
                break;
        }
    }

    private void doCreatedTime() {
        userListener.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                            fakeText.setText(user.getUsername());
                        },
                        throwable -> Log.e(TAG, "onErrorFromTheGetLiveTime: "
                                + throwable.getMessage()));
    }

    private void doMrButton() {
        userListener.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                            fakeCreated.setText(user.getLiveTime());
                        },
                        throwable -> Log.e(TAG, "onErrorFromTheGETUserCall: "
                                + throwable.getMessage()));
    }
}
