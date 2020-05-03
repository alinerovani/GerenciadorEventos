package com.example.gerenciadordeeventos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class ParticipanteEventoController {


    private SQLiteDatabase db;
    private DBHelper banco;

    public ParticipanteEventoController(Context context) {
        banco = new DBHelper(context);
    }

    public String InsereDados(int idUsuario, int idEvento, Context context) {

        //Pega os inscritos no evento
        Cursor cursorParticipantesEvento = CarregaDados(DBHelper.PARTICIPANTEEVENTO_COLUMN_IDEVENTO + " = " + idEvento);

        //Pega os dados do evento
        EventoController eventoController = new EventoController(context);
        Cursor cursorEvento = eventoController.CarregaDados(DBHelper.ID + " = " + idEvento);

        if (cursorParticipantesEvento != null) {

            if (cursorParticipantesEvento.getCount() >= cursorEvento.getInt(cursorEvento.getColumnIndex(DBHelper.EVENTO_COLUMN_NUMEROVAGAS))) {
                return "Não existem mais vagas disponíveis para esse evento";
            }
        }

        //valida se o usuário já não está inscrito
        Cursor cursor = CarregaDados(DBHelper.PARTICIPANTEEVENTO_COLUMN_IDEVENTO + " = " + idEvento + " AND " + DBHelper.PARTICIPANTEEVENTO_COLUMN_IDUSUARIO + " = " + idUsuario);
        if (cursor != null) {
            if (cursor.getCount() > 0)
                return "Você já está inscrito nesse evento";
        }


        db = banco.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBHelper.PARTICIPANTEEVENTO_COLUMN_IDEVENTO, idEvento);
        values.put(DBHelper.PARTICIPANTEEVENTO_COLUMN_IDUSUARIO, idUsuario);

        long newRowId = db.insert(DBHelper.TABLE_PARTICIPANTESEVENTO, null, values);
        db.close();
        if (newRowId == -1) {
            return "Erro ao realizar a inscrição!";
        } else {
            return "Inscrição realizada com sucesso!";
        }
    }

    public Cursor CarregaDados(@Nullable String filtro) {
        Cursor cursor;
        String[] campos = {DBHelper.ID, DBHelper.PARTICIPANTEEVENTO_COLUMN_IDEVENTO, DBHelper.PARTICIPANTEEVENTO_COLUMN_IDUSUARIO};
        db = banco.getReadableDatabase();
        cursor = db.query(DBHelper.TABLE_PARTICIPANTESEVENTO, campos, filtro, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public Cursor CarregaDadosParticipantes(int idEvento) {
        Cursor cursor;
        db = banco.getReadableDatabase();
        cursor = db.rawQuery("Select usu." + DBHelper.USUARIO_COLUMN_NOME + ", usu." + DBHelper.USUARIO_COLUMN_EMAIL + " From " + DBHelper.TABLE_USUARIO + " as usu Inner Join " + DBHelper.TABLE_PARTICIPANTESEVENTO + " as part on part." + DBHelper.PARTICIPANTEEVENTO_COLUMN_IDUSUARIO + " = usu." + DBHelper.ID + " Where part." + DBHelper.PARTICIPANTEEVENTO_COLUMN_IDEVENTO + " = " + idEvento, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public void DeletaRegistro(int id) {
        String where = DBHelper.ID + "=" + id;
        db = banco.getReadableDatabase();
        db.delete(DBHelper.TABLE_PARTICIPANTESEVENTO, where, null);
        db.close();
    }
}
