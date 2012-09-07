package br.com.casadocodigo.boaviagem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

	private static final String BANCO_DADOS = "BoaViagem";
	private static int VERSAO = 2;
	
	public static class Viagem {
		public static final String TABELA = "viagem";
		public static final String _ID = "_id";
		public static final String DESTINO = "destino";
		public static final String DATA_CHEGADA = "data_chegada";
		public static final String DATA_SAIDA = "data_saida";
		public static final String ORCAMENTO = "orcamento";
		public static final String QUANTIDADE_PESSOAS = "quantidade_pessoas";
		public static final String TIPO_VIAGEM = "tipo_viagem";
		
		public static final String[] COLUNAS = new String[]{
							_ID, DESTINO, DATA_CHEGADA, DATA_SAIDA,  
							TIPO_VIAGEM, ORCAMENTO, QUANTIDADE_PESSOAS };
	}
	
	public static class Gasto{
		public static final String TABELA = "gasto";
		public static final String _ID = "_id";
		public static final String VIAGEM_ID = "viagem_id";
		public static final String CATEGORIA = "categoria";
		public static final String DATA = "data";
		public static final String DESCRICAO = "descricao";
		public static final String VALOR = "valor";
		public static final String LOCAL = "local";
		
		public static final String[] COLUNAS = new String[]{
			_ID, VIAGEM_ID, CATEGORIA, DATA, DESCRICAO, VALOR, LOCAL
		};
	}
	
	public static interface Anotacao{
		public static final String TABELA = "anotacao";
		public static final String _ID = "_id";
		public static final String DIA = "dia";
		public static final String TITULO = "titulo";
		public static final String DESCRICAO = "descricao";
		
		public static final String[] COLUNAS = new String[]{
							_ID, DIA, TITULO, DESCRICAO
		};
	}
	
	public DatabaseHelper(Context context) {
		super(context, BANCO_DADOS, null, VERSAO);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE viagem (_id INTEGER PRIMARY KEY," +
					" destino TEXT, tipo_viagem INTEGER, data_chegada DATE," +
					" data_saida DATE, orcamento DOUBLE, quantidade_pessoas INTEGER);");
		
		db.execSQL("CREATE TABLE gasto (_id INTEGER PRIMARY KEY," +
					" categoria TEXT, data DATE, valor DOUBLE, descricao TEXT," +
					" local TEXT, viagem_id INTEGER," +
					" FOREIGN KEY(viagem_id) REFERENCES viagem(_id));");
		
		db.execSQL("CREATE TABLE anotacao (_id INTEGER PRIMARY KEY," +
				   " dia INTEGER, titulo TEXT, descricao TEXT);");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE viagem;");
		db.execSQL("DROP TABLE gasto;");
		onCreate(db);
	}
}