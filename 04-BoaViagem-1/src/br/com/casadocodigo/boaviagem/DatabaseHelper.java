package br.com.casadocodigo.boaviagem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

	private static final String BANCO_DADOS = "BoaViagem";
	private static int VERSAO = 1;

	public DatabaseHelper(Context context) {
		super(context, BANCO_DADOS, null, VERSAO);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE VIAGEM (_ID INTEGER PRIMARY KEY," +
					" DESTINO TEXT, TIPO_VIAGEM INTEGER, DATA_CHEGADA DATE," +
					" DATA_SAIDA DATE, ORCAMENTO DOUBLE, QUANTIDADE_PESSOAS INTEGER);");
		
		db.execSQL("CREATE TABLE GASTO (_ID INTEGER PRIMARY KEY," +
					" CATEGORIA TEXT, DATA DATE, VALOR DOUBLE, DESCRICAO TEXT," +
					" LOCAL TEXT, VIAGEM_ID INTEGER," +
					" FOREIGN KEY(VIAGEM_ID) REFERENCES VIAGEM(_ID));");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}