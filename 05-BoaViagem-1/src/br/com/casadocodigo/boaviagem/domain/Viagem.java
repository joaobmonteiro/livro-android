package br.com.casadocodigo.boaviagem.domain;

import java.util.Date;

public class Viagem {
	private Integer id;
	private String destino;
	private Integer tipoViagem;
	private Date dataChegada;
	private Date dataSaida;
	private Double orcamento;
	private Integer quantidadePessoas;
	
	public Viagem(){}
	
	public Viagem(int id, String destino, Integer tipoViagem, Date dataChegada,
			Date dataSaida, Double orcamento, Integer quantidadePessoas) {
		this.id = id;
		this.destino = destino;
		this.tipoViagem = tipoViagem;
		this.dataChegada = dataChegada;
		this.dataSaida = dataSaida;
		this.orcamento = orcamento;
		this.quantidadePessoas = quantidadePessoas;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDestino() {
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public Integer getTipoViagem() {
		return tipoViagem;
	}
	public void setTipoViagem(Integer tipoViagem) {
		this.tipoViagem = tipoViagem;
	}
	public Date getDataChegada() {
		return dataChegada;
	}
	public void setDataChegada(Date dataChegada) {
		this.dataChegada = dataChegada;
	}
	public Date getDataSaida() {
		return dataSaida;
	}
	public void setDataSaida(Date dataSaida) {
		this.dataSaida = dataSaida;
	}
	public Double getOrcamento() {
		return orcamento;
	}
	public void setOrcamento(Double orcamento) {
		this.orcamento = orcamento;
	}
	public Integer getQuantidadePessoas() {
		return quantidadePessoas;
	}
	public void setQuantidadePessoas(Integer quantidadePessoas) {
		this.quantidadePessoas = quantidadePessoas;
	}
}
