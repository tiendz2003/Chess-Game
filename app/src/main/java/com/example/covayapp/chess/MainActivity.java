package com.example.covayapp.chess;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.covayapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnDouble;
    private Button btnExit;
    private Button btnRecord;
    private Button btnSetting;
    private Button btnSingle0;
    private Button btnSingle1;
    private Button btnSingle2;


    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int i = metrics.widthPixels;
        int i2 = metrics.heightPixels - 50;
        this.btnSingle0 = (Button) findViewById(R.id.button1);
        this.btnSingle0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                Bundle b = new Bundle();
                b.putInt("type", 0);
                intent.putExtras(b);
                MainActivity.this.startActivity(intent);
            }
        });
        this.btnSingle1 = (Button) findViewById(R.id.button2);
        this.btnSingle1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                /*Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                Bundle b = new Bundle();
                b.putInt("type", 1);
                intent.putExtras(b);
                MainActivity.this.startActivity(intent);*/
                Intent intent = new Intent(MainActivity.this, GuideActivity.class);
                startActivity(intent);
            }
        });
        this.btnSingle2 = (Button) findViewById(R.id.button3);
        this.btnSingle2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                Bundle b = new Bundle();
                b.putInt("type", 2);
                intent.putExtras(b);
                MainActivity.this.startActivity(intent);
            }
        });
        this.btnDouble = (Button) findViewById(R.id.button4);
        this.btnDouble.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BoardActivity.class);
                Bundle b = new Bundle();
                b.putInt("type", -1);
                intent.putExtras(b);
                MainActivity.this.startActivity(intent);
            }
        });
        this.btnRecord = (Button) findViewById(R.id.button5);
        this.btnRecord.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecordActivity.class);
                intent.putExtras(new Bundle());
                MainActivity.this.startActivity(intent);
            }
        });
        this.btnExit = (Button) findViewById(R.id.button6);
        this.btnExit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.finish();
            }
        });
    }
}
