package com.example.gerenciadordeeventos;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class ParticipanteEvento implements Serializable  {

    private String nomeUsuario;
    private String emailUsuario;

    public ParticipanteEvento(String nomeUsuario, String emailUsuario) {

        this.nomeUsuario = nomeUsuario;
        this.emailUsuario = emailUsuario;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getEmailUsuario() {
        return emailUsuario;
    }

    public void setEmailUsuario(String emailUsuario) {
        this.emailUsuario = emailUsuario;
    }

    @NonNull
    @Override
    public String toString() {
        return "Usuário: " + getNomeUsuario() + "\n" + "E-mail: " + getEmailUsuario();
    }
}
