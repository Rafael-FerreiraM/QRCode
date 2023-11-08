package com.example.qrbola7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import com.example.qrbola7.dao.AlunoDAO;
import android.widget.LinearLayout;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity {
    private AlunoDAO alunoDAO;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        LinearLayout lerQR = findViewById(R.id.bloco1);
        LinearLayout gerarQR = findViewById(R.id.bloco2);
        LinearLayout minhaConta = findViewById(R.id.bloco3);
        LinearLayout verPresencas = findViewById(R.id.bloco4);
        LinearLayout sair = findViewById(R.id.bloco5);

        alunoDAO = new AlunoDAO(this);
        preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Recupera os dados do usuário das SharedPreferences
        int alunoId = preferences.getInt("alunoId", -1);
        String nomeCompleto = preferences.getString("nomeCompleto", "");
        String cpf = preferences.getString("cpf", "");
        String rgm = preferences.getString("rgm", "");
        String nomeFaculdade = preferences.getString("nomeFaculdade", "");
        String curso = preferences.getString("curso", "");
        String senha = preferences.getString("senha", "");

        lerQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redireciona para a classe LerQR
                Intent intent = new Intent(MainActivity.this, LerQR.class);
                startActivity(intent);
            }
        });

        sair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redireciona para a TelaLogin e pergunta se deseja sair
                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Você tem Certeza?")
                        .setContentText("Deseja sair da sua conta?")
                        .setConfirmText("Sim")
                        .setCancelText("Não")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                // Redireciona para a tela de login após a confirmação
                                startActivity(new Intent(MainActivity.this, TelaLogin.class));
                            }
                        })
                        .show();
            }
        });

        gerarQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Salva os dados do usuário nas SharedPreferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("alunoId", alunoId);
                editor.putString("nomeCompleto", nomeCompleto);
                editor.putString("cpf", cpf);
                editor.putString("rgm", rgm);
                editor.putString("nomeFaculdade", nomeFaculdade);
                editor.putString("curso", curso);
                editor.putString("senha", senha);
                editor.apply();

                // Redireciona para a classe GerarQR
                Intent intent = new Intent(MainActivity.this, GerarQR.class);
                startActivity(intent);
            }
        });

        minhaConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Salva os dados do usuário nas SharedPreferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("alunoId", alunoId);
                editor.putString("nomeCompleto", nomeCompleto);
                editor.putString("cpf", cpf);
                editor.putString("rgm", rgm);
                editor.putString("nomeFaculdade", nomeFaculdade);
                editor.putString("curso", curso);
                editor.putString("senha", senha);
                editor.apply();

                // Redireciona para a classe Conta
                Intent intent = new Intent(MainActivity.this, Conta.class);
                startActivity(intent);
            }
        });

        verPresencas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redireciona para a classe Presenca
                Intent intent = new Intent(MainActivity.this, Presenca.class);
                startActivity(intent);
            }
        });
    }



    @Override
    public void onBackPressed() {
        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Você tem Certeza?")
                .setContentText("Deseja sair do aplicativo?")
                .setConfirmText("Sim")
                .setCancelText("Não")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        // Redireciona para a tela de login após a confirmação
                        startActivity(new Intent(MainActivity.this, TelaLogin.class));
                    }
                })
                .show();
    }
}
