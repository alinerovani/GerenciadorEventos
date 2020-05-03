package com.example.gerenciadordeeventos;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Evento implements Serializable {
    private Integer id;
    private String titulo;
    private String descricao;
    private String data;
    private String valor;
    private Integer numeroVagas;

    public Evento(int id, String titulo, String descricao, String data, String valor, Integer numeroVagas) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.data = data;
        this.valor = valor;
        this.numeroVagas = numeroVagas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Integer getNumeroVagas() {
        return numeroVagas;
    }

    public void setNumeroVagas(Integer numeroVagas) {
        this.numeroVagas = numeroVagas;
    }

    @NonNull
    @Override
    public String toString() {
        return "Título: " + getTitulo() + "\nDescrição: " + getDescricao()+ "\nData: " + getData() + "\nValor da Inscrição: " + getValor() + "\nNúmero de Vagas: " + getNumeroVagas() ;
    }
}
