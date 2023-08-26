package br.com.maps.gerenciadorFinanceiro.validaApis;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import br.com.maps.gerenciadorFinanceiro.model.apis.GerenciadorFinanceiro;

class ValidaConsultaDeDividas {

	@Test
	void test() {
		//Cria uma instancia de Inclusao de dividas e inclui dividas
		GerenciadorFinanceiro gerenciadorFinanceiro = new  GerenciadorFinanceiro();
		gerenciadorFinanceiro.adicionarDivida(100.20, 10, LocalDate.of(2000, 1, 2));
		gerenciadorFinanceiro.adicionarDivida(1000.00, 10, LocalDate.of(2000, 1, 12));
		gerenciadorFinanceiro.adicionarDivida(100000.00, 10, LocalDate.of(2000, 1, 12));

		//adiciona parcelas para as dividas
		gerenciadorFinanceiro.adicionarPagamentoDeParcela(0, 50.09, LocalDate.of(2000, 1, 3));
//		gerenciadorFinanceiro.adicionarPagamentoDeParcela(1, 1.00, LocalDate.of(2000, 1, 23));
		gerenciadorFinanceiro.adicionarPagamentoDeParcela(0, 10.00, LocalDate.of(2000, 1, 5));
		gerenciadorFinanceiro.adicionarPagamentoDeParcela(0,40.32 ,LocalDate.of(2000, 1, 13));
		gerenciadorFinanceiro.adicionarPagamentoDeParcela(2, 100037.82, LocalDate.of(2000, 1, 13));
		
		System.out.println(gerenciadorFinanceiro.listaVisaoDaDivida(LocalDate.of(2000, 1, 13)));
		
		
	}
	
//	@Test
//		public void testeEspecifico(){
//			GerenciadorFinanceiro gerenciadorFinanceiro = new  GerenciadorFinanceiro();
//			gerenciadorFinanceiro.adicionarDivida(100.20, 10, LocalDate.of(2000, 1, 2));
//			gerenciadorFinanceiro.adicionarDivida(1000.00, 10, LocalDate.of(2000, 1, 12));
//			gerenciadorFinanceiro.adicionarDivida(100000.00, 10, LocalDate.of(2000, 1, 12));
//
//			gerenciadorFinanceiro.adicionarPagamentoDeParcela(2, 100037.82, LocalDate.of(2000, 1, 13));
//			
//			System.out.println(gerenciadorFinanceiro.listaVisaoDaDivida(LocalDate.of(2000, 1, 13)));
//	}

}
