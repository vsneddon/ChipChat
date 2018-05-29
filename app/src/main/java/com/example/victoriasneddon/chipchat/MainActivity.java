package com.example.victoriasneddon.chipchat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    LinearLayout popularList;
    LinearLayout localList;
    Context context;

    Button popularButton;
    Button localButton;

    View.OnClickListener chatRoomClick;

    App appState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appState = ((App)this.getApplication());

        context = getApplicationContext();

        popularList = findViewById(R.id.Popular);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET}, 0);
        }

        //Buttons ----------------------------------------------------------------------------------
        popularButton = findViewById(R.id.PopularRooms);
        localButton = findViewById(R.id.LocalRooms);
        popularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popularList.setVisibility(View.VISIBLE);
                //localList.setVisibility(View.GONE);
                popularButton.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue));
                localButton.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));
            }
        });
        localButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //localList.setVisibility(View.VISIBLE);
                popularList.setVisibility(View.GONE);
                localButton.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue));
                popularButton.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));
            }
        });

        popularButton.setBackgroundColor(ContextCompat.getColor(context, R.color.light_blue));
        localButton.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));

        //For rooms --------------------------------------------------------------------------------
        chatRoomClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startConnect(v);
            }
        };

        appState.chatroom = new ChatRoom(new ChatRoom.Listener() {
            @Override
            public void setText(String text) {
                Log.d("ChatRoom", text);
                if(text.equals("")){
                    return;
                }

                String[] splitRooms = text.split(";");
                boolean odd = false;
                for(String s : splitRooms){
                    String[] temp = s.split(" ");
                    LinearLayout newL = new LinearLayout(context);
                    newL.setContentDescription(temp[0]);
                    newL.setPadding(50,50,50,50);
                    if(odd){
                        newL.setBackgroundColor(ContextCompat.getColor(context, R.color.light_grey));
                    }
                    popularList.addView(newL);

                    TextView tempView = new TextView(context);
                    //tempView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    tempView.setText(temp[0]);

                    TextView tempView2 = new TextView(context);
                    tempView2.setText("Users: "+temp[1]);
                    tempView2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    tempView2.setGravity(Gravity.RIGHT);

                    newL.addView(tempView);
                    newL.addView(tempView2);

                    newL.setOnClickListener(chatRoomClick);

                    odd = !odd;
                }
            }
        });

        appState.chatroom.execute("init");
    }

    public void startConnect(View v){
        LinearLayout l = (LinearLayout) v;
        Toast toast = Toast.makeText(this, l.getContentDescription(), Toast.LENGTH_LONG);
        toast.show();
        appState.roomName = l.getContentDescription().toString();
        startActivity(new Intent(context, PreChatRoom.class));
    }


}
