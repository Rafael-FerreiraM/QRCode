package com.example.qrbola7;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import com.example.qrbola7.dao.AlunoDAO;
import com.example.qrbola7.model.Aluno;
import cn.pedant.SweetAlert.SweetAlertDialog;
import net.glxn.qrgen.android.QRCode;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GerarQR extends AppCompatActivity {
    private ImageView imageView;
    private Button gerarQRButton;
    private Button voltarButton;
    private Button ler;
    private Aluno aluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerar_qr);
        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        imageView = findViewById(R.id.imageView);
        gerarQRButton = findViewById(R.id.btnGerar);
        voltarButton = findViewById(R.id.btnVoltar);
        ler = findViewById(R.id.btnLer);

        // Recupera o ID do aluno a partir das SharedPreferences
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int alunoId = preferences.getInt("alunoId", -1);

        AlunoDAO alunoDAO = new AlunoDAO(this);
        aluno = alunoDAO.getAlunoPorId(alunoId);

        if (aluno != null) {
            gerarQRButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Gera o QR Code com os dados formatados do aluno
                    generateQRCode(formatData());
                }
            });
        }

        voltarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GerarQR.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GerarQR.this, LerQR.class);
                startActivity(intent);
            }
        });
    }
    private String formatData() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dataFormatada = sdf.format(new Date());

        String conteudo = "Nome Completo: " + aluno.getNomeCompleto() + "\n" +
                "Nome da Faculdade: " + aluno.getNomeFaculdade() + "\n" +
                "Curso: " + aluno.getCurso() + "\n" +
                "RGM: " + aluno.getRgm() + "\n" +
                "Data e Hora: " + dataFormatada;

        return conteudo;
    }

    private void generateQRCode(String content) {
        // Gera o QR Code
        Bitmap qrCodeBitmap = QRCode.from(content).bitmap();

        // Define o QR Code no ImageView
        imageView.setImageBitmap(qrCodeBitmap);
    }


    @Override
    public void onBackPressed() {
        // Redireciona para a Main
        startActivity(new Intent(GerarQR.this, MainActivity.class));
    }
}
