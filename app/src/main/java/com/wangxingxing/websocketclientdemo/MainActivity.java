package com.wangxingxing.websocketclientdemo;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.wangxingxing.websocketclientdemo.websocketclientdemo.R;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_10;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private Button btnSend;
    private EditText etContent;
    private TextView tvConsole;

    private WebSocketClient mSocketClient;
    private Socket mSocket;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            tvConsole.setText(tvConsole.getText() + "\n" + msg.obj);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initWebSocketIO();
    }

    private void initView() {
        btnSend = findViewById(R.id.btn_send);
        etContent = findViewById(R.id.et_content);
        tvConsole = findViewById(R.id.tv_console);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSocketClient != null) {
                    mSocketClient.send(etContent.getText().toString().trim());
                    etContent.setText("");
                }
                if (mSocket != null) {
                    mSocket.emit("new message", etContent.getText().toString().trim());
                    etContent.setText("");
                }
            }
        });
    }

    private void initWebSocket() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String localHost = "ws://192.168.0.117:2018";
                    mSocketClient = new WebSocketClient(new URI(localHost), new Draft_10()) {
                        @Override
                        public void onOpen(ServerHandshake handshakedata) {
                            Logger.i("onOpen: " + handshakedata.getHttpStatus());
                            handler.obtainMessage(0, "打开通道：state:" + handshakedata.getHttpStatus()).sendToTarget();
                        }

                        @Override
                        public void onMessage(String message) {
                            Logger.i("onMessage: " + message);
                            handler.obtainMessage(0, message).sendToTarget();
                        }

                        @Override
                        public void onClose(int code, String reason, boolean remote) {
                            Logger.i("onClose: reason=" + reason + ", code=" + code + ", remote=" + remote);
                            handler.obtainMessage(0, "关闭通道：" + reason).sendToTarget();
                        }

                        @Override
                        public void onError(Exception ex) {
                            Logger.e("onError: " + ex.toString());
                        }
                    };
                    mSocketClient.connect();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initWebSocketIO() {
        try {
            Log.i(TAG, "initWebSocketIO: ");
            String localHost = "http://192.168.0.117:2018/";
            String remoteHost = "https://socket-io-chat.now.sh/";
            mSocket = IO.socket(remoteHost);

            mSocket.on(Socket.EVENT_CONNECT, onConnect);
            mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectTimeout);
            mSocket.on("new message", onNewMessage);
            mSocket.on("user joined", onUserJoined);
            mSocket.on("user left", onUserLeft);
            mSocket.on("typing", onTyping);
            mSocket.on("stop typing", onStopTyping);

            mSocket.connect();
            mSocket.emit("add user", "wxxtest");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Logger.e("initWebSocketIO: "+ e.toString());
        }
    }

    Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logger.i("call: ");
        }
    };

    Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logger.i("call: ");
        }
    };

    Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logger.e("call: ");
        }
    };

    Emitter.Listener onConnectTimeout = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logger.i("call: ");
        }
    };

    Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logger.i("call: ");
        }
    };

    Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logger.i("call: ");
        }
    };

    Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logger.i("call: ");
        }
    };

    Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logger.i("call: ");
        }
    };

    Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Logger.i("call: ");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSocketClient != null) {
            mSocketClient.close();
        }

        if (mSocket != null) {
            mSocket.disconnect();
            mSocket.off(Socket.EVENT_CONNECT, onConnect);
            mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            mSocket.off("new message", onNewMessage);
            mSocket.off("user joined", onUserJoined);
            mSocket.off("user left", onUserLeft);
            mSocket.off("typing", onTyping);
            mSocket.off("stop typing", onStopTyping);
        }
    }
}
