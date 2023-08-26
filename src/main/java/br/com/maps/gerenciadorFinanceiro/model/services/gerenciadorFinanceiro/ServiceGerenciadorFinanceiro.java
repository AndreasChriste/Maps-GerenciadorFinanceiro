package br.com.maps.gerenciadorFinanceiro.model.services.gerenciadorFinanceiro;

import java.time.LocalDate;

import br.com.maps.gerenciadorFinanceiro.exceptions.ValorPagoInvalidoException;
import br.com.maps.gerenciadorFinanceiro.model.entities.Divida;
import br.com.maps.gerenciadorFinanceiro.model.entities.PagamentoDeParcela;
import br.com.maps.gerenciadorFinanceiro.model.services.formatacao.FormatacaoDecimalDuasCasas;

public class ServiceGerenciadorFinanceiro {
	public static double calculaJuros(double saldo, double taxaDeJurosDiaria) {
		double novoJuros = FormatacaoDecimalDuasCasas.formataDecimalDuasCasas((saldo * taxaDeJurosDiaria) - saldo);
		return novoJuros;
	}

	// Função que abate o valor pago do juros e retorna o restante do pagamento;
	public static double abaterOPagamentoDoJuros(double juros, double valorDoPagamento) {
		if (valorDoPagamento >= juros) {
			double restanteSobreOJuros = valorDoPagamento - juros;
			return restanteSobreOJuros;
		} else {
			return 0;
		}
	}

	// Função que atualiza o atributo saldo. Retorna o restante do saldo;
	public static double abaterDoSaldo(double saldo, double valorDoPagamento) {
		double novoSaldo;
		if (valorDoPagamento == saldo) {
			novoSaldo = 0;
			return novoSaldo;
		} else {
			novoSaldo = saldo - valorDoPagamento;
			return novoSaldo;
		}
	}

	// devolve o o Saldo atualizado entre o momento atual da dívida e a data do
	// pagamento
	public static double atualizaSaldoParaAData(Divida divida, double saldoParaAData, PagamentoDeParcela pagamento) {
		if (divida == null) {
			throw new RuntimeException("Divida inválida");
		}
		if (saldoParaAData < 0) {
			throw new ValorPagoInvalidoException("saldo inválido");
		}
		if (pagamento == null) {
			throw new RuntimeException("Pagamento inválido");
		}
		LocalDate dataDoPagamentoDaParcela = pagamento.getParcela().getDataDoPagamento();
		LocalDate dataDaDivida = divida.getDataDeAquisicaoDaDivida();// data de aquisição da divida
		double taxaDeJuros = divida.getTaxaDeJuros();// taxa de juros anual da divida
		double jurosDiario = divida.getTaxaDejurosDiario().calculaTaxaDeJurosDiario(taxaDeJuros, dataDaDivida,
				dataDoPagamentoDaParcela);
		double novoJuros = calculaJuros(saldoParaAData, jurosDiario);
		double valorDoPagamento = pagamento.getParcela().getValorDoPagamento();
		double saldoAtualizadoParaData;

		if (valorDoPagamento == saldoParaAData + novoJuros)
			return 0;
		if (valorDoPagamento >= novoJuros) {
			valorDoPagamento = abaterOPagamentoDoJuros(novoJuros, valorDoPagamento);

			if (valorDoPagamento == saldoParaAData) {
				saldoAtualizadoParaData = calculaJuros(saldoParaAData, jurosDiario);
			} else {
				saldoAtualizadoParaData = abaterDoSaldo(saldoParaAData, valorDoPagamento);
				;
			}
		} else {
			double jurosAtualizado = novoJuros - valorDoPagamento;

			saldoAtualizadoParaData = saldoParaAData + jurosAtualizado;

		}
		return saldoAtualizadoParaData;
	}

	// função que devolve o valor do juros dado uma data de pesquisa
	public static double atualizaOJurosParaAData(Divida divida, double saldoParaAData, LocalDate dataDePesquisa) {
		if (divida == null) {
			throw new RuntimeException("Divida inválida");
		}
		if (saldoParaAData < 0) {
			throw new ValorPagoInvalidoException("saldo inválido");
		}

		LocalDate dataDaDivida = divida.getDataDeAquisicaoDaDivida();// data de aquisição da divida

		if (dataDaDivida.isAfter(dataDePesquisa)) {
			throw new RuntimeException("Data de divida posterior a data pesquisada");
		}
		double taxaDeJuros = divida.getTaxaDeJuros();// taxa de juros anual da divida
		double jurosDiario = divida.getTaxaDejurosDiario().calculaTaxaDeJurosDiario(taxaDeJuros, dataDaDivida,
				dataDePesquisa);
		double novoJuros = calculaJuros(saldoParaAData, jurosDiario);
		return novoJuros;
	}

	// devolde o valor do juros pago entre o saldo atual da divida e o pagamento da
	// parcela
	public static double devolveJurosPago(Divida divida, double saldoParaAData, PagamentoDeParcela pagamento) {
		if (divida == null) {
			throw new RuntimeException("Divida inválida");
		}
		if (saldoParaAData < 0) {
			throw new ValorPagoInvalidoException("saldo inválido");
		}
		if (pagamento == null) {
			throw new RuntimeException("Pagamento inválido");
		}
		LocalDate dataDoPagamentoDaParcela = pagamento.getParcela().getDataDoPagamento();
		LocalDate dataDaDivida = divida.getDataDeAquisicaoDaDivida();// data de aquisição da divida
		double taxaDeJuros = divida.getTaxaDeJuros();// taxa de juros anual da divida
		double jurosDiario = divida.getTaxaDejurosDiario().calculaTaxaDeJurosDiario(taxaDeJuros, dataDaDivida,
				dataDoPagamentoDaParcela);
		double novoJuros = calculaJuros(saldoParaAData, jurosDiario);
		double valorDoPagamento = pagamento.getParcela().getValorDoPagamento();

		if (valorDoPagamento >= novoJuros) {
			return novoJuros;
		}
		return valorDoPagamento;
	}

	// dada uma dívida, se quitada, devolve a data de quitação,caso contrario,
	// devolve nulo;
	public static LocalDate confereDataDeQuitacao(Divida divida) {
		if (divida == null) {
			throw new RuntimeException("Divida inválida");
		}
		if (divida.getQuitado()) {
			// retorna a data da ultima parcela se estiver quitado
			if (!divida.getConjuntoDePagamento().isEmpty()) {
				int ultimoIndice = divida.getConjuntoDePagamento().size() - 1;
				return divida.getConjuntoDePagamento().get(ultimoIndice).getParcela().getDataDoPagamento();
			} else {
				throw new RuntimeException("conjunto de pagamentos vazio");
			}
		}
		return null;
	}

}
