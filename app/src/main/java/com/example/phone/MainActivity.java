package com.example.phone;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    EditText phoneNo;
    FloatingActionButton callbtn;
    Button answerCAllbtn;
    static int PERMISSION_CODE = 100;

    private TelephonyManager telephonyManager;
    private PhoneStateListener phoneStateListener;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneNo = findViewById(R.id.editText);
        callbtn = findViewById(R.id.callbtn);
        answerCAllbtn=findViewById(R.id.btnAnswerCall);

        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneno = phoneNo.getText().toString();
                makeCall(phoneno);
            }
        });

        answerCAllbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answerCall();
            }
        });

        // Check and request necessary permissions
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ANSWER_PHONE_CALLS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ANSWER_PHONE_CALLS}, PERMISSION_CODE);
        } else {
            initializeTelephonyManager();
        }
    }

    private void initializeTelephonyManager() {
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                super.onCallStateChanged(state, phoneNumber);
                switch (state) {
                    case TelephonyManager.CALL_STATE_RINGING:
                        break;
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        break;
                }
            }
        };
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    private void makeCall(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Permission to make calls not granted", Toast.LENGTH_SHORT).show();
        }
    }

    private void answerCall() {
        TelecomManager telecomManager = (TelecomManager) getSystemService(Context.TELECOM_SERVICE);
        if (telecomManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ANSWER_PHONE_CALLS) == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    telecomManager.acceptRingingCall();
                }
                Toast.makeText(this, "Call Answered", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission to answer calls not granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Unable to access telecom service", Toast.LENGTH_SHORT).show();
        }
    }
}
