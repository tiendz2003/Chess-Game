package com.example.covayapp.chess;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.example.covayapp.R;


public class RecordActivity extends AppCompatActivity {
    TextView tv32;
    TextView tv33;
    TextView tv34;
    TextView tv42;
    TextView tv43;
    TextView tv44;
    TextView tv52;
    TextView tv53;
    TextView tv54;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        RecordDetail rd0 = Record.getRecord(this, PlayMode.SINGLE_0);
        RecordDetail rd1 = Record.getRecord(this, PlayMode.SINGLE_1);
        RecordDetail rd2 = Record.getRecord(this, PlayMode.SINGLE_2);
        this.tv32 = (TextView) findViewById(R.id.textView32);
        this.tv33 = (TextView) findViewById(R.id.textView33);
        this.tv34 = (TextView) findViewById(R.id.textView34);
        this.tv42 = (TextView) findViewById(R.id.textView42);
        this.tv43 = (TextView) findViewById(R.id.textView43);
        this.tv44 = (TextView) findViewById(R.id.textView44);
        this.tv52 = (TextView) findViewById(R.id.textView52);
        this.tv53 = (TextView) findViewById(R.id.textView53);
        this.tv54 = (TextView) findViewById(R.id.textView54);
        this.tv32.setText(String.valueOf(rd0.win));
        this.tv33.setText(String.valueOf(rd0.lose));
        this.tv34.setText(String.valueOf(rd0.giveup));
        this.tv42.setText(String.valueOf(rd1.win));
        this.tv43.setText(String.valueOf(rd1.lose));
        this.tv44.setText(String.valueOf(rd1.giveup));
        this.tv52.setText(String.valueOf(rd2.win));
        this.tv53.setText(String.valueOf(rd2.lose));
        this.tv54.setText(String.valueOf(rd2.giveup));
    }
}
