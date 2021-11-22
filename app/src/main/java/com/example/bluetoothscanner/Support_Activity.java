package com.example.bluetoothscanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Support_Activity extends AppCompatActivity {

    TextView tvThongtin, tvKhaibao;
    Button btnBack, btnCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        tvThongtin = (TextView) findViewById(R.id.tvThongtin);
        tvThongtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://covid19.gov.vn/"));
                startActivity(intent);
            }
        });

        tvKhaibao = (TextView) findViewById(R.id.tvKhaibao);
        tvKhaibao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://tokhaiyte.vn/"));
                startActivity(intent);
            }
        });

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(Support_Activity.this, MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });

        btnCall = (Button) findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel: 19009095"));
                startActivity(intent);
            }
        });
    }
}