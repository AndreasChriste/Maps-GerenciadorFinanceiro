package br.com.maps.gerenciadorFinanceiro.validaApis.gerenciadorFinanceiro;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import br.com.maps.gerenciadorFinanceiro.model.apis.GerenciadorFinanceiro;
import br.com.maps.gerenciadorFinanceiro.model.entities.VisaoDaDivida;

class ValidaConsultaDeDividas {

	private static GerenciadorFinanceiro gerenciadorFinanceiro;
	private static List<VisaoDaDivida> listaVisaoDaDivida;

	@BeforeAll
	// inicializa as instancias para os testes
	public static void inicializa() {
		// Cria uma instancia de Inclusao de dividas e inclui dividas
		gerenciadorFinanceiro = new GerenciadorFinanceiro();
		gerenciadorFinanceiro.adicionarDivida(100.20, 10, LocalDate.of(2000, 1, 2));
		gerenciadorFinanceiro.adicionarDivida(1000.00, 10, LocalDate.of(2000, 1, 12));
		gerenciadorFinanceiro.adicionarDivida(100000.00, 10, LocalDate.of(2000, 1, 12));

		// adiciona parcelas para as dividas
		gerenciadorFinanceiro.adicionarPagamentoDeParcela(0, 50.09, LocalDate.of(2000, 1, 3));
//		gerenciadorFinanceiro.adicionarPagamentoDeParcela(1, 1.00, LocalDate.of(2000, 1, 23));
		gerenciadorFinanceiro.adicionarPagamentoDeParcela(0, 10.00, LocalDate.of(2000, 1, 5));
		gerenciadorFinanceiro.adicionarPagamentoDeParcela(0, 40.32, LocalDate.of(2000, 1, 13));
		gerenciadorFinanceiro.adicionarPagamentoDeParcela(2, 100037.82, LocalDate.of(2000, 1, 13));
		listaVisaoDaDivida = gerenciadorFinanceiro.listaVisaoDaDivida(LocalDate.of(2000, 1, 13));
	}

	@Test
	void validaListaVisaoDaDivida() {
		Assertions.assertNotNull(listaVisaoDaDivida);
		}

		@Test
		void validaListaVisaoDaDivida_2() {
			Assertions.assertTrue(listaVisaoDaDivida instanceof List );
		}
		
		@Test
		void validaListaVisaoDaDivida_3() {
			if (!listaVisaoDaDivida.isEmpty()) {
				Assertions.assertTrue(listaVisaoDaDivida.get(0) instanceof VisaoDaDivida);
			}

		}
}
