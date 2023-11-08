package com.example.qrbola7;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.example.qrbola7.dao.AlunoDAO;
import com.example.qrbola7.model.Aluno;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class TelaLogin extends AppCompatActivity {

    private EditText rgmEditText;
    private EditText senhaEditText;
    private AlunoDAO alunoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_login);

        rgmEditText = findViewById(R.id.rgmEditText);
        senhaEditText = findViewById(R.id.senhaEditText);
        Button loginButton = findViewById(R.id.loginButton);
        Button cadastrarButton = findViewById(R.id.cadastrarButton);

        alunoDAO = new AlunoDAO(this);

        cadastrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TelaLogin.this, TelaCadastro.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rgm = rgmEditText.getText().toString();
                String senha = senhaEditText.getText().toString();

                // Obtem o ID com base no RGM
                int alunoId = alunoDAO.obterIdPeloRGM(rgm);

                if (alunoId >= 0) {
                    // O aluno foi encontrado com base no RGM
                    Aluno aluno = alunoDAO.getAlunoPorId(alunoId);

                    if (aluno != null && aluno.getSenha().equals(senha)) {
                        // Armazena o ID do aluno nas SharedPreferences
                        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putInt("alunoId", alunoId);
                        editor.apply();

                        SweetAlertDialog pDialog = new SweetAlertDialog(TelaLogin.this, SweetAlertDialog.PROGRESS_TYPE);
                        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                        pDialog.setTitleText("Fazendo Login....");
                        pDialog.setCancelable(false);
                        pDialog.show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pDialog.dismissWithAnimation();

                                new SweetAlertDialog(TelaLogin.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Login Feito com Sucesso")
                                        .show();
                                limparCampos();

                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        // Recupera o ID do aluno a partir das SharedPreferences
                                        int alunoId = preferences.getInt("alunoId", -1);

                                        if (alunoId >= 0) {
                                            // O aluno está logado, redireciona para a próxima atividade com o alunoId
                                            Intent intent = new Intent(TelaLogin.this, MainActivity.class);
                                            intent.putExtra("alunoId", alunoId);
                                            startActivity(intent);
                                        } else {
                                            // O aluno não está logado ou o ID não foi encontrado nas SharedPreferences
                                            new SweetAlertDialog(TelaLogin.this, SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Login Inválido")
                                                    .setContentText("Suas credenciais estão incorretas")
                                                    .show();
                                            limparCampos();
                                        }
                                    }
                                }, 2000);
                            }
                        }, 2000);
                    } else {
                        new SweetAlertDialog(TelaLogin.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Login Inválido")
                                .setContentText("Suas credenciais estão incorretas")
                                .show();
                        limparCampos();
                    }
                } else {
                    new SweetAlertDialog(TelaLogin.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Login Inválido")
                            .setContentText("Sua conta não está cadastrada.")
                            .show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        new SweetAlertDialog(TelaLogin.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Você tem Certeza?")
                .setContentText("Deseja sair do aplicativo?")
                .setConfirmText("Sim")
                .setCancelText("Não")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        finishAffinity();
                    }
                }).show();
    }

    private void limparCampos() {
        rgmEditText.setText("");
        senhaEditText.setText("");
    }
}
