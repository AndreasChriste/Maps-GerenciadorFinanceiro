package br.com.maps.gerenciadorFinanceiro.model.services.gerenciadorFinanceiro;

import java.time.LocalDate;

import br.com.maps.gerenciadorFinanceiro.model.entities.Divida;
import br.com.maps.gerenciadorFinanceiro.model.entities.PagamentoDeParcela;
import br.com.maps.gerenciadorFinanceiro.model.services.formatacao.FormatacaoDecimalDuasCasas;

public  class ServiceGerenciadorFinanceiro {
	
	public double calculaJuros(double saldo, double taxaDeJurosDiaria) {
		double novoJuros = FormatacaoDecimalDuasCasas.formataDecimalDuasCasas((saldo * taxaDeJurosDiaria) - saldo);
		return novoJuros;
	}

	// Função que abate o valor pago do juros e retorna o restante do pagamento;
	public  double abaterOPagamentoDoJuros(double juros, double valorDoPagamento) {
		if (valorDoPagamento >= juros) {
			double restanteSobreOJuros = valorDoPagamento - juros;
			return restanteSobreOJuros;
		} else {
			return 0;
		}
	}

	// Função que atualiza o atributo saldo. Retorna o restante do saldo;
	public  double abaterDoSaldo(double saldo, double valorDoPagamento) {
		double novoSaldo;
		if (valorDoPagamento == saldo) {
			novoSaldo = 0;
			return novoSaldo;
		} else {
			novoSaldo = saldo - valorDoPagamento;
			return novoSaldo;
		}
	}
	
	
	// função que atualiza a dívida de acordo com os dados do pagamento da parcela
	public void atualizaDivida(Divida divida, PagamentoDeParcela pagamentoDeParcela) {
		LocalDate dataDoPagamentoDaParcela = pagamentoDeParcela.getParcela().getDataDoPagamento();
		LocalDate dataDaDivida = divida.getDataDeAquisicaoDaDivida();// data de aquisição da divida
		double taxaDeJuros = divida.getTaxaDeJuros();// taxa de juros anual da divida
		double jurosDiario = divida.getTaxaDejurosDiario().calculaTaxaDeJurosDiario(taxaDeJuros, dataDaDivida,
				dataDoPagamentoDaParcela);// taxa de juros diaria da divida
		double valorDoPagamento = pagamentoDeParcela.getParcela().getValorDoPagamento();
		double saldoAtual = divida.getSaldo();
		double novoJuros = calculaJuros(saldoAtual, jurosDiario);

		// atualiza o valor do juros
		divida.setJuros(novoJuros);

		if (valorDoPagamento >= novoJuros) {
			divida.setJuros(0);
			divida.setTotalDeJurosPago(divida.getTotalDeJurosPago() + novoJuros);
			valorDoPagamento = abaterOPagamentoDoJuros(novoJuros, valorDoPagamento);
			// do saldo a divida
			if (valorDoPagamento == saldoAtual) {
				divida.setSaldo(0);
				divida.setQuitado(true);
			} else {
				divida.setSaldo(abaterDoSaldo(saldoAtual, valorDoPagamento));

			}

		} else {
			double jurosAtualizado = novoJuros - valorDoPagamento;
			divida.setJuros(0);
			divida.setSaldo(saldoAtual + jurosAtualizado);
			divida.setTotalDeJurosPago(divida.getTotalDeJurosPago() + valorDoPagamento);
		}
	}
	
	// dado uma divida e a data do pagamento calcula o valor da nova divida Obs: o
	// valor é calculado a partir do ultimo estado da dívida;
	public double calculaValorDaDivida(Divida divida, LocalDate dataDoPagamento) {
		double taxaDeJurosDiario = divida.getTaxaDejurosDiario().calculaTaxaDeJurosDiario(divida.getTaxaDeJuros(),
				divida.getDataDeAquisicaoDaDivida(), dataDoPagamento);
		double novoJuros = calculaJuros(divida.getSaldo(), taxaDeJurosDiario);
		return divida.getSaldo() + novoJuros;
	}
	
	

	// devolve o o Saldo atualizado entre o momento atual da dívida e a data do
	// pagamento
	public  double atualizaSaldoParaAData(Divida divida, double saldoParaAData, PagamentoDeParcela pagamento) {
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
			}
		} else {
			double jurosAtualizado = novoJuros - valorDoPagamento;

			saldoAtualizadoParaData = saldoParaAData + jurosAtualizado;

		}
		return saldoAtualizadoParaData;
	}

	// função que devolve o valor do juros dado uma data de pesquisa
	public  double atualizaOJurosParaAData(Divida divida, double saldoParaAData, LocalDate dataDePesquisa) {

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
	public  double devolveJurosPago(Divida divida, double saldoParaAData, PagamentoDeParcela pagamento) {
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
	public  LocalDate confereDataDeQuitacao(Divida divida) {
		if (divida.getQuitado()) {
			// retorna a data da ultima parcela se estiver quitado
			if (!divida.getConjuntoDePagamento().isEmpty()) {
				int ultimoIndice = divida.getConjuntoDePagamento().size() - 1;
				return divida.getConjuntoDePagamento().get(ultimoIndice).getParcela().getDataDoPagamento();
			} 
		}
		return null;
	}

}
