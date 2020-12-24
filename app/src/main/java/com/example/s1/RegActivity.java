package com.example.s1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RegActivity extends AppCompatActivity {

    EditText userEdit, emailEditText, passwordEditText, retryPasswordEditText;
    Button button;
    String username, email, pass, twoPass;
    Oshibka warningDialog;
    String url = "http://cars.areas.su/signup";
    HttpURLConnection connection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        userEdit = findViewById(R.id.username);
        emailEditText = findViewById(R.id.e_mail);
        passwordEditText = findViewById(R.id.password);
        retryPasswordEditText = findViewById(R.id.retry_password);
        button = findViewById(R.id.registration);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean filling = true;
                try {
                    username = userEdit.getText().toString();
                    email = emailEditText.getText().toString();
                    pass = passwordEditText.getText().toString();
                    twoPass = retryPasswordEditText.getText().toString();
                    if (!pass.equals(twoPass)) {
                        throw new Exception();
                    }
                    if(username.isEmpty() || email.isEmpty() || pass.isEmpty()){
                        throw new NullPointerException();
                    }
                } catch (NullPointerException e) {
                    warningDialog = new Oshibka("Заполните все поля!");
                    warningDialog.show(getSupportFragmentManager(), "custom");
                    filling = false;
                } catch (Exception e) {
                    warningDialog = new Oshibka("Пароли не совпадают");
                    warningDialog.show(getSupportFragmentManager(), "custom");
                    filling = false;
                }

                Boolean finalFilling = filling;
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        if (finalFilling) {
                            try {
                                connection = (HttpURLConnection) new URL(url).openConnection();
                                connection.setRequestMethod("POST");
                                connection.setDoOutput(true);
                                connection.connect();
                                String request = "username=" + username + "&" + "email=" + email + "&" + "password=" + pass;
                                connection.getOutputStream().write(request.getBytes());

                                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                String inputLine = "";
                                StringBuilder response = new StringBuilder();
                                while ((inputLine = in.readLine()) != null) {
                                    response.append(inputLine);
                                }
                                int a = response.lastIndexOf("answer") - 2;
                                int b = response.indexOf("}", a) + 1;
                                String response_finally = response.substring(a, b);
                                JSONObject jsonObject = new JSONObject(response_finally);
                                String answer = jsonObject.get("answer").toString();
                                if (answer.equals("Success")) {
                                    loginAfterRegistration();
                                } else {
                                    Oshibka warningDialog = new Oshibka(answer);
                                    warningDialog.show(getSupportFragmentManager(), "custom");
                                }
                                connection.disconnect();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        });
    }

    public void loginAfterRegistration(){
        Intent intent = new Intent(this, LogActivity.class);
        startActivity(intent);
    }
}