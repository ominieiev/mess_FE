package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.encrypter.AES;
import com.example.myapplication.encrypter.DataPrepearer;
import com.example.myapplication.servises.Rester;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private EditText sendText;
    private TextView chatText;
    private Button cleanBtn;
    private Button sendBtn;
    private static final String MESSAGE_FOR_CHAT_CLEANING = "66f561bb666f561bb6bn837nggfddsgjkkrrtyesfdg2b68372213e00fbf4a2cd6519e0cfcb";
    private static final String URL = "https://ominieiev-mess.herokuapp.com";
    private static final String URL_GET_POSTFIX = "/getChat";
    private static final String URL_POST_POSTFIX = "/addToChat";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String name = DataPrepearer.generateRandomName();

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                getDataUsingVolley(URL + URL_GET_POSTFIX);
            }
        };
        timer.schedule(timerTask, 1000L);

        sendBtn = findViewById(R.id.sendBtn);
        cleanBtn = findViewById(R.id.cleanBtn);
        chatText = findViewById(R.id.chatText);
        chatText.setMovementMethod(new ScrollingMovementMethod());
        sendText = findViewById(R.id.sendText);
        sendText.setMovementMethod(new ScrollingMovementMethod());
        RequestQueue queue = Volley.newRequestQueue(this);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sendText.getText().toString().isEmpty()) {
                    String time = LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute();
                    postDataUsingVolley(URL + URL_POST_POSTFIX,
                            time+" {" + name + "}: " + sendText.getText().toString());
                    sendText.setText("");
                    final int scrollAmount = chatText.getLayout().getLineTop(chatText.getLineCount()) - chatText.getHeight();
                    chatText.scrollTo(0, scrollAmount+180);
                }

            }
        });
        cleanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postDataUsingVolley(URL + URL_POST_POSTFIX, MESSAGE_FOR_CHAT_CLEANING);
            }
        });

    }

    private void postDataUsingVolley(String url, String message) {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String decrepted = DataPrepearer.prepareText(response.toString());
                //decrepted = response.toString();
                chatText.setText(decrepted);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int a = 1;
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String encryptedMessage = AES.encrypt(message);
                if (message == MESSAGE_FOR_CHAT_CLEANING) {
                    encryptedMessage = MESSAGE_FOR_CHAT_CLEANING;
                }
                params.put("message", encryptedMessage);
                return params;
            }
        };
        queue.add(request);
    }

    private void getDataUsingVolley(String url) {
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String decrepted = DataPrepearer.prepareText(response.toString());

                chatText.setText(decrepted);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                int a = 2;
            }
        });
        queue.add(request);
    }

}