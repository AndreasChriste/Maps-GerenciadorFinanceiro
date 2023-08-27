package br.com.maps.gerenciadorFinanceiro.services.validaServiceGerenciadorFinanceiro;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import br.com.maps.gerenciadorFinanceiro.exceptions.OperacaoInvalidaException;
import br.com.maps.gerenciadorFinanceiro.model.entities.Divida;
import br.com.maps.gerenciadorFinanceiro.model.entities.PagamentoDeParcela;
import br.com.maps.gerenciadorFinanceiro.model.services.gerenciadorFinanceiro.ServiceGerenciadorFinanceiro;

class ValidaServiceGerenciadorFinanceiro {
	private static ServiceGerenciadorFinanceiro service;
	private static List<Divida> listaDeDividas;
	//dividas
	private static Divida divida;
	private static Divida divida_2;
	//pagamentos
	private static PagamentoDeParcela pagamento;
	private static PagamentoDeParcela pagamento_2;
	private static PagamentoDeParcela pagamento_3;
	private static PagamentoDeParcela pagamento_4;
	//informações extras
	private static double saldo;
	private static double saldoParaData;
	private static double taxaDeJurosDiaria;
	private static LocalDate dataDePesquisa;
	private static double juros;
	private static double juros_2;

	@BeforeAll
	// inicializa os valores que serão utlizados nos testes
	public static void inicializa() {
		listaDeDividas = new ArrayList<Divida>();
		service = new ServiceGerenciadorFinanceiro();
		divida = new Divida(0, 100.20, 10, LocalDate.of(2000, 1, 2));
		divida_2 = new Divida(1, 100000.00, 10, LocalDate.of(2000, 1, 2));
		divida_2.setQuitado(true);
		pagamento = new PagamentoDeParcela(0, 0, 50.09, LocalDate.of(2000, 1, 3));
		pagamento_2 = new PagamentoDeParcela(0, 0, 30.00, LocalDate.of(2000, 1, 3));
		pagamento_3 = new PagamentoDeParcela(0, 0, 100.2, LocalDate.of(2000, 1, 3));
		pagamento_4 = new PagamentoDeParcela(0, 0, 100.23, LocalDate.of(2000, 1, 3));
		divida_2.getConjuntoDePagamento().add(pagamento_3);
		saldo = 100.20;
		taxaDeJurosDiaria = 1.00037828;
		saldoParaData = 100000.00;
		juros = 10.00;
		juros_2 = 100.20;
		dataDePesquisa = LocalDate.of(2000, 2, 26);

	}

	@Test
	public void validaGetDivida() {
		listaDeDividas.add(divida);
		Assertions.assertThrows(OperacaoInvalidaException.class, () -> service.getDivida(listaDeDividas, 1));
	}

	@Test
	public void validaGetDivida_2() {
		listaDeDividas.add(divida);
		Divida DividaPeloIndice = service.getDivida(listaDeDividas,0);
		Assertions.assertTrue(DividaPeloIndice instanceof Divida);
	}

	@Test
	public void validaCalculaJuros() {
		Assertions.assertEquals(0.03, service.calculaJuros(saldo, taxaDeJurosDiaria));
	}

	@Test
	public void validaAbaterDoSaldo() {
		Assertions.assertEquals(0.00, service.abaterDoSaldo(saldo, pagamento_3.getParcela().getValorDoPagamento()));
	}

	@Test
	public void validaAbaterDoSaldo_2() {
		Assertions.assertEquals(50.11, service.abaterDoSaldo(saldo, pagamento.getParcela().getValorDoPagamento()));
	}

	@Test
	public void validaAbaterOPagamentoDoJuros() {
		Assertions.assertEquals(40.09,
				service.abaterOPagamentoDoJuros(juros, pagamento.getParcela().getValorDoPagamento()));
	}

	@Test
	public void validaAbaterOPagamentoDoJuros_2() {
		Assertions.assertEquals(0,
				service.abaterOPagamentoDoJuros(juros_2, pagamento.getParcela().getValorDoPagamento()));
	}

	@Test
	public void validaAtualizaSaldoParaAData() {
		Assertions.assertEquals(50.14, service.atualizaSaldoParaAData(divida, saldo, pagamento));
	}
	@Test
	public void validaAtualizaSaldoParaAData_2() {
		Assertions.assertEquals(0, service.atualizaSaldoParaAData(divida, saldo, pagamento_4));
	}

	@Test
	public void validaAtualizaOJurosParaAData() {
		Assertions.assertEquals(1.56, service.atualizaOJurosParaAData(divida, saldo, dataDePesquisa));
	}

	@Test
	void validaDevolveJurosPago() {
		Assertions.assertEquals(0.03, service.devolveJurosPago(divida, saldo, pagamento));
	}

	@Test
	void validaDevolveJurosPago_2() {
		Assertions.assertEquals(30.00, service.devolveJurosPago(divida_2, saldoParaData, pagamento_2));
	}

	@Test
	public void validaConfereDataDeQuitacao() {
		Assertions.assertEquals(null, service.confereDataDeQuitacao(divida));
	}

	@Test
	public void validaConfereDataDeQuitacao_2() {
		Assertions.assertEquals(LocalDate.of(2000, 01, 03), service.confereDataDeQuitacao(divida_2));
	}
}
