package com.example.juexingzhe.messengertest;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ProgressBar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Button mButton;
    private WebView mWebView;

    private Messenger mSender;
    private Messenger mReceiver = new Messenger(new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.i(TAG, "msg is received from service");

            Bundle data = msg.getData();
            if (null != data) {
                mButton.setVisibility(View.GONE);
                String body = data.getString("body");
                mWebView.loadUrl(body);
            }

        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {

        mButton = (Button) findViewById(R.id.btn);
        mWebView = (WebView) findViewById(R.id.webView);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MessengerService.class);
                bindService(intent, serviceConnection, BIND_AUTO_CREATE);

                mButton.setText(R.string.wait_tip);
                mWebView.setVisibility(View.VISIBLE);
                sendMessage();
            }
        });

    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mSender = new Messenger(iBinder);

            sendMessage();

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        if (mButton.getVisibility() == View.GONE) {
            mButton.setText(getString(R.string.btn_click));
            mButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        mSender = null;
        mReceiver = null;
        mWebView = null;
        super.onDestroy();
    }

    private void sendMessage() {
        if (null != mSender) {
            Message msg = Message.obtain();
            msg.replyTo = mReceiver;
            try {
                mSender.send(msg);
                Log.i(TAG, "msg is right now send");
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
