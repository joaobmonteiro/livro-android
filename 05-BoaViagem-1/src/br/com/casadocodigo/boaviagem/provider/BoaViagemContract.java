package br.com.casadocodigo.boaviagem.provider;

import android.net.Uri;

public final class BoaViagemContract {
	public static final String AUTHORITY = "br.com.casadocodigo.boaviagem.provider";
	public static final Uri AUTHORITY_URI = Uri.parse("content://" + AUTHORITY);
	public static final String VIAGEM_PATH = "viagem";
	public static final String GASTO_PATH = "gasto";
	
	public static final class Viagem{
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" +
						"vnd.br.com.casadocodigo.boaviagem.provider/viagem";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" +
						"vnd.br.com.casadocodigo.boaviagem.provider/viagem";

		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, VIAGEM_PATH);
		public static final String _ID = "_ID";
		public static final String DESTINO = "DESTINO";
		public static final String DATA_CHEGADA = "DATA_CHEGADA";
		public static final String DATA_SAIDA = "DATA_SAIDA";
		public static final String ORCAMENTO = "ORCAMENTO";
		public static final String QUANTIDADE_PESSOAS = "QUANTIDADE_PESSOAS";
	}

	public static final class Gasto{
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/" +
				"vnd.br.com.casadocodigo.boaviagem.provider/gasto";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/" +
				"vnd.br.com.casadocodigo.boaviagem.provider/gasto";

		public static final Uri CONTENT_URI = Uri.withAppendedPath(AUTHORITY_URI, GASTO_PATH);
		public static final String _ID = "_ID";
		public static final String VIAGEM_ID = "VIAGEM_ID";
		public static final String CATEGORIA = "CATEGORIA";
		public static final String DATA = "DATA";
		public static final String DESCRICAO = "DESCRICAO";
		public static final String LOCAL = "LOCAL";
	}
}
