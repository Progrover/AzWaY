package com.msaharov.azway.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.os.Bundle;

import com.msaharov.azway.R;
import com.msaharov.azway.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onBackPressed() {
        super.onBackPressed();
        try {
            Navigation.findNavController(this, R.id.host_fragment).navigateUp();
        } catch (IllegalArgumentException e) {
            Navigation.findNavController(this, R.id.globalNavContainer).navigateUp();

        }

    }
}