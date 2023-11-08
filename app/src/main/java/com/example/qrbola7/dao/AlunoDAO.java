package com.example.qrbola7.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.qrbola7.model.Aluno;
import com.example.qrbola7.util.ConnectionFactory;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {
    private SQLiteDatabase registro;
    private ConnectionFactory conexao;

    public AlunoDAO(Context context) {
        conexao = new ConnectionFactory(context);
        registro = conexao.getWritableDatabase();
    }


    // Método para inserir um aluno no banco de dados
    public long salvarAluno(Aluno aluno) {
        ContentValues values = new ContentValues();
        values.put("nomeCompleto", aluno.getNomeCompleto());
        values.put("cpf", aluno.getCpf());
        values.put("rgm", aluno.getRgm());
        values.put("senha", aluno.getSenha());
        values.put("nomeFaculdade", aluno.getNomeFaculdade());
        values.put("curso", aluno.getCurso());

        return registro.insert("alunos", null, values);
    }

    // Método para atualizar um aluno no banco de dados
    public int atualizarAluno(Aluno aluno) {
        ContentValues values = new ContentValues();
        values.put("nomeCompleto", aluno.getNomeCompleto());
        values.put("cpf", aluno.getCpf());
        values.put("rgm", aluno.getRgm());
        values.put("nomeFaculdade", aluno.getNomeFaculdade());
        values.put("curso", aluno.getCurso());

        return registro.update("alunos", values, "id = ?", new String[]{String.valueOf(aluno.getId())});
    }
    //Método para saber se um cpf foi cadastrado

    public boolean isCPFCadastrado(String cpf) {
        String[] colunas = {"id"};
        String selecao = "cpf = ?";

        Cursor cursor = registro.query("alunos", colunas, selecao, new String[]{cpf}, null, null, null);

        boolean cpfCadastrado = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }

        return cpfCadastrado;
    }


    //Método para ver se um RGM foi cadastrado
    public boolean isRGMCadastrado(String rgm) {
        String[] colunas = {"id"};
        String selecao = "rgm = ?";

        Cursor cursor = registro.query("alunos", colunas, selecao, new String[]{String.valueOf(rgm)}, null, null, null);

        boolean rgmCadastrado = cursor != null && cursor.getCount() > 0;
        if (cursor != null) {
            cursor.close();
        }

        return rgmCadastrado;
    }




    // Método para obter um aluno com base no RGM
    public int obterIdPeloRGM(String rgm) {
        int alunoId = -1; // Valor padrão se o aluno não for encontrado

        if (registro == null) {
            return alunoId; // Retorna o valor padrão se o registro for nulo
        }

        String[] colunas = {"id"};
        String selecao = "rgm = ?";

        Cursor cursor = registro.query("alunos", colunas, selecao, new String[]{rgm}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("id");
                if (idIndex >= 0) {
                    alunoId = cursor.getInt(idIndex);
                }
            }
            cursor.close();
        }

        return alunoId;
    }


    // Método para obter um aluno com base no ID
    public Aluno getAlunoPorId(int alunoId) {
        Aluno aluno = null;
        String[] colunas = {"id", "nomeCompleto", "cpf", "rgm", "senha", "nomeFaculdade", "curso"};
        String selecao = "id = ?"; // Condição para a consulta

        Cursor cursor = registro.query("alunos", colunas, selecao, new String[]{String.valueOf(alunoId)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                aluno = cursorToAluno(cursor);
            }
            cursor.close();
        }

        return aluno;
    }

    //Metódo para pegar id pelo cpf
    public int obterIdPeloCPF(String cpf) {
        int alunoId = -1; // Valor padrão se o aluno não for encontrado

        if (registro == null) {
            return alunoId; // Retorna o valor padrão se o registro for nulo
        }

        String[] colunas = {"id"};
        String selecao = "cpf = ?";

        Cursor cursor = registro.query("alunos", colunas, selecao, new String[]{cpf}, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex("id");
                if (idIndex >= 0) {
                    alunoId = cursor.getInt(idIndex);
                }
            }
            cursor.close();
        }

        return alunoId;
    }


//Cursor parar indexar dados/construir
    private Aluno cursorToAluno(Cursor cursor) {
        int idIndex = cursor.getColumnIndex("id");
        int nomeCompletoIndex = cursor.getColumnIndex("nomeCompleto");
        int cpfIndex = cursor.getColumnIndex("cpf");
        int rgmIndex = cursor.getColumnIndex("rgm");
        int senhaIndex = cursor.getColumnIndex("senha");
        int nomeFaculdadeIndex = cursor.getColumnIndex("nomeFaculdade");
        int cursoIndex = cursor.getColumnIndex("curso");

        int id = cursor.getInt(idIndex);
        String nomeCompleto = cursor.getString(nomeCompletoIndex);
        String cpf = cursor.getString(cpfIndex);
        String rgm = cursor.getString(rgmIndex);
        String senha = cursor.getString(senhaIndex);
        String nomeFaculdade = cursor.getString(nomeFaculdadeIndex);
        String curso = cursor.getString(cursoIndex);

        Aluno aluno = new Aluno();
        aluno.setId(id);
        aluno.setNomeCompleto(nomeCompleto);
        aluno.setCpf(cpf);
        aluno.setRgm(rgm);
        aluno.setSenha(senha);
        aluno.setNomeFaculdade(nomeFaculdade);
        aluno.setCurso(curso);

        return aluno;
    }
}