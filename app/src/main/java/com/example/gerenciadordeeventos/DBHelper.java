package com.example.gerenciadordeeventos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "evento.db";

    // Table Names
    public static final String TABLE_EVENTO = "evento";
    public static final String TABLE_USUARIO = "usuario";
    public static final String TABLE_PARTICIPANTESEVENTO = "participantesevento";

    // Common column names
    public static final String ID = "_id";

    // Colunas da tabela de evento
    public static final String EVENTO_COLUMN_TITULO = "titulo";
    public static final String EVENTO_COLUMN_DESCRICAO = "descricao";
    public static final String EVENTO_COLUMN_DATA = "data";
    public static final String EVENTO_COLUMN_VALOR = "valor";
    public static final String EVENTO_COLUMN_NUMEROVAGAS = "numeroVagas";

    // Colunas da tabela de usuario
    public static final String USUARIO_COLUMN_NOME = "nome";
    public static final String USUARIO_COLUMN_EMAIL = "email";
    public static final String USUARIO_COLUMN_SENHA = "senha";
    public static final String USUARIO_COLUMN_ADMINISTRADOR = "administrador";

    // Colunas da tabela de participantes do evento
    public static final String PARTICIPANTEEVENTO_COLUMN_IDUSUARIO = "idusuario";
    public static final String PARTICIPANTEEVENTO_COLUMN_IDEVENTO = "idevento";

    private static final String SQL_CREATE_EVENTO =
            "CREATE TABLE " + TABLE_EVENTO + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    EVENTO_COLUMN_TITULO + " TEXT," +
                    EVENTO_COLUMN_DESCRICAO + " TEXT," +
                    EVENTO_COLUMN_DATA + " TEXT," +
                    EVENTO_COLUMN_VALOR + " TEXT," +
                    EVENTO_COLUMN_NUMEROVAGAS + " INTEGER)";

    private static final String SQL_CREATE_USUARIO =
            "CREATE TABLE " + TABLE_USUARIO + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    USUARIO_COLUMN_NOME + " TEXT," +
                    USUARIO_COLUMN_EMAIL + " TEXT," +
                    USUARIO_COLUMN_ADMINISTRADOR + " INTEGER," +
                    USUARIO_COLUMN_SENHA + " TEXT) ";

    private static final String SQL_CREATE_PARTICIPANTEEVENTO =
            "CREATE TABLE " + TABLE_PARTICIPANTESEVENTO + " (" +
                    ID + " INTEGER PRIMARY KEY," +
                    PARTICIPANTEEVENTO_COLUMN_IDEVENTO + " INTEGER," +
                    PARTICIPANTEEVENTO_COLUMN_IDUSUARIO + " INTEGER) ";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_EVENTO);
        db.execSQL(SQL_CREATE_USUARIO);
        db.execSQL(SQL_CREATE_PARTICIPANTEEVENTO);

        //Popula o banco com algumas informações padrão
        //Insere alguns eventos
        db.execSQL("INSERT INTO " + TABLE_EVENTO + " (" + EVENTO_COLUMN_TITULO + "," + EVENTO_COLUMN_DESCRICAO + "," + EVENTO_COLUMN_DATA + "," + EVENTO_COLUMN_VALOR + "," + EVENTO_COLUMN_NUMEROVAGAS + ") " +
                "VALUES " +
                "   ('Criptografia quântica','Introdução à criptografia quântica no qual serão vistas ferramentas básicas para compreender, projetar e analisar protocolos quânticos; protocolos de distribuição de chaves quânticas e como dispositivos quânticos não confiáveis podem ser avaliados, entre outros assuntos','10/10/2020', '200,00', 50), " +
                "   ('Computação Forense','Apresentação dos conceitos da Computação Forense e métodos de Investigação Digital, baseado no conteúdo apresentado nas certificações mais conhecidas do mercado','11/10/2020', '200,00', 50), " +
                "   ('Introdução a Arduino','Introdução geral que apresenta conceitos básicos de eletrônica e programação que serão úteis para desenvolver um projeto sobre Arduino, ou aprender sobre suas possibilidades','12/10/2020', '200,00', 50), " +
                "   ('Confiabilidade e Tolerância a Falhas','O projeto dos dispositivos e dos códigos para qualquer aplicação tem que considerar a possibilidade de falhas acontecerem','13/10/2020', '200,00', 50), " +
                "   ('Machine learning','O aprendizado de máquina é um campo que evoluiu do estudo de reconhecimento de padrões e da teoria do aprendizado computacional em inteligência artificial','14/10/2020', '200,00', 50);") ;

        //Insere um usuário administrador
        db.execSQL("INSERT INTO " + TABLE_USUARIO  + " (" + USUARIO_COLUMN_NOME + "," + USUARIO_COLUMN_EMAIL + "," + USUARIO_COLUMN_SENHA + "," + USUARIO_COLUMN_ADMINISTRADOR + ") " +
                "VALUES " +
                "   ('administrador','administrador@administrador.com','12345', 1);");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENTO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARTICIPANTESEVENTO);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
