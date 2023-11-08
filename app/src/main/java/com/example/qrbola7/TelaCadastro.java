package com.example.qrbola7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.qrbola7.dao.AlunoDAO;
import com.example.qrbola7.model.Aluno;
import com.google.android.material.textfield.TextInputEditText;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class TelaCadastro extends AppCompatActivity {
    private TextInputEditText cpfEditText;
    private Button cadastrarButton;
    private Button voltarButton;
    private TextInputEditText nomeCompletoEditText;
    private TextInputEditText rgmEditText;
    private TextInputEditText faculdadeEditText;
    private TextInputEditText cursoEditText;
    private TextInputEditText senhaEditText;
    private AlunoDAO alunoDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        cpfEditText = findViewById(R.id.cpfEditText);
        cadastrarButton = findViewById(R.id.cadastrarButton);
        voltarButton = findViewById(R.id.voltarButton);
        nomeCompletoEditText = findViewById(R.id.nomeCompletoEditText);
        rgmEditText = findViewById(R.id.rgmEditText);
        faculdadeEditText = findViewById(R.id.faculdadeEditText);
        cursoEditText = findViewById(R.id.cursoEditText);
        senhaEditText = findViewById(R.id.senhaEditText);

        alunoDAO = new AlunoDAO(this);

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

        cadastrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean camposValidos = true;

                String nomeCompleto = nomeCompletoEditText.getText().toString();
                String cpf = cpfEditText.getText().toString().replaceAll("[^0-9]", "");
                String rgm = rgmEditText.getText().toString();
                String senha = senhaEditText.getText().toString();
                String faculdade = faculdadeEditText.getText().toString();
                String curso = cursoEditText.getText().toString();

                // Verifica se os campos estão em branco
                if (nomeCompleto.isEmpty() || cpf.isEmpty() || rgm.isEmpty() || senha.isEmpty() || faculdade.isEmpty() || curso.isEmpty()) {
                    camposValidos = false;
                }


                // Verifica se o nome completo contém apenas letras e espaços
                if (!nomeCompleto.matches("^[a-zA-Z\\s]+$")) {
                    nomeCompletoEditText.setError("Nome inválido. Use apenas letras e espaços.");
                    camposValidos = false;
                } else {
                    nomeCompletoEditText.setError(null);
                }

                // Verifica se o nome do curso contém apenas letras e espaços
                if (!curso.matches("^[a-zA-Z\\s]+$")) {
                    cursoEditText.setError("Curso inválido. Use apenas letras e espaços.");
                    camposValidos = false;
                } else {
                    cursoEditText.setError(null);
                }

                // Verifica se o nome da faculdade contém apenas letras e espaços
                if (!faculdade.matches("^[a-zA-Z\\s]+$")) {
                    faculdadeEditText.setError("Faculdade inválida. Use apenas letras e espaços.");
                    camposValidos = false;
                } else {
                    faculdadeEditText.setError(null);
                }

                // Verifica se o RGM contém apenas números
                if (!rgm.matches("^[0-9]+$")) {
                    rgmEditText.setError("RGM inválido. Use apenas números.");
                    camposValidos = false;
                } else {
                    rgmEditText.setError(null);
                }

                // Verifica se a senha tem pelo menos 8 caracteres
                if (senha.length() < 8) {
                    senhaEditText.setError("A senha deve ter pelo menos 8 caracteres.");
                    camposValidos = false;
                } else {
                    senhaEditText.setError(null);
                }

                if (camposValidos) {
                    if (alunoDAO.isCPFCadastrado(cpf)) {
                        // O CPF já existe no banco de dados
                        new SweetAlertDialog(TelaCadastro.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Erro")
                                .setContentText("CPF já cadastrado.")
                                .show();
                    } else if (alunoDAO.isRGMCadastrado(rgm)) {
                        // O RGM já existe no banco de dados
                        new SweetAlertDialog(TelaCadastro.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Erro")
                                .setContentText("RGM já cadastrado.")
                                .show();
                    } else {
                        Aluno aluno = new Aluno();
                        aluno.setNomeCompleto(nomeCompleto);
                        aluno.setCpf(cpf);
                        aluno.setRgm(rgm);
                        aluno.setSenha(senha);
                        aluno.setNomeFaculdade(faculdade);
                        aluno.setCurso(curso);

                        alunoDAO.salvarAluno(aluno);

                        new SweetAlertDialog(TelaCadastro.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Cadastro realizado com sucesso!")
                                .setContentText("Você será redirecionado para a tela de login!")
                                .show();

                        limparCampos();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(TelaCadastro.this, TelaLogin.class);
                                int alunoId = alunoDAO.obterIdPeloCPF(cpf);
                                SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putInt("alunoId", alunoId);
                                editor.apply();
                                startActivity(intent);
                            }
                        }, 2000);
                    }
                }
            }
            });

                voltarButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Voltar para a classe TelaLogin
                        Intent intent = new Intent(TelaCadastro.this, TelaLogin.class);
                        startActivity(intent);
                    }
                });
            }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TelaCadastro.this, TelaLogin.class);
        limparCampos();
        startActivity(intent);
    }

    private void limparCampos() {
        nomeCompletoEditText.setText("");
        cpfEditText.setText("");
        rgmEditText.setText("");
        senhaEditText.setText("");
        faculdadeEditText.setText("");
        cursoEditText.setText("");
    }
    }
