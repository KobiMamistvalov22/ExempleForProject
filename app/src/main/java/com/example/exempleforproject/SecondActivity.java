package com.example.exempleforproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class SecondActivity extends FragmentActivity implements PlayOrLogout.PlayOrLogoutFragmentListener {

    private SpacecraftView spacecraftView;
    private Handler handler = new Handler();
    private final static long INTERVAL = 30;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PlayOrLogout playOrLogoutFragmentListener = new PlayOrLogout();
        playOrLogoutFragmentListener.setQuestion(getIntent().getStringExtra(MainActivity.USERNAME));
        playOrLogoutFragmentListener.setPlayOrLogoutFragmentListener(this);
        playOrLogoutFragmentListener.show(getSupportFragmentManager(), "");
        spacecraftView = new SpacecraftView(this);
        setContentView(spacecraftView);

    }


    @Override
    public void onChoose(boolean isYes) {
        if (isYes == false){
            SharedPreferences prefs = getSharedPreferences(MainActivity.PREFS, MODE_PRIVATE);
            prefs.edit().remove(MainActivity.USERNAME).remove(MainActivity.PASSWORD).commit();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            spacecraftView.invalidate();
                        }
                    });
                }
            }, 0, INTERVAL);
        }
    }
}
