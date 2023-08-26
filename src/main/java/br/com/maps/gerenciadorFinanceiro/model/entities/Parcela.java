package br.com.maps.gerenciadorFinanceiro.model.entities;


import java.time.LocalDate;

public class Parcela {
	private double valorDoPagamento;
	private LocalDate dataDoPagamento;
	
	public Parcela(double valorDoPagamento, LocalDate dataDoPagamento) {
		this.valorDoPagamento = valorDoPagamento;
		this.dataDoPagamento = dataDoPagamento;
	}
	public double getValorDoPagamento() {
		return valorDoPagamento;
	}

	public LocalDate getDataDoPagamento() {
		return dataDoPagamento;
	}
	
}
