package com.example.qrbola7;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

import java.util.HashSet;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LerQR extends AppCompatActivity {

    private CodeScanner codeScanner;
    private TextView codeData;
    private CodeScannerView scannerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ler_qr);

        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                android.Manifest.permission.CAMERA
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        } else {
            // Vincula o leitor ao layout
            scannerView = findViewById(R.id.scanner_view);
            codeData = findViewById(R.id.text_code);
            runCodeScanner();
        }
    }

    public void runCodeScanner() {
        codeScanner = new CodeScanner(this, scannerView);

        codeScanner.setAutoFocusEnabled(true);
        codeScanner.setFormats(CodeScanner.ALL_FORMATS);
        codeScanner.setScanMode(ScanMode.CONTINUOUS);

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String data = result.getText();
                        codeData.setText(data);

                        if (isValidDataFormat(data)) {
                            // Salva os dados nas SharedPreferences
                            saveDataToSharedPreferences(data);

                            // Redireciona para a classe Presenca
                            Intent intent = new Intent(LerQR.this, Presenca.class);
                            startActivity(intent);
                        }
                    }
                });
            }
        });


        // Inicia a visualização da câmera
        codeScanner.startPreview();
    }

    private boolean isValidDataFormat(String data) {
        // Divide os dados em linhas com base na quebra de linha
        String[] linhas = data.split("\n");

        // Verifica se as linhas contêm os rótulos esperados
        boolean contemNomeCompleto = false;
        boolean contemNomeFaculdade = false;
        boolean contemCurso = false;
        boolean contemRGM = false;
        boolean contemDataHora = false;

        for (String linha : linhas) {
            if (linha.startsWith("Nome Completo:")) {
                contemNomeCompleto = true;
            } else if (linha.startsWith("Nome da Faculdade:")) {
                contemNomeFaculdade = true;
            } else if (linha.startsWith("Curso:")) {
                contemCurso = true;
            } else if (linha.startsWith("RGM:")) {
                contemRGM = true;
            } else if (linha.startsWith("Data e Hora:")) {
                contemDataHora = true;
            }
        }

        // Verifica se todos os dados esperados estão presentes
        return contemNomeCompleto && contemNomeFaculdade && contemCurso && contemRGM && contemDataHora;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    //Salva os dados do QR e envia
    private void saveDataToSharedPreferences(String data) {
        SharedPreferences preferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        Set<String> dadosSalvos = preferences.getStringSet("dadosFormatados", new HashSet<>());

        dadosSalvos.add(data);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putStringSet("dadosFormatados", dadosSalvos);
        editor.apply();
    }

}