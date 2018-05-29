package com.example.victoriasneddon.chipchat;

import android.os.AsyncTask;
import android.util.Log;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class client extends AsyncTask<String, String, String> {
    public ArrayList<String> messages = new ArrayList<>();

    private DataOutputStream out;
    private ChatRoom chatRoom;

    public client(ChatRoom chatRoom){
        this.chatRoom = chatRoom;
        Log.d("ChatRoom", "Constructed.");
    }

    @Override
    protected String doInBackground(String... params){
        try {
            // Wait so client can connect to the server.
            Thread.sleep(2000);
            this.out = chatRoom.getOut();
        }catch (Exception e){
            Log.e("ChatRoom", "Could not start messenger.", e);
        }

        while(true){
            if(messages.size() > 0){
                Log.d("ChatRoom", "Sending message."+messages.get(0));
                Message(messages.get(0));
                messages.remove(0);
            }
        }
    }

    private void Message(String text){
        try {

            Log.d("ChatRoom", "Starting send message..");

            if (chatRoom.setupLevel != 5) {
                chatRoom.errorMessage = "Current SetupLevel: " + chatRoom.setupLevel + ", Level 3 required.";
                return;
            }

            while (!chatRoom.closed) {
                try {
                    if (chatRoom.kicked)
                        break;

                    if (!text.equals("")) {
                        //Check for command from user
                        if (text.equals("/close")) break;
                        if (text.charAt(0) == '/') {
                            out.writeUTF(chatRoom.DE.encryptText(text, true));
                        } else {
                            //Send message if message is not empty
                            out.writeUTF(chatRoom.DE.encryptText(chatRoom.username + ": " + text));
                            out.flush();
                        }
                    }
                } catch (IOException e) {
                    Log.d("ChatRoom","::Could not send message. " + e);
                    break;
                }
                return;
            }
            chatRoom.sock.close();
        }catch(Exception e){
            Log.e("ChatRoom", "Some exception", e);
        }
    }
}
