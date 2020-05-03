package com.example.gerenciadordeeventos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //this.deleteDatabase("Evento.db");

        getSupportActionBar().hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarConsultaEventos();
            }
        }, 3000);
    }

    private void mostrarConsultaEventos() {
        Intent intent = new Intent(
                SplashActivity.this,ConsultaEventos.class
        );
        startActivity(intent);
        finish();
    }
}
