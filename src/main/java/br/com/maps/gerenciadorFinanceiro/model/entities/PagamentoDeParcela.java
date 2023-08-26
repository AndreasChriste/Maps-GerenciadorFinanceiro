package br.com.maps.gerenciadorFinanceiro.model.entities;


import java.time.LocalDate;
import java.util.Objects;

public class PagamentoDeParcela {
	private long id;
	private long id_divida;
	private Parcela parcela;


	public PagamentoDeParcela(long id,long id_divida,double valorDoPagamento,LocalDate dataDoPagamento) {
		this.id = id;
		this.id_divida = id_divida;
		this.parcela = new Parcela(valorDoPagamento, dataDoPagamento);
	}

	public long getId() {
		return id;
	}
	
	public Parcela getParcela() {
		return parcela;
	}


	public long getId_divida() {
		return id_divida;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PagamentoDeParcela other = (PagamentoDeParcela) obj;
		return id == other.id;
	}

	
}
