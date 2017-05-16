package com.example.juexingzhe.messengertest;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;

/**
 * Created by juexingzhe on 2017/5/16.
 */

public class MessengerService extends Service {


    private static final String REMOTE_URL = "https://www.baidu.com";

    private Messenger messenger = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            try {
                Thread.sleep(5 * 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }


            Message response = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString("body", REMOTE_URL);
            response.setData(bundle);

            Messenger clientMessenger = msg.replyTo;
            try {
                clientMessenger.send(response);
            } catch (RemoteException e) {
                e.printStackTrace();
            }


        }
    });

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }
}
