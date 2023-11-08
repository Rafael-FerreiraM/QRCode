package com.example.qrbola7.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConnectionFactory extends SQLiteOpenHelper {

    private static final String NAME = "registro.db";
    private static final int VERSION = 1;

    public ConnectionFactory(@Nullable Context context){
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table alunos (id integer primary key autoincrement," +
                "nomeCompleto varchar(50), cpf varchar(50), rgm varchar(50), senha varchar(50), " +
                " nomeFaculdade varchar(50), curso varchar(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        String sql =  "DROP TABLE IF EXISTS alunos";
        db.execSQL(sql);
        onCreate(db);
    }
}

