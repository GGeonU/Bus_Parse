package com.example.dell.bus_parse_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton go_Hwagok;
    private ImageButton go_Home;
    private ImageButton search;
    private ImageButton currentLocation;
    private Button db;
    private Handler handler;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        go_Hwagok = (ImageButton) findViewById(R.id.go_Hwagok);
        go_Home = (ImageButton) findViewById(R.id.go_Home);
        search = (ImageButton) findViewById(R.id.search);
        currentLocation = (ImageButton) findViewById(R.id.currentLocation);


        go_Hwagok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, go_Hwagok.class);
                startActivity(intent);
            }
        });

        go_Home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, go_Home.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Search.class);
                startActivity(intent);
            }
        });

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this ,CurrentLocation.class);
                startActivity(intent);
                handler = new Handler();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog = progressDialog.show(MainActivity.this, "", "잠시만 기다려주세요", true);
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    if(progressDialog!=null&&progressDialog.isShowing()){
                                        progressDialog.dismiss();
                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        },5000);
                    }
                });
            }
        });


    }




}
