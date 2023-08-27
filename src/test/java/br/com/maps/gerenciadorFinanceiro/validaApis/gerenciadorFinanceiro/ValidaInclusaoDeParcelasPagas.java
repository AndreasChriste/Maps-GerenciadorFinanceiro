package br.com.maps.gerenciadorFinanceiro.validaApis.gerenciadorFinanceiro;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.maps.gerenciadorFinanceiro.exceptions.DataInvalidaException;
import br.com.maps.gerenciadorFinanceiro.exceptions.PagamentoDuploException;
import br.com.maps.gerenciadorFinanceiro.exceptions.PagamentoMaiorQueDevidoException;
import br.com.maps.gerenciadorFinanceiro.exceptions.PagamentoParcelaPosteriorException;
import br.com.maps.gerenciadorFinanceiro.exceptions.ValorPagoInvalidoException;
import br.com.maps.gerenciadorFinanceiro.model.apis.GerenciadorFinanceiro;

class ValidaInclusaoDeParcelasPagas {

	private static GerenciadorFinanceiro gerenciadorFinanceiro;

	@BeforeEach
	public  void inicializa() {
		gerenciadorFinanceiro = new GerenciadorFinanceiro();
		// Cria uma instância de InclusaoDeDividas que contém uma lista de dividas
		gerenciadorFinanceiro.adicionarDivida(100.20, 10, LocalDate.of(2000, 1, 2));
		gerenciadorFinanceiro.adicionarDivida(1000.00, 10, LocalDate.of(2000, 1, 12));
		gerenciadorFinanceiro.adicionarDivida(100000.00, 10, LocalDate.of(2000, 1, 12));

	}

	@Test
	public void validaAdicionarPagamentoDeParcela() {
		// adicionando uma parcela paga referente a divida 0
		Assertions.assertEquals(true,
				gerenciadorFinanceiro.adicionarPagamentoDeParcela(0, 50.09, LocalDate.of(2000, 1, 3)));
		//valida o pagamento duplicado
	}

	@Test
	public void validaAdicionarPagamentoDeParcela_2() {
		// adicionando uma parcela paga referente a divida 1
		Assertions.assertEquals(true,
				gerenciadorFinanceiro.adicionarPagamentoDeParcela(1, 1.00, LocalDate.of(2000, 1, 23)));
	}

	@Test
	public void validaAdicionarPagamentoDeParcela__3() {
		// adicionando uma parcela paga referente a divida 0
		Assertions.assertEquals(true,
				gerenciadorFinanceiro.adicionarPagamentoDeParcela(0, 10.00, LocalDate.of(2000, 1, 3)));
	}

	@Test
	public void validaAdicionarPagamentoDeParcelaValorInvalido() {
		// adicionando duas parcela com valor de pagamento invalido
		Assertions.assertThrows(ValorPagoInvalidoException.class, () -> {
			gerenciadorFinanceiro.adicionarPagamentoDeParcela(1, 50.099, LocalDate.of(2000, 1, 3));
		});
		Assertions.assertThrows(ValorPagoInvalidoException.class, () -> {
			gerenciadorFinanceiro.adicionarPagamentoDeParcela(1, -18.28, LocalDate.of(2000, 1, 3));
		});
	}

	@Test
	public void validaAdicionarPagamentoDeParcelaDataInvalida() {
		// adicionando parcela com a data antecessora à data da divida;
		// neste caso data da dívida é 2000-1-12 <yyyy-mm-dd>
		Assertions.assertThrows(DataInvalidaException.class, () -> {
			gerenciadorFinanceiro.adicionarPagamentoDeParcela(1, 50.12, LocalDate.of(2000, 1, 1));
		});
		Assertions.assertThrows(DataInvalidaException.class, () -> {
			gerenciadorFinanceiro.adicionarPagamentoDeParcela(1, 50.12, LocalDate.of(2000, 1, 8));
		});
	}
	
	@Test
	public void validaPagamentoParcelaPosteriorException() {
		// adicionando parcela com duas datas iguais para a mesma divida;
		gerenciadorFinanceiro.adicionarPagamentoDeParcela(0, 10.00, LocalDate.of(2000, 1, 5));
		Assertions.assertThrows(PagamentoParcelaPosteriorException.class, () -> {
			gerenciadorFinanceiro.adicionarPagamentoDeParcela(0, 50.09, LocalDate.of(2000, 1, 3));
		});

	}
	
	@Test
	public void validaPagamentoDuploException() {
		// adicionando parcela com duas datas iguais para a mesma divida;
		gerenciadorFinanceiro.adicionarPagamentoDeParcela(0, 10.00, LocalDate.of(2000, 1, 5));
		Assertions.assertThrows(PagamentoDuploException.class, () -> {
			gerenciadorFinanceiro.adicionarPagamentoDeParcela(0, 50.09, LocalDate.of(2000, 1, 5));
		});
	}
	
	@Test
	public void validaPagamentoMaiorQueDevidoException() {
		//testa pagamento maior que o valor da dívida
		Assertions.assertThrows(PagamentoMaiorQueDevidoException.class, () -> {
			gerenciadorFinanceiro.adicionarPagamentoDeParcela(0, 120.99, LocalDate.of(2000, 1, 10));
		});
	}
}
