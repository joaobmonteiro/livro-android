package br.com.casadocodigo.boaviagem;

import android.os.Bundle;
import br.com.casadocodigo.boaviagem.domain.Anotacao;

public interface AnotacaoListener {
	void viagemSelecionada(Bundle bundle);
	void anotacaoSelecionada(Anotacao anotacao);
	void novaAnotacao();
}