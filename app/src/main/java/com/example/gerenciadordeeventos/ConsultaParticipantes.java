package com.example.gerenciadordeeventos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ConsultaParticipantes extends AppCompatActivity {
    private ListView listParticipantes;
    private List<ParticipanteEvento> participantes;
    private ArrayAdapter<ParticipanteEvento> participantesAdaptador;
    private int idEvento = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_participantes);

        getSupportActionBar().hide();

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            idEvento = extra.getInt("idEvento");
        }

        listParticipantes = findViewById(R.id.lv_participantes);

        CarregarParticipantes();
    }

    private void CarregarParticipantes() {
        participantes = new ArrayList<ParticipanteEvento>();

        //Pega os inscritos no evento
        ParticipanteEventoController participanteEventoController = new ParticipanteEventoController(this);
        Cursor cursor = participanteEventoController.CarregaDadosParticipantes(idEvento);

        if (cursor == null) {
            Toast toast = Toast.makeText(this, "Não há nenhum participante inscrito", Toast.LENGTH_SHORT);
            toast.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.mensagemDeSucesso));
            toast.show();
            return;

        } else if (cursor.getCount() == 0)
        {
            Toast toast = Toast.makeText(this, "Não há nenhum participante inscrito", Toast.LENGTH_SHORT);
            toast.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.mensagemDeSucesso));
            toast.show();
            return;
        }

        if (cursor.moveToFirst()) {
            do {
                ParticipanteEvento linhaParticipante = new ParticipanteEvento(
                        cursor.getString(cursor.getColumnIndex(DBHelper.USUARIO_COLUMN_NOME)),
                        cursor.getString(cursor.getColumnIndex(DBHelper.USUARIO_COLUMN_EMAIL)));
                participantes.add(linhaParticipante);
            } while (cursor.moveToNext());
        }
        cursor.close();

        participantesAdaptador = new ArrayAdapter<ParticipanteEvento>(this,
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                participantes);
        listParticipantes.setAdapter(participantesAdaptador);

    }
}
