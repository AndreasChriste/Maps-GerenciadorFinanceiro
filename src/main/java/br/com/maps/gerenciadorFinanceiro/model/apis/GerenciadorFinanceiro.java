package br.com.maps.gerenciadorFinanceiro.model.apis;

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
import br.com.maps.gerenciadorFinanceiro.model.services.formatacao.FormatacaoDecimalDuasCasas;
import br.com.maps.gerenciadorFinanceiro.model.services.gerenciadorFinanceiro.ServiceGerenciadorFinanceiro;

public class GerenciadorFinanceiro {
	private long proximoIdDivida;
	private long proximoIDPagamento;
	private List<Divida> listaDeDividas;
	private List<PagamentoDeParcela> listaDePagamentosDeParcelas;
	private ServiceGerenciadorFinanceiro service;

	// construtor
	public GerenciadorFinanceiro() {
		this.service = new ServiceGerenciadorFinanceiro();
		this.proximoIdDivida = 0;
		this.proximoIDPagamento = 0;
		this.listaDeDividas = new ArrayList<>();
		this.listaDePagamentosDeParcelas = new ArrayList<>();
	}


	//Instrução responsável por adicionar uma dívida à lista de Dívida
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




	// Classe responsável por adicionar um pagamento de uma parcela à lista
	// depagamentos de parcelas;
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
			if (pagamento.getParcela().getDataDoPagamento().isEqual(dataDoPagamento)
					&& divida.getId() == pagamento.getId_divida()) {
				throw new PagamentoDuploException(
						"Não podem haver 2 pagamentos de parcela para a mesma dívida no mesmo dia.");
			}

			if (pagamento.getParcela().getDataDoPagamento().isAfter(dataDoPagamento)) {
				throw new PagamentoParcelaPosteriorException(valorDoPagamentoCasasDecimais);
			}
		}
		double dividaNaData = service.calculaValorDaDivida(divida, dataDoPagamento);
		if (valorDoPagamento > dividaNaData) {
			throw new PagamentoMaiorQueDevidoException(
					"Não pode-se pagar mais do que os juros e o saldo da dívida somados.");
		}
		pagamentoDeParcela = new PagamentoDeParcela(proximoIDPagamento, id_divida, valorDoPagamento, dataDoPagamento);
		listaDePagamentosDeParcelas.add(pagamentoDeParcela);
		proximoIDPagamento++;
		service.atualizaDivida(divida, pagamentoDeParcela);
		divida.getConjuntoDePagamento().add(pagamentoDeParcela);
		return true;
	}

	
	
	public List<VisaoDaDivida> listaVisaoDaDivida(LocalDate dataDePesquisa) {
		// lista Utilizada para armazenar a visão de cada divida
		List<VisaoDaDivida> listaDeVisaoDasDividas = new ArrayList<>();
		for (Divida divida : listaDeDividas) {
			if (!divida.getDataDeAquisicaoDaDivida().isAfter(dataDePesquisa)) {
				double saldoParaAData = divida.getPrincipal();
				double valorDoJurosParaAData;
				double totalDeJurosPagoAteAData = 0;

				// ultimo pagamento reliazado até a data pesquisada
				PagamentoDeParcela ultimoPagamentoAteDataPesquisada = null;

				for (PagamentoDeParcela pagamento : divida.getConjuntoDePagamento()) {
					// chama Saldo para data
					// se a data do ultimo pagamento não é posterior à data de pesquisa
					if (!pagamento.getParcela().getDataDoPagamento().isAfter(dataDePesquisa)) {
						totalDeJurosPagoAteAData += service.devolveJurosPago(divida, saldoParaAData, pagamento);
						saldoParaAData = service.atualizaSaldoParaAData(divida, saldoParaAData, pagamento);
					}
					ultimoPagamentoAteDataPesquisada = pagamento;
					// chama total de juros pagos em todas as parcelas até a data
				}
				// chama valor de juros para data
				if (ultimoPagamentoAteDataPesquisada != null) {
					if (ultimoPagamentoAteDataPesquisada.getParcela().getDataDoPagamento().isEqual(dataDePesquisa)) {
						valorDoJurosParaAData = 0;
					} else {
						valorDoJurosParaAData = service.atualizaOJurosParaAData(divida, saldoParaAData, dataDePesquisa);
					}
				} else {
					valorDoJurosParaAData = service.atualizaOJurosParaAData(divida, saldoParaAData, dataDePesquisa);
				}
				// chama data de quitação
				// atributos para instanciação de uma visao de uma dívida
				long id = divida.getId();
				double principal = divida.getPrincipal();
				LocalDate dataDeAquisicao = divida.getDataDeAquisicaoDaDivida();
				totalDeJurosPagoAteAData = FormatacaoDecimalDuasCasas.formataDecimalDuasCasas(totalDeJurosPagoAteAData);
				LocalDate dataDaQuitacao = service.confereDataDeQuitacao(divida);

				VisaoDaDivida visaoDaDivida = new VisaoDaDivida(id, principal, dataDeAquisicao, saldoParaAData,
						valorDoJurosParaAData, totalDeJurosPagoAteAData, dataDaQuitacao);
				listaDeVisaoDasDividas.add(visaoDaDivida);
			}
		}
		return listaDeVisaoDasDividas;

	}
	
	//função auxiliar dado um id referente à uma divida retorna a divida correspondente
	private Divida getDivida(long id_divida) {
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
	
}
