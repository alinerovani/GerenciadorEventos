package com.example.gerenciadordeeventos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DetalhesEvento extends AppCompatActivity {
    private TextView lblTitulo;
    private TextView lblData;
    private TextView lblNumeroVagas;
    private TextView lblValorInscricao;
    private TextView lblDescricao;
    private int IDEvento = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_evento);

        getSupportActionBar().hide();

        lblTitulo = findViewById(R.id.lblTitulo);
        lblData = findViewById(R.id.lblData);
        lblNumeroVagas = findViewById(R.id.lblNumeroVagas);
        lblValorInscricao = findViewById(R.id.lblValorInscricao);
        lblDescricao = findViewById(R.id.lblDescricao);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            Evento evento = (Evento) getIntent().getSerializableExtra("evento");

            IDEvento = evento.getId();
            lblTitulo.setText(evento.getTitulo());
            lblDescricao.setText(evento.getDescricao());
            lblData.setText(evento.getData());
            lblValorInscricao.setText(evento.getValor());
            lblNumeroVagas.setText(String.valueOf(evento.getNumeroVagas()));
        }
    }

    public void Inscricao(View view) {

        //Verifica se o usuário está logado
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        int userID = sharedPreferences.getInt("userID", 0);

        if (userID > 0)
        {
            //inscreve o usuário logado no curso
            ParticipanteEventoController participanteEventoController = new ParticipanteEventoController(this);
            String resultado = participanteEventoController.InsereDados(userID, IDEvento, this);

            if (resultado.contains("Erro")) {
                Toast toast = Toast.makeText(this, resultado, Toast.LENGTH_LONG);
                toast.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.mensagemDeErro));
                toast.show();

                return;
            } else {
                Toast toast = Toast.makeText(this, resultado, Toast.LENGTH_SHORT);
                toast.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.mensagemDeSucesso));
                toast.show();
            }

        } else  {
            Intent intent = new Intent(view.getContext(), LoginActivity.class);
            intent.putExtra("idEvento", IDEvento);
            startActivity(intent);
        }
    }
}
