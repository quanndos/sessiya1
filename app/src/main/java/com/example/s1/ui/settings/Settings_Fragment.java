package com.example.s1.ui.settings;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.s1.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class Settings_Fragment extends Fragment {

    public TextView exit;
    private String url = "http://cars.areas.su/logout";
    private HttpURLConnection connection;
    String username;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        exit = root.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            connection = (HttpURLConnection) new URL(url).openConnection();
                            connection.setRequestMethod("POST");
                            connection.setDoInput(true);
                            connection.connect();

                            connection.getOutputStream().write(("username=" + username).getBytes());


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
        return root;
    }

}