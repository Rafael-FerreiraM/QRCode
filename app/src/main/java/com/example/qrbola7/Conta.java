package com.example.qrbola7;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.qrbola7.dao.AlunoDAO;
import com.example.qrbola7.model.Aluno;
import com.google.android.material.textfield.TextInputEditText;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class Conta extends AppCompatActivity {
    private TextInputEditText cpfEditText;
    private Button atualizarButton;
    private Button voltarButton;
    private TextInputEditText nomeCompletoEditText;
    private TextInputEditText rgmEditText;
    private TextInputEditText faculdadeEditText;
    private TextInputEditText cursoEditText;
    private TextInputEditText senhaEditText;
    private AlunoDAO alunoDAO;
    private int alunoId; // Variável para armazenar o ID do aluno
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta);

        cpfEditText = findViewById(R.id.cpfEditText);
        atualizarButton = findViewById(R.id.atualizarButton);
        voltarButton = findViewById(R.id.voltarButton);
        nomeCompletoEditText = findViewById(R.id.nomeCompletoEditText);
        rgmEditText = findViewById(R.id.rgmEditText);
        faculdadeEditText = findViewById(R.id.faculdadeEditText);
        cursoEditText = findViewById(R.id.cursoEditText);
        senhaEditText = findViewById(R.id.senhaEditText);

        alunoDAO = new AlunoDAO(this);
        preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Recupera o ID do aluno das SharedPreferences
        alunoId = preferences.getInt("alunoId", -1);

        if (alunoId != -1) {
            // DAO para obter o objeto Aluno com base no ID
            Aluno aluno = alunoDAO.getAlunoPorId(alunoId);

            if (aluno != null) {
                // Preenche os campos com os dados do aluno
                nomeCompletoEditText.setText(aluno.getNomeCompleto());
                rgmEditText.setText(aluno.getRgm());
                faculdadeEditText.setText(aluno.getNomeFaculdade());
                cursoEditText.setText(aluno.getCurso());
                senhaEditText.setText(aluno.getSenha());
                cpfEditText.setText(aluno.getCpf());
            }
        }

        // Lógica de validação para o CPF
        cpfEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    String cpf = cpfEditText.getText().toString().replaceAll("[^0-9]", "");
                    if (cpf.length() != 11) {
                        cpfEditText.setError("CPF inválido");
                    } else {
                        cpfEditText.setError(null);
                    }
                }
            }
        });

        atualizarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Captura os novos dados dos campos de texto
                String nomeCompleto = nomeCompletoEditText.getText().toString();
                String rgm = rgmEditText.getText().toString();
                String faculdade = faculdadeEditText.getText().toString();
                String curso = cursoEditText.getText().toString();
                String senha = senhaEditText.getText().toString();
                String cpf = cpfEditText.getText().toString();

                // Atualiza os dados nas SharedPreferences
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("nomeCompleto", nomeCompleto);
                editor.putString("cpf", cpf);
                editor.putString("rgm", rgm);
                editor.putString("nomeFaculdade", faculdade);
                editor.putString("curso", curso);
                editor.putString("senha", senha);
                editor.apply();

                // Cria um objeto Aluno com os novos dados
                Aluno alunoAtualizado = new Aluno();
                alunoAtualizado.setId(alunoId);
                alunoAtualizado.setNomeCompleto(nomeCompleto);
                alunoAtualizado.setRgm(rgm);
                alunoAtualizado.setNomeFaculdade(faculdade);
                alunoAtualizado.setCurso(curso);
                alunoAtualizado.setSenha(senha);
                alunoAtualizado.setCpf(cpf);

                // Usa o método atualizarAluno do AlunoDAO
                int linhasAfetadas = alunoDAO.atualizarAluno(alunoAtualizado);

                if (linhasAfetadas > 0) {
                    new SweetAlertDialog(Conta.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Atualização Concluída, faça login novamente!")
                            .show();
                    // Redireciona para a tela de login
                    Intent loginIntent = new Intent(Conta.this, TelaLogin.class);
                    startActivity(loginIntent);
                } else {
                    new SweetAlertDialog(Conta.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Falha na Atualização! Verifique os campos")
                            .show();
                }
            }
        });

        voltarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Conta.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Conta.this, MainActivity.class);
        startActivity(intent);
    }
}
