package com.slashandhyphen.saplyn;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.slashandhyphen.saplyn.Models.Pojo.User;
import com.slashandhyphen.saplyn.Models.SaplynWebservice.RetrofitHeader;
import com.slashandhyphen.saplyn.Models.SaplynWebservice.SaplynService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class VladimirActivity extends AppCompatActivity {
    private static String TAG = "~Vladimir~";
    Button mrButton;
    TextView fakeText;
    private String authToken = "b1e6668141b3dd7f8b12c13ae38bb78c";
    private static final String BASE_URL = "https://saplyn-ookamijin.c9users.io/api/v1/";
    private Observable<User> userListener;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vladimir);

        fakeText = (TextView) findViewById(R.id.hello);
        mrButton = (Button) findViewById(R.id.mr_button);
        mrButton.setOnClickListener(v -> {
            Retrofit retrofit = new Retrofit.Builder()
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .client(RetrofitHeader.getHeader(authToken))
                    .build();

            SaplynService service = retrofit.create(SaplynService.class);

            userListener = service.viewUser();

            userListener.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(randomUser -> {
                        user = randomUser;
                        fakeText.setText(user.getUsername());
                    }, throwable -> Log.e(TAG, "onErrorFromTheGETUserCall: "
                            + throwable.getMessage()));
        });
    }
}
