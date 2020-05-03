package com.example.gerenciadordeeventos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ConsultaEventos extends AppCompatActivity {

    private ListView listEventos;
    private List<Evento> eventos;
    private ArrayAdapter<Evento> eventosAdaptador;
    private Evento eventoSelecionado;
    private Boolean usuarioAdministradorLogado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_eventos);

        getSupportActionBar().setTitle("Eventos");

        listEventos = findViewById(R.id.lv_eventos);

        CarregarEventos();

        //Verifica o usuário que está logado é o administrador
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        int userID = sharedPreferences.getInt("userID", 0);

        if (userID > 0) {

            UsuarioController usuarioController = new UsuarioController(this);
            Cursor cursor = usuarioController.CarregaDados(DBHelper.ID + " = " + userID);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();

                    if (cursor.getInt(cursor.getColumnIndex(DBHelper.USUARIO_COLUMN_ADMINISTRADOR)) == 1) {
                        usuarioAdministradorLogado = true;
                    }
                }
            }
        }

        FloatingActionButton layers = (FloatingActionButton) findViewById(R.id.btnCadastrarEvento);
        layers.hide();

        if (usuarioAdministradorLogado == true) {
            layers.show();
        }

        listEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eventoSelecionado = eventos.get(position);

                Evento evento = new Evento(eventoSelecionado.getId(), eventoSelecionado.getTitulo(), eventoSelecionado.getDescricao(), eventoSelecionado.getData(), eventoSelecionado.getValor(), eventoSelecionado.getNumeroVagas());

                if (usuarioAdministradorLogado == true) {
                    Intent intent = new Intent(view.getContext(), CadastroEventos.class);
                    intent.putExtra("evento", evento);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(view.getContext(), DetalhesEvento.class);
                    intent.putExtra("evento", evento);
                    startActivity(intent);
                }

            }
        });

    }

    private void CarregarEventos() {
        eventos = new ArrayList<Evento>();

        EventoController ac = new EventoController(this);
        Cursor cursor = ac.CarregaDados("");
        while (cursor.moveToNext()) {
            Evento linhaEvento = new Evento(
                    cursor.getInt(cursor.getColumnIndex(DBHelper.ID)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.EVENTO_COLUMN_TITULO)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.EVENTO_COLUMN_DESCRICAO)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.EVENTO_COLUMN_DATA)),
                    cursor.getString(cursor.getColumnIndex(DBHelper.EVENTO_COLUMN_VALOR)),
                    cursor.getInt(cursor.getColumnIndex(DBHelper.EVENTO_COLUMN_NUMEROVAGAS)));
            eventos.add(linhaEvento);
        }

        eventosAdaptador = new ArrayAdapter<Evento>(this,
                android.R.layout.simple_list_item_2,
                android.R.id.text2,
                eventos);
        listEventos.setAdapter(eventosAdaptador);
    }

    public void NovoEvento(View view) {
        Intent intent = new Intent(this, CadastroEventos.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        //Verifica se o usuário está logado
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        int userID = sharedPreferences.getInt("userID", 0);

        menu.findItem(R.id.listarParticipantes).setVisible(false);

        if (userID > 0) {
            menu.findItem(R.id.login).setVisible(false);
            menu.findItem(R.id.sair).setVisible(true);

        } else {
            menu.findItem(R.id.sair).setVisible(false);
            menu.findItem(R.id.login).setVisible(true);
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.sair:
                Logout();
                return true;
            case R.id.login:
                Login();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void Logout() {
        //Verifica se o usuário está logado
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        int userID = sharedPreferences.getInt("userID", 0);

        if (userID > 0) {
            sharedPreferences.edit().remove("userID").commit();

            Toast toast = Toast.makeText(this, "Logout realizado", Toast.LENGTH_SHORT);
            toast.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.mensagemDeSucesso));
            toast.show();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mostrarConsultaEventos();
                }
            }, 1000);

        } else
            return;
    }

    private void Login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void mostrarConsultaEventos() {
        Intent intent = new Intent(
                this, ConsultaEventos.class
        );
        startActivity(intent);
        finish();
    }

}
