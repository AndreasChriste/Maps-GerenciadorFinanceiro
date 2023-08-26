package br.com.maps.gerenciadorFinanceiro.model.apis;

import static br.com.maps.gerenciadorFinanceiro.model.services.inclusaoDeParcelas.ServiceInclusaoDeParcelas.abaterDoSaldo;
import static br.com.maps.gerenciadorFinanceiro.model.services.inclusaoDeParcelas.ServiceInclusaoDeParcelas.abaterOPagamentoDoJuros;
import static br.com.maps.gerenciadorFinanceiro.model.services.inclusaoDeParcelas.ServiceInclusaoDeParcelas.atualizaJuros;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import br.com.maps.gerenciadorFinanceiro.exceptions.DataInvalidaException;
import br.com.maps.gerenciadorFinanceiro.exceptions.OperacaoInvalidaException;
import br.com.maps.gerenciadorFinanceiro.exceptions.PagamentoDuploException;
import br.com.maps.gerenciadorFinanceiro.exceptions.PagamentoMaiorQueDevidoException;
import br.com.maps.gerenciadorFinanceiro.exceptions.PagamentoParcelaPosteriorException;
import br.com.maps.gerenciadorFinanceiro.exceptions.ValorPagoInvalidoException;
import br.com.maps.gerenciadorFinanceiro.model.entities.Divida;
import br.com.maps.gerenciadorFinanceiro.model.entities.PagamentoDeParcela;
import br.com.maps.gerenciadorFinanceiro.model.entities.VisaoDaDivida;

public class GerenciadorFinanceiro {
	private long proximoIdDivida;
	private long proximoIDPagamento;
	private List<Divida> listaDeDividas;
	private List<PagamentoDeParcela> listaDePagamentosDeParcelas;

	public GerenciadorFinanceiro() {
		super();
		this.proximoIdDivida = 0;
		this.proximoIDPagamento = 0;
		this.listaDeDividas = new ArrayList<>();
		this.listaDePagamentosDeParcelas = new ArrayList<>();
	}

	/* Instruções relacionadas à inclusão de dívidas */
	public boolean adicionarDivida(double principal, double taxaDeJuros, LocalDate dataDeAquisicaoDaDivida) {
		//
		String principalCasasDecimais = String.valueOf(principal).split("\\.")[1];
		if (principal >= 0 && (principalCasasDecimais.length() <= 2)) {
			listaDeDividas.add(new Divida(proximoIdDivida, principal, taxaDeJuros, dataDeAquisicaoDaDivida));
			proximoIdDivida++;
			return true;
		} else {
			throw new RuntimeException("Valor principal:" + principal + "inválido");
		}
	}

	// dado um id referente à uma divida retorna a divida correspondente
	public Divida getDivida(long id_divida) {
		if (!listaDeDividas.isEmpty()) {
			Divida divida = listaDeDividas.get((int) id_divida);
			if (divida != null) {
				return divida;
			} else
				throw new OperacaoInvalidaException("Operacao Invalida");
		} else {
			throw new RuntimeException("Lista de Dívidas vazia!;");
		}
	}
	/* **************************************************** */

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*  instruções relacioanda à inclusao de Pagamentos e por consequencia atualizar a divida   */
	
	// Classe responsável por adicionar um pagamento de uma parcela à lista depagamentos de parcelas;
	public boolean adicionarPagamentoDeParcela(long id_divida, double valorDoPagamento, LocalDate dataDoPagamento) {
			String valorDoPagamentoCasasDecimais = Double.toString(valorDoPagamento).split("\\.")[1];
			Divida divida = this.getDivida(id_divida);
			PagamentoDeParcela pagamentoDeParcela;

			if (valorDoPagamento < 0 || valorDoPagamentoCasasDecimais.length() > 2) {
				throw new ValorPagoInvalidoException("O valor pago não pode ser negativo, nem ter frações de centavos");
			}

			if (dataDoPagamento.isBefore(divida.getDataDeAquisicaoDaDivida()) || dataDoPagamento == null) {
				throw new DataInvalidaException("A data de pagamento deve ser posterior ao início da dívida");
			}

			for (PagamentoDeParcela pagamento : divida.getConjuntoDePagamento()) {
				if (pagamento.getParcela().getDataDoPagamento().equals(dataDoPagamento)
						&& divida.getId() == pagamento.getId_divida()) {
					throw new PagamentoDuploException(
							"Não podem haver 2 pagamentos de parcela para a mesma dívida no mesmo dia.");
				}

				if (pagamento.getParcela().getDataDoPagamento().isAfter(dataDoPagamento)) {
					throw new PagamentoParcelaPosteriorException(valorDoPagamentoCasasDecimais);
				}
			}
//			calculaValorDaDivida(divida.getSaldo(),divida.getTaxaDeJuros(),dataDoPagamento);
			if (valorDoPagamento > divida.getJuros() + divida.getSaldo()) {
				throw new PagamentoMaiorQueDevidoException(
						"Não pode-se pagar mais do que os juros e o saldo da dívida somados.");
			}
			pagamentoDeParcela = new PagamentoDeParcela(proximoIDPagamento, id_divida, valorDoPagamento,
					dataDoPagamento);
			listaDePagamentosDeParcelas.add(pagamentoDeParcela);
			proximoIDPagamento++;
			atualizaDivida(divida, pagamentoDeParcela);
			divida.getConjuntoDePagamento().add(pagamentoDeParcela);
			return true;
	}
	
	
	

	// função que atualiza a dívida de acordo com os dados do pagamento da parcela
	private void atualizaDivida(Divida divida, PagamentoDeParcela pagamentoDeParcela) {
		LocalDate dataDoPagamentoDaParcela = pagamentoDeParcela.getParcela().getDataDoPagamento();
		LocalDate dataDaDivida = divida.getDataDeAquisicaoDaDivida();// data de aquisição da divida
		double taxaDeJuros = divida.getTaxaDeJuros();// taxa de juros anual da divida
		double jurosDiario = divida.getTaxaDejurosDiario().calculaTaxaDeJurosDiario(taxaDeJuros, dataDaDivida,
				dataDoPagamentoDaParcela);// taxa de juros diaria da divida
		double valorDoPagamento = pagamentoDeParcela.getParcela().getValorDoPagamento();
		double saldoAtual = divida.getSaldo();
		double novoJuros = atualizaJuros(saldoAtual, jurosDiario);

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
				;
			}
		} else {
			double jurosAtualizado = novoJuros - valorDoPagamento;
			divida.setJuros(0);
			divida.setSaldo(saldoAtual + jurosAtualizado);
			divida.setTotalDeJurosPago(divida.getTotalDeJurosPago() + valorDoPagamento);
		}
	}

	/*  *********************************************  */
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public List<VisaoDaDivida> listaVisaoDaDivida(LocalDate dataDePesquisa) {
		List<VisaoDaDivida> listaDeVisaoDasDividas = new ArrayList<>();// lista Utilizada para armazenar a visão de cada
																		// divida

		for (Divida divida : listaDeDividas) {
			if (!divida.getDataDeAquisicaoDaDivida().isAfter(dataDePesquisa)) {
				double saldoParaAData = divida.getPrincipal();
				double valorDoJurosParaAData;
				double totalDeJurosPagoParaAteAData;
				PagamentoDeParcela ultimoPagamentoAteDataPesquisada = null; // ultimo pagamento reliazadoaté a data
																			// pesquisada
				for (PagamentoDeParcela pagamento : divida.getConjuntoDePagamento()) {
					// chama Saldo para data
					// se a data do ultimo pagamento não é posterior à data de pesquisa
					if (!pagamento.getParcela().getDataDoPagamento().isAfter(dataDePesquisa)) {
						saldoParaAData = atualizaSaldoParaAData(divida, saldoParaAData, pagamento);
					}
					ultimoPagamentoAteDataPesquisada = pagamento;
					// chama total de juros pagos em todas as parcelas até a data
				}
				// chama valor de juros para data
				if (ultimoPagamentoAteDataPesquisada.getParcela().getDataDoPagamento().equals(dataDePesquisa)) {
					valorDoJurosParaAData = 0;
				} else {
					valorDoJurosParaAData = atualizaOJurosParaAData(divida, saldoParaAData, dataDePesquisa);
				}

				// chama data de quitação
				LocalDate dataDaQuitacao = confereDataDeQuitacao(divida);
				VisaoDaDivida visaoDaDivida = new VisaoDaDivida(divida.getId(), divida.getPrincipal(),
						divida.getDataDeAquisicaoDaDivida(), saldoParaAData, valorDoJurosParaAData, 0, dataDaQuitacao);
				listaDeVisaoDasDividas.add(visaoDaDivida);
			}
		}
		return listaDeVisaoDasDividas;

	}

	private LocalDate confereDataDeQuitacao(Divida divida) {
		if (divida.getQuitado()) {
			if (!divida.getConjuntoDePagamento().isEmpty()) {
				int ultimoIndice = divida.getConjuntoDePagamento().size();
				divida.getConjuntoDePagamento().get(ultimoIndice);
			} else {
				throw new RuntimeException("conjunto de pagamentos vazio");
			}
		}
		return null;
	}

	private double atualizaSaldoParaAData(Divida divida, double saldoParaAData, PagamentoDeParcela pagamento) {
		LocalDate dataDoPagamentoDaParcela = pagamento.getParcela().getDataDoPagamento();
		LocalDate dataDaDivida = divida.getDataDeAquisicaoDaDivida();// data de aquisição da divida
		double taxaDeJuros = divida.getTaxaDeJuros();// taxa de juros anual da divida
		double jurosDiario = divida.getTaxaDejurosDiario().calculaTaxaDeJurosDiario(taxaDeJuros, dataDaDivida,
				dataDoPagamentoDaParcela);
		double novoJuros = atualizaJuros(saldoParaAData, jurosDiario);
		double valorDoPagamento = pagamento.getParcela().getValorDoPagamento();
		double saldoAtualizadoParaData;

		if (valorDoPagamento >= novoJuros) {
			valorDoPagamento = abaterOPagamentoDoJuros(novoJuros, valorDoPagamento);

			if (valorDoPagamento == saldoParaAData) {
				saldoAtualizadoParaData = atualizaJuros(saldoParaAData, jurosDiario);
			} else {
				saldoAtualizadoParaData = (abaterDoSaldo(saldoParaAData, valorDoPagamento));
				;
			}
		} else {
			double jurosAtualizado = novoJuros - valorDoPagamento;

			saldoAtualizadoParaData = saldoParaAData + jurosAtualizado;
			// divida.getTotalDeJurosPago()+valorDoPagamento);
		}
		return saldoAtualizadoParaData;
	}

	private double atualizaOJurosParaAData(Divida divida, double saldoParaAData, LocalDate dataDePesquisa) {
		LocalDate dataDaDivida = divida.getDataDeAquisicaoDaDivida();// data de aquisição da divida
		double taxaDeJuros = divida.getTaxaDeJuros();// taxa de juros anual da divida
		double jurosDiario = divida.getTaxaDejurosDiario().calculaTaxaDeJurosDiario(taxaDeJuros, dataDaDivida,
				dataDePesquisa);
		double novoJuros = atualizaJuros(saldoParaAData, jurosDiario);
		return novoJuros;
	}

}
