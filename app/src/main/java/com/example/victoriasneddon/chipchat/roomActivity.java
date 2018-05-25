package com.example.victoriasneddon.chipchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class roomActivity extends AppCompatActivity {


    private Button mSendButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

        mSendButton = (Button) findViewById(R.id.button_chatbox_send);
        mSendButton.setOnClickListener(chatroom.message("test"));


    }
}
