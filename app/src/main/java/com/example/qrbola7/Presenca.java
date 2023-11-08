package com.example.qrbola7;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Presenca extends AppCompatActivity {

    private ListView listView;
    private Button btnVoltar;
    private List<String> dadosLidos = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presenca);
        btnVoltar = findViewById(R.id.btnVoltar);
        setSupportActionBar(findViewById(R.id.toolbar));
        listView = findViewById(R.id.listViewPresenca);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Recupera os dados salvos na SharedPreferences
        Set<String> dadosSalvos = sharedPreferences.getStringSet("dadosFormatados", new HashSet<>());
        dadosLidos.addAll(dadosSalvos);

        // Exibe os dados na ListView
        updateListView();

        btnVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Presenca.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void updateListView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dadosLidos);
        listView.setAdapter(adapter);
    }



    @Override
    public void onBackPressed() {
        // Redireciona para a Main
        startActivity(new Intent(Presenca.this, MainActivity.class));
    }
}
