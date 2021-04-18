package com.example.chatbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView chatsRV;
    private Button sendMsgIB;
    private EditText userMsgEdt;
    private final String USER_KEY ="user";
    private final String BOT_KEY="bot";

    //volley request key
    private RequestQueue mRequestQueue;

    private ArrayList<MessageModel> messageModelArrayList;
    private MessageRVAdapter adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatsRV=findViewById(R.id.idChat);
        sendMsgIB=findViewById(R.id.idSend);
        userMsgEdt=findViewById(R.id.idText);

        mRequestQueue= Volley.newRequestQueue(MainActivity.this);
        mRequestQueue.getCache().clear();

        messageModelArrayList=new ArrayList<>();


        sendMsgIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userMsgEdt.getText().toString().isEmpty())
                {
                    Toast.makeText(MainActivity.this,"please enter your message..",Toast.LENGTH_SHORT).show();
                    return;
                }

                sendMessage(userMsgEdt.getText().toString());

                userMsgEdt.setText("");
            }
        });

        adapter = new MessageRVAdapter(messageModelArrayList,this);

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(MainActivity.this,RecyclerView.VERTICAL,false);

        chatsRV.setLayoutManager(linearLayoutManager);

        chatsRV.setAdapter(adapter);


    }

    private void sendMessage(String userMsg)
    {
       messageModelArrayList.add(new MessageModel(userMsg,USER_KEY));
       adapter.notifyDataSetChanged();

      String url="https://mocki.io/v1/a56de911-c965-4f8a-9a27-b2c4269e6f4d";
        // String url="http://api.brainshop.ai/get?bid=155712&key=laS1BfQo8yHzW1rf&uid=mshape&msg=" + userMsg;

     RequestQueue queue = Volley.newRequestQueue(this);


        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String botResponse;

                    if(userMsg.contains("name"))  botResponse=response.getString("name");
                    else  if(userMsg.contains("city"))  botResponse=response.getString("city");
                    else if(userMsg.contains("age"))  botResponse=response.getString("age");
                    else  if(userMsg.contains("hy"))  botResponse=response.getString("hyy");
                    else botResponse=response.getString("default");


                    messageModelArrayList.add(new MessageModel(botResponse, BOT_KEY));

                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                    messageModelArrayList.add(new MessageModel("NO Response", BOT_KEY));
                    adapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                messageModelArrayList.add(new MessageModel("Sorry NO Response found", BOT_KEY));
                Toast.makeText(MainActivity.this,"No response from the bot...",Toast.LENGTH_SHORT).show();

            }



    });

    queue.add(jsonObjectRequest);











    }
}