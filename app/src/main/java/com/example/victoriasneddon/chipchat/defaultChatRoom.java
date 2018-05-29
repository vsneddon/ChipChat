package com.example.victoriasneddon.chipchat;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class defaultChatRoom extends AppCompatActivity {

    private Context context;
    private LinearLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default_chat_room);
        context = getApplicationContext();

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        mLayout = findViewById(R.id.layout);

        final App appState = (App)this.getApplication();

        appState.chatroom = new ChatRoom(new ChatRoom.Listener() {
            @Override
            public void setText(String text) {
                Log.d("ChatRoom", text);
                TextView inputText = new TextView(context);
                inputText.setText(text);
                mLayout.addView(inputText);
            }
        });
        appState.chatroom.execute("join", appState.roomName, password, username);

        final client messageClient = new client(appState.chatroom);
        messageClient.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageClient.messages.add("Test");
            }
        });
    }
}
