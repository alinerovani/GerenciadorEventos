package com.example.gerenciadordeeventos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public class CadastroEventos extends AppCompatActivity {
    private EditText txtTitulo;
    private EditText txtDescricao;
    private EditText txtData;
    private EditText txtValor;
    private EditText txtNumeroVagas;
    private boolean edit = false;
    private Integer IDEvento = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_eventos);

        txtTitulo = findViewById(R.id.txtTitulo);
        txtDescricao = findViewById(R.id.txtDescricao);
        txtData = findViewById(R.id.txtData);
        txtValor = findViewById(R.id.txtValorInscricao);
        txtNumeroVagas = findViewById(R.id.txtNumeroVagas);

//criar a mascara
        SimpleMaskFormatter simpleMaskFormatter =
                new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher maskTextWatcher =
                new MaskTextWatcher(txtData, simpleMaskFormatter);
        txtData.addTextChangedListener(maskTextWatcher);

        SimpleMaskFormatter simpleMaskFormatterValor =
                new SimpleMaskFormatter("NNN,NN");
        MaskTextWatcher maskTextWatcherValor =
                new MaskTextWatcher(txtValor, simpleMaskFormatterValor);
        txtValor.addTextChangedListener(maskTextWatcherValor);

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            Evento evento = (Evento) getIntent().getSerializableExtra("evento");

            txtTitulo.setText(evento.getTitulo());
            txtDescricao.setText(evento.getDescricao());
            txtData.setText(evento.getData());
            txtValor.setText(evento.getValor());
            txtNumeroVagas.setText(String.valueOf(evento.getNumeroVagas()));
            IDEvento = evento.getId();
            edit = true;
        }

        Button btnExcluir = findViewById(R.id.btnExcluir);
        if (edit == false) {
            btnExcluir.setEnabled(false);

            getSupportActionBar().hide();
        } else {
            btnExcluir.setEnabled(true);

            getSupportActionBar().show();
            getSupportActionBar().setTitle("Evento");
        }
    }

    private String ValidarCamposObrigatorios() {
        String retorno = "";
        if (txtTitulo.getText().toString().equals("")) {
            retorno += "Título do evento não informado. \n";
        }

        if (txtDescricao.getText().toString().equals("")) {
            retorno += "Descrição do evento não informada. \n";
        }

        if (txtData.getText().toString().equals("")) {
            retorno += "Data do evento não informada. \n";
        }

        if (txtNumeroVagas.getText().toString().equals("")) {
            retorno += "Número de vagas não informado. \n";
        }

        if (txtValor.getText().toString().equals("")) {
            retorno += "Valor da inscrição não informado.";
        }

        return retorno;
    }

    private void LimpaCampos() {
        txtTitulo.setText("");
        txtDescricao.setText("");
        txtData.setText("");
        txtValor.setText("");
        txtNumeroVagas.setText("");
    }

    public void Salvar(View view) {

        String retornoValidacao = ValidarCamposObrigatorios();
        if (!retornoValidacao.equals("")) {
            Toast toast = Toast.makeText(this, retornoValidacao, Toast.LENGTH_LONG);
            toast.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.mensagemDeErro));
            toast.show();

            return;
        }

        EventoController eventoController = new EventoController(this);

        String resultado = "";

        if (edit == false) {
            resultado = eventoController.InsereDados(txtTitulo.getText().toString(), txtDescricao.getText().toString(), txtData.getText().toString(), txtValor.getText().toString(), Integer.valueOf(txtNumeroVagas.getText().toString()));
        } else {
            resultado = eventoController.AlteraRegistro(IDEvento, txtTitulo.getText().toString(), txtDescricao.getText().toString(), txtData.getText().toString(), txtValor.getText().toString(), Integer.valueOf(txtNumeroVagas.getText().toString()));
        }

        if (resultado.contains("Erro")) {
            Toast toast = Toast.makeText(this, resultado, Toast.LENGTH_LONG);
            toast.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.mensagemDeErro));
            toast.show();

            return;
        } else {
            Toast toast = Toast.makeText(this, resultado, Toast.LENGTH_SHORT);
            toast.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.mensagemDeSucesso));
            toast.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mostrarConsultaEventos();
                }
            }, 2000);
        }
    }

    public void Excluir(View view) {
        if (edit == true) {

            //Verifica se tem inscritos para não permitir excluir
            ParticipanteEventoController participanteEventoController = new ParticipanteEventoController(this);
            Cursor cursor = participanteEventoController.CarregaDados(DBHelper.PARTICIPANTEEVENTO_COLUMN_IDEVENTO + " = " + IDEvento);
            if (cursor != null) {
                if (cursor.getCount() > 0) {

                    Toast toast = Toast.makeText(this, "Não é possível excluir o evento pois existem participantes cadastrados", Toast.LENGTH_LONG);
                    toast.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.mensagemDeErro));
                    toast.show();

                    return;
                }
            }

            EventoController ec = new EventoController(this);
            ec.DeletaRegistro(IDEvento);

            Toast toast = Toast.makeText(this, "Evento excluído", Toast.LENGTH_SHORT);
            toast.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.mensagemDeSucesso));
            toast.show();

            Intent intent = new Intent(this, ConsultaEventos.class);
            startActivity(intent);
        }
    }

    private void mostrarConsultaEventos() {
        Intent intent = new Intent(
                this, ConsultaEventos.class
        );
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.login).setVisible(false);
        menu.findItem(R.id.sair).setVisible(false);
        menu.findItem(R.id.listarParticipantes).setVisible(true);

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.listarParticipantes:
                Intent intent = new Intent(this, ConsultaParticipantes.class);
                intent.putExtra("idEvento", IDEvento);
                startActivity(intent);

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
