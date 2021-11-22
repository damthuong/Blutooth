package com.example.bluetoothscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Lich_su_quet extends AppCompatActivity {
    Button btnBack;
    TextView tvList;
    ListView lvList;

    ArrayList<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lich_su_quet);

        btnBack = (Button) findViewById(R.id.btnbackHis);
        tvList = (TextView) findViewById(R.id.tvList);
       // lvList = (ListView) findViewById(R.id.lvHis);
        tvList.append("Lịch sử tiếp xúc gần: \n\n");
        Intent intent = getIntent();
        String value = intent.getStringExtra("value");
        tvList.append(value);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}