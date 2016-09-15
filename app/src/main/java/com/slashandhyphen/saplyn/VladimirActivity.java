package com.slashandhyphen.saplyn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.slashandhyphen.saplyn.Models.FakeModel;

public class VladimirActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vladimir);

        TextView fakeText = (TextView) findViewById(R.id.hello);
        fakeText.setText(new FakeModel().getStuffFromModel());
    }
}
