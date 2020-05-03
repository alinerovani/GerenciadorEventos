package com.example.gerenciadordeeventos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class EventoController {
    private SQLiteDatabase db;
    private DBHelper banco;

    public EventoController(Context context) {
        banco = new DBHelper(context);
    }

    public String InsereDados(String titulo, String descricao, String data, String valorInscricao, int numeroVagas) {
        db = banco.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.EVENTO_COLUMN_TITULO, titulo);
        values.put(DBHelper.EVENTO_COLUMN_DESCRICAO, descricao);
        values.put(DBHelper.EVENTO_COLUMN_VALOR, valorInscricao);
        values.put(DBHelper.EVENTO_COLUMN_DATA, data);
        values.put(DBHelper.EVENTO_COLUMN_NUMEROVAGAS, numeroVagas);
        long newRowId = db.insert(DBHelper.TABLE_EVENTO, null, values);
        db.close();
        if (newRowId == -1) {
            return "Erro ao inserir informações!";
        } else {
            return "Evento gravado com sucesso!";
        }
    }

    public Cursor CarregaDados(@Nullable String filtro) {
        Cursor cursor;
        String[] campos = {DBHelper.ID, DBHelper.EVENTO_COLUMN_TITULO, DBHelper.EVENTO_COLUMN_DESCRICAO, DBHelper.EVENTO_COLUMN_DATA, DBHelper.EVENTO_COLUMN_NUMEROVAGAS, DBHelper.EVENTO_COLUMN_VALOR};
        db = banco.getReadableDatabase();
        cursor = db.query(DBHelper.TABLE_EVENTO, campos, filtro, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public void DeletaRegistro(int id) {
        String where = DBHelper.ID + "=" + id;
        db = banco.getReadableDatabase();
        db.delete(DBHelper.TABLE_EVENTO, where, null);
        db.close();
    }

    public String AlteraRegistro(int id, String titulo, String descricao, String data, String valorInscricao, int numeroVagas) {
        ContentValues values;
        String where;
        db = banco.getWritableDatabase();
        where = DBHelper.ID + "=" + id;
        values = new ContentValues();
        values.put(DBHelper.EVENTO_COLUMN_TITULO, titulo);
        values.put(DBHelper.EVENTO_COLUMN_DESCRICAO, descricao);
        values.put(DBHelper.EVENTO_COLUMN_VALOR, valorInscricao);
        values.put(DBHelper.EVENTO_COLUMN_DATA, data);
        values.put(DBHelper.EVENTO_COLUMN_NUMEROVAGAS, numeroVagas);
        int result = db.update(DBHelper.TABLE_EVENTO, values, where, null);
        db.close();
        if (result > 0)
            return "Evento alterado";
        else
            return "Não foi possível alterar o evento";
    }
}
