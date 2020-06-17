package com.example.familymapclient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Toast;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.Map;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Iconify.with(new FontAwesomeModule());

        setContentView(R.layout.activity_main);
        LoginFragment loginFragment;
        MapFragment mapFragment;
        DataCache dataCache = DataCache.getInstance();
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        if(dataCache.getAuthToken() == null){
            loginFragment = (LoginFragment) fragmentManager.findFragmentById(R.id.displayMain);
            if(loginFragment == null){
                loginFragment = new LoginFragment();
                fragmentManager.beginTransaction().add(R.id.displayMain, loginFragment).commit();
            }
        }
        else {
            mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.displayMain);
            if(mapFragment == null){
                mapFragment = new MapFragment();
            }
            fragmentManager.beginTransaction().add(R.id.displayMain, mapFragment).commit();
        }
    }

    public void switchToMap(){
       getSupportFragmentManager().beginTransaction().replace(R.id.displayMain, new MapFragment()).commit();
    }
}