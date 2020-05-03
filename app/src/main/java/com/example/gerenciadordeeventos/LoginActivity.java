package com.example.gerenciadordeeventos;

import android.app.Activity;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    int IDEvento = 0;
    private EditText txtLogin;
    private EditText txtSenha;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            IDEvento = extra.getInt("idEvento");
        }

        txtLogin = findViewById(R.id.username);
        txtSenha = findViewById(R.id.password);
    }

    public void Logar(View view) {

        String retornoValidacao = ValidarCamposObrigatorios();
        if (!retornoValidacao.equals("")) {
            Toast toast = Toast.makeText(this, retornoValidacao, Toast.LENGTH_LONG);
            toast.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.mensagemDeErro));
            toast.show();

            return;
        }

        UsuarioController usuarioController = new UsuarioController(this);
        Cursor cursor = usuarioController.CarregaDados(DBHelper.USUARIO_COLUMN_EMAIL + " = '" + txtLogin.getText().toString() + "' and " + DBHelper.USUARIO_COLUMN_SENHA + " = '" + txtSenha.getText().toString() + "'");

        if (cursor == null) {
            Toast toast = Toast.makeText(this, "Usuário não encontrado", Toast.LENGTH_LONG);
            toast.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.mensagemDeErro));
            toast.show();

            return;
        }

        //Grava no sharedPreferences o ID do usuário logado
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userID", Integer.valueOf(cursor.getInt(cursor.getColumnIndex(DBHelper.ID))));
        editor.apply();

        Intent intent = new Intent(this, ConsultaEventos.class);
        startActivity(intent);
    }


    private String ValidarCamposObrigatorios() {
        String retorno = "";
        if (txtLogin.getText().toString().equals("")) {
            retorno += "Login não informado. \n";
        }

        if (txtSenha.getText().toString().equals("")) {
            retorno += "Senha não informado.";
        }

        return retorno;
    }

    public void Registrar(View view) {
        Intent intent = new Intent(view.getContext(), CadastroUsuario.class);
        intent.putExtra("idEvento", IDEvento);
        startActivity(intent);
    }
}
