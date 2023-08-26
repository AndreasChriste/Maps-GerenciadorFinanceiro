package br.com.maps.gerenciadorFinanceiro.model.entities;

import java.time.LocalDate;

public class VisaoDaDivida {
	private long id_divida;
	private double principal;
	private LocalDate dataDeAquisicaoDaDivida;
	private double saldoParaAData;
	private double valorDeJurosParaData;
	private double totalDeJurosPagosAteAData;
	private LocalDate dataDeQuitacao;

	public VisaoDaDivida(long id_divida, double principal, LocalDate dataDeAquisicaoDaDivida, double saldoParaAData,
			double valorDeJurosParaData, double totalDeJurosPagosAteAData, LocalDate dataDeQuitacao) {
		this.id_divida = id_divida;
		this.principal = principal;
		this.dataDeAquisicaoDaDivida = dataDeAquisicaoDaDivida;
		this.saldoParaAData = saldoParaAData;
		this.valorDeJurosParaData = valorDeJurosParaData;
		this.totalDeJurosPagosAteAData = totalDeJurosPagosAteAData;
		this.dataDeQuitacao = dataDeQuitacao;
	}

	public long getId_divida() {
		return id_divida;
	}

	public double getPrincipal() {
		return principal;
	}

	public LocalDate getDataDeAquisicaoDaDivida() {
		return dataDeAquisicaoDaDivida;
	}

	public double getSaldoParaAData() {
		return saldoParaAData;
	}

	public double getValorDeJurosParaData() {
		return valorDeJurosParaData;
	}

	public double getTotalDeJurosPagosAteAData() {
		return totalDeJurosPagosAteAData;
	}

	public LocalDate getDataDeQuitacao() {
		return dataDeQuitacao;
	}

	@Override
	public String toString() {
		return "VisaoDaDivida [id_divida=" + id_divida + ", principal=" + principal + ", dataDeAquisicaoDaDivida="
				+ dataDeAquisicaoDaDivida + ", saldoParaAData=" + saldoParaAData + ", valorDeJurosParaData="
				+ valorDeJurosParaData + ", totalDeJurosPagosAteAData=" + totalDeJurosPagosAteAData
				+ ", dataDeQuitacao=" + dataDeQuitacao + "]";
	}

}
