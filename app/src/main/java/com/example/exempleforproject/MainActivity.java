package com.example.exempleforproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends Activity {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String PREFS = "prefs";
    public static final String USERS = "users";
    private boolean isRunning = false;
    private EditText txtUser, txtPassword;
    private Users users;
    private User user;
    private Button btn1, btn2;
    private TextView lblWait;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtUser = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        lblWait = findViewById(R.id.lblWait);
        users = Users.getUsers();

        SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
        if(prefs.contains(USERS)){
            Set<String> usersAsString = prefs.getStringSet(USERS, null);
            users.loadUsers(usersAsString);
        }
        if(prefs.contains(USERNAME)) {
            String userName = prefs.getString(USERNAME, null);
            String password = prefs.getString(PASSWORD, null);
            user = new User(userName, password);
            txtUser.setText(user.getUserName());
            txtPassword.setText(user.getPassword());
            loginOrSignup(true);
        }
    }

    public void btnSignupClicked(View view) {
        holdOnForCheckIfEverythingOk(false);
    }

    public void btnLoginClicked(View view) {
        holdOnForCheckIfEverythingOk(true);
    }

    private void goToSecondActivity(){
        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra(USERNAME, user.getUserName());
        startActivity(intent);
        finish();
    }

    private void verySlowAlgorithm(){
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private void holdOnForCheckIfEverythingOk(final boolean isLogin){
        if(isRunning){
            Toast.makeText(this, "please wait for it to finish first", Toast.LENGTH_SHORT).show();
            return;
        }
        isRunning = true;
        btn1.setEnabled(false);
        btn2.setEnabled(false);
        txtUser.setEnabled(false);
        txtPassword.setEnabled(false);
        lblWait.setText("please wait...");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                isRunning = false;

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        verySlowAlgorithm();
                        btn1.setEnabled(true);
                        btn2.setEnabled(true);
                        txtUser.setEnabled(true);
                        txtPassword.setEnabled(true);
                        if (isLogin){
                            loginOrSignup(true);
                        } else {
                            loginOrSignup(false);
                        }
                    }
                });
            }
        });
        thread.start();
    }

    private void loginOrSignup(boolean isLogin){
        user = getUserFromUI();
        if(user == null)
            return;
        String message = null;
        if(isLogin){
            if(!users.login(user)){
                message = "username or password are incorrect";
            }
        }else{
            if(!users.signup(user)){
                message = "username taken";
            }
        }
        if(message != null){
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            lblWait.setText(message);

        }else {
            SharedPreferences prefs = getSharedPreferences(PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(USERNAME, user.getUserName())
                    .putString(PASSWORD, user.getPassword());
            if(!isLogin){
                Set<String> usersAsString = users.getUsersAsString();
                editor.putStringSet(USERS, usersAsString);
            }

            editor.commit();
            goToSecondActivity();
        }
    }

    private User getUserFromUI(){
        String userName = txtUser.getText().toString();
        String password = txtPassword.getText().toString();
        if(userName.isEmpty() || password.isEmpty()){
            Toast.makeText(this, "both username and password are mandatory!",
                    Toast.LENGTH_SHORT).show();
            return null;
        }
        return new User(userName, password);
    }



}
