package com.example.qrbola7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class TelaInicial extends AppCompatActivity {

    private static final long SPLASH_DURATION = 4000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Após o tempo definido inicia a tela de login
                Intent intent = new Intent(TelaInicial.this, TelaLogin.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}