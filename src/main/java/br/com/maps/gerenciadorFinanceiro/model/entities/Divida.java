package br.com.maps.gerenciadorFinanceiro.model.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.maps.gerenciadorFinanceiro.model.services.taxas.TaxaBase252;
import br.com.maps.gerenciadorFinanceiro.model.services.taxas.TaxaDeJurosDiario;

public class Divida {
	private long id;
	private double principal;
	private double taxaDeJuros;
	private double saldo;
	private double juros;
	private LocalDate dataDeAquisicaoDaDivida;
	private TaxaDeJurosDiario taxaDejurosDiario;
	private List<PagamentoDeParcela> conjuntoDePagamento;
	private double TotalDeJurosPago;
	private boolean quitado;

	public Divida(long id, double principal, double taxaDeJuros, LocalDate dataDeAquisicaoDaDivida) {
		this.id = id;
		this.principal = principal;
		this.taxaDeJuros = taxaDeJuros;
		this.saldo = principal;
		this.juros = 0;
		this.dataDeAquisicaoDaDivida = dataDeAquisicaoDaDivida;
		this.taxaDejurosDiario = new TaxaBase252();
		this.TotalDeJurosPago = 0;
		this.quitado = false;
		this.conjuntoDePagamento = new ArrayList<>();
	}

	public long getId() {
		return id;
	}

	public double getPrincipal() {
		return principal;
	}

	public double getTaxaDeJuros() {
		return taxaDeJuros;
	}

	public double getSaldo() {
		return saldo;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public double getJuros() {
		return juros;
	}

	public void setJuros(double juros) {
		this.juros = juros;
	}

	public LocalDate getDataDeAquisicaoDaDivida() {
		return dataDeAquisicaoDaDivida;
	}

	public TaxaDeJurosDiario getTaxaDejurosDiario() {
		return taxaDejurosDiario;
	}

	public double getTotalDeJurosPago() {
		return TotalDeJurosPago;
	}

	public void setTotalDeJurosPago(double totalDeJurosPago) {
		TotalDeJurosPago = totalDeJurosPago;
	}

	public boolean getQuitado() {
		return quitado;
	}

	public void setQuitado(boolean quitado) {
		this.quitado = quitado;
	}

	public List<PagamentoDeParcela> getConjuntoDePagamento() {
		return conjuntoDePagamento;
	}

	private String ExibirConjuntoDePagamento() {

		String saida = "conjunto de Pagamentos: [ ";
		for (PagamentoDeParcela pagamento : conjuntoDePagamento) {
			saida += "id: " + pagamento.getId() + " valor: " + pagamento.getParcela().getValorDoPagamento() + " / ";
		}
		saida += "]";
		return saida;
	}

	@Override
	public String toString() {
		return "Divida [id=" + id + ", principal=" + principal + ", taxaDeJuros=" + taxaDeJuros + ", saldo=" + saldo
				+ ", juros=" + juros + ", dataDeAquisicaoDaDivida=" + dataDeAquisicaoDaDivida
				+ ExibirConjuntoDePagamento() + ", TotalDeJurosPago=" + TotalDeJurosPago + ", quitado=" + quitado + "]";
	}

}
