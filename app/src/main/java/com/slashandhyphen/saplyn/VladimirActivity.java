package com.slashandhyphen.saplyn;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.slashandhyphen.saplyn.Models.FakeModel;

public class VladimirActivity extends AppCompatActivity {
    Button mrButton;
    TextView fakeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vladimir);

        fakeText = (TextView) findViewById(R.id.hello);
        mrButton = (Button) findViewById(R.id.mr_button);
        mrButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String test = new FakeModel().getStuffFromModel();
                fakeText.setText(test);
            }
        });
    }
}
