package com.slashandhyphen.saplyn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.slashandhyphen.saplyn.Models.Pojo.User;
import com.slashandhyphen.saplyn.Models.SaplynWebservice.RetrofitHeader;
import com.slashandhyphen.saplyn.Models.SaplynWebservice.SaplynService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VladimirActivity extends AppCompatActivity {
    private static String TAG = "~Vladimir~";
    Button mrButton;
    TextView fakeText;
    private String authToken = "b1e6668141b3dd7f8b12c13ae38bb78c";
    private static final String BASE_URL = "https://saplyn-ookamijin.c9users.io/api/v1/";
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vladimir);

        fakeText = (TextView) findViewById(R.id.hello);
        mrButton = (Button) findViewById(R.id.mr_button);
        mrButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .client(RetrofitHeader.getHeader(authToken))
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                SaplynService service = retrofit.create(SaplynService.class);

                Call<User> call = service.viewUser();

                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if(response.isSuccessful()) {
                            Log.d(TAG, "onResponse: Got response from callback " + response.code());
                            Log.d(TAG, "onResponse: " + response.body());
                            user = response.body();
                            fakeText.setText(user.getUsername());
                        }
                        else {
                            Log.d(TAG, "onResponse: Got an error code: " + response.code());
                            try {
                                Log.d(TAG, "onResponse: " + response.errorBody().string());
                            } catch (IOException e) {
                                Log.d(TAG, "onResponse: " + "...And it threw an IO Exception...");
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Log.d(TAG, "onFailure: Got failure from callback");
                        Log.d(TAG, "onFailure: t is: ", t);
                    }
                });
            }
        });
    }
}
