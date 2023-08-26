package br.com.maps.gerenciadorFinanceiro.testeApis;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import br.com.maps.gerenciadorFinanceiro.exceptions.DataInvalidaException;
import br.com.maps.gerenciadorFinanceiro.exceptions.PagamentoMaiorQueDevidoException;
import br.com.maps.gerenciadorFinanceiro.exceptions.PagamentoParcelaPosteriorException;
import br.com.maps.gerenciadorFinanceiro.exceptions.ValorPagoInvalidoException;
import br.com.maps.gerenciadorFinanceiro.model.apis.GerenciadorFinanceiro;

class ValidaInclusaoDeParcelasPagas {

	private static GerenciadorFinanceiro gerenciadorFinanceiro;

	@BeforeAll
	public static void inicializa() {
		gerenciadorFinanceiro = new GerenciadorFinanceiro();
		// Cria uma instância de InclusaoDeDividas que contém uma lista de dividas
		gerenciadorFinanceiro.adicionarDivida(100.20, 10, LocalDate.of(2000, 1, 1));
		gerenciadorFinanceiro.adicionarDivida(1000.00, 10, LocalDate.of(2000, 1, 12));
		gerenciadorFinanceiro.adicionarDivida(100000.00, 10, LocalDate.of(2000, 1, 12));

	}

	@Test
	public void validaAdicionarPagamentoDeParcela() {
		// adicionando uma parcela paga referente a divida 0
		Assertions.assertEquals(true,
				gerenciadorFinanceiro.adicionarPagamentoDeParcela(0, 50.09, LocalDate.of(2000, 1, 2)));
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

//	@Test
//	public void validaAdicionarPagamentoDeParcelaDataIgual() {
//		// adicionando parcela com duas datas iguais para a mesma divida;
//		Assertions.assertThrows(PagamentoDuploException.class, () -> {
//			inclusaoDeParcelasPagas.adicionarPagamentoDeParcela(0, 50.09, LocalDate.of(2000, 1, 2));
//		});
//	}

	@Test
	public void validaAdicionarPagamentoDeParcela_n() {

		// adicionando parcela com duas datas iguais para a mesma divida;
		Assertions.assertThrows(PagamentoParcelaPosteriorException.class, () -> {
			gerenciadorFinanceiro.adicionarPagamentoDeParcela(0, 50.09, LocalDate.of(2000, 1, 1));
		});
		Assertions.assertThrows(PagamentoParcelaPosteriorException.class, () -> {
			gerenciadorFinanceiro.adicionarPagamentoDeParcela(1, 50.09, LocalDate.of(2000, 1, 22));
		});

		// adicionando parcela com duas datas iguais para a mesma divida;
		Assertions.assertThrows(PagamentoMaiorQueDevidoException.class, () -> {
			gerenciadorFinanceiro.adicionarPagamentoDeParcela(0, 120.99, LocalDate.of(2000, 1, 10));
		});

		// inclusao de pagamento que zera a divida
//			inclusaoDeParcelasPagas.adicionarPagamentoDeParcela(2, 0, LocalDate.of(2000, 1, 13));

		// causa erro arrumar!!!!
		// inclusaoDeParcelasPagas.adicionarPagamentoDeParcela(2, 100037.82,
		// LocalDate.of(2000, 1, 13));

	}

}
