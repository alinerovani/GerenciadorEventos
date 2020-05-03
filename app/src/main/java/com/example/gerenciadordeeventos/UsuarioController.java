package com.example.gerenciadordeeventos;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class UsuarioController {

    private SQLiteDatabase db;
    private DBHelper banco;

    public UsuarioController(Context context) {
        banco = new DBHelper(context);
    }

    public String InsereDados(String nome, String email, String senha, int adminitrador) {

        db = banco.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.USUARIO_COLUMN_NOME, nome);
        values.put(DBHelper.USUARIO_COLUMN_EMAIL, email);
        values.put(DBHelper.USUARIO_COLUMN_SENHA, senha);
        values.put(DBHelper.USUARIO_COLUMN_ADMINISTRADOR, adminitrador);

        long newRowId = db.insert(DBHelper.TABLE_USUARIO, null, values);
        db.close();
        if (newRowId == -1) {
            return "Erro ao inserir as informações!";
        } else {
            return "Usuário gravado com sucesso!" + newRowId;
        }
    }

    public Cursor CarregaDados(@Nullable String filtro) {
        Cursor cursor;
        String[] campos = {DBHelper.ID, DBHelper.USUARIO_COLUMN_NOME, DBHelper.USUARIO_COLUMN_EMAIL, DBHelper.USUARIO_COLUMN_SENHA, DBHelper.USUARIO_COLUMN_ADMINISTRADOR};
        db = banco.getReadableDatabase();
        cursor = db.query(DBHelper.TABLE_USUARIO, campos, filtro, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public void DeletaRegistro(int id) {
        String where = DBHelper.ID + "=" + id;
        db = banco.getReadableDatabase();
        db.delete(DBHelper.TABLE_USUARIO, where, null);
        db.close();
    }

    public String AlteraRegistro(int id, String nome, String email, String senha, int administrador) {
        ContentValues values;
        String where;
        db = banco.getWritableDatabase();
        where = DBHelper.ID + "=" + id;
        values = new ContentValues();
        values.put(DBHelper.USUARIO_COLUMN_NOME, nome);
        values.put(DBHelper.USUARIO_COLUMN_EMAIL, email);
        values.put(DBHelper.USUARIO_COLUMN_SENHA, senha);
        values.put(DBHelper.USUARIO_COLUMN_ADMINISTRADOR, administrador);

        int result = db.update(DBHelper.TABLE_USUARIO, values, where, null);
        db.close();
        if (result > 0)
            return "Evento alterado";
        else
            return "Não foi possível alterar o evento";
    }
}
