package com.example.gerenciadordeeventos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CadastroUsuario extends AppCompatActivity {
    private EditText txtNome;
    private EditText txtEmail;
    private EditText txtSenha;
    private boolean edit = false;
    private Integer IDUsuario = 0;
    private Integer IDEvento = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        getSupportActionBar().hide();

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            IDEvento = extra.getInt("idEvento");
        }

        txtNome = findViewById(R.id.txtNome);
        txtSenha = findViewById(R.id.txtSenha);
        txtEmail = findViewById(R.id.txtEmail);
    }

    public void Salvar(View view) {

        String retornoValidacao = ValidarCamposObrigatorios();
        if (!retornoValidacao.equals("")) {
            Toast toast = Toast.makeText(this, retornoValidacao, Toast.LENGTH_LONG);
            toast.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.mensagemDeErro));
            toast.show();

            return;
        }

        UsuarioController usuarioController = new UsuarioController(this);

        String resultado = "";

        if (edit == false) {
            resultado = usuarioController.InsereDados(txtNome.getText().toString(), txtEmail.getText().toString(), txtSenha.getText().toString(), 0);
        } else {
            resultado = usuarioController.AlteraRegistro(IDUsuario, txtNome.getText().toString(), txtEmail.getText().toString(), txtSenha.getText().toString(), 0);
        }

        if (resultado.contains("Erro")) {
            Toast toast = Toast.makeText(this, resultado, Toast.LENGTH_LONG);
            toast.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.mensagemDeErro));
            toast.show();

            return;
        } else {

            String array[] = new String[2];
            array = resultado.split("!");

            LimpaCampos();

            //Grava no sharedPreferences o ID do usuário logado
            SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("userID", Integer.valueOf(array[1]));
            editor.apply();

            //Inscreve o usuario no curso
            ParticipanteEventoController participanteEventoController = new ParticipanteEventoController(this);
            String resultadoInscricao = participanteEventoController.InsereDados(sharedPreferences.getInt("userID", 0), IDEvento, this);

            if (resultadoInscricao.contains("Erro")) {
                Toast toast = Toast.makeText(this, resultadoInscricao, Toast.LENGTH_LONG);
                toast.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.mensagemDeErro));
                toast.show();

                return;
            } else {
                Toast toast = Toast.makeText(this, resultadoInscricao, Toast.LENGTH_SHORT);
                toast.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.mensagemDeSucesso));
                toast.show();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mostrarConsultaEventos();
                }
            }, 3000);
        }

    }

    private void mostrarConsultaEventos() {
        Intent intent = new Intent(
                this, ConsultaEventos.class
        );
        startActivity(intent);
        finish();
    }

    private String ValidarCamposObrigatorios() {
        String retorno = "";
        if (txtNome.getText().toString().equals("")) {
            retorno += "Nome não informado. \n";
        }

        if (txtEmail.getText().toString().equals("")) {
            retorno += "E-mail não informado. \n";
        } else if (!txtEmail.getText().toString().contains("@")) {
            retorno += "Formato de e-mail inválido. \n";
        }

        if (txtSenha.getText().toString().equals("")) {
            retorno += "Senha não informada. \n";
        } else if (txtSenha.getText().toString().length() < 5) {
            retorno += "A senha deve conter pelo menos 5 caracteres. \n";
        }

        return retorno;
    }

    private void LimpaCampos() {
        txtNome.setText("");
        txtEmail.setText("");
        txtSenha.setText("");
    }
}
