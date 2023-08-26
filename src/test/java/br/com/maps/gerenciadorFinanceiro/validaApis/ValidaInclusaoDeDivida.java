package br.com.maps.gerenciadorFinanceiro.validaApis;


import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.com.maps.gerenciadorFinanceiro.model.apis.GerenciadorFinanceiro;

class ValidaInclusaoDeDivida {
	//inicializa as intancias de dívidas
	GerenciadorFinanceiro gerenciadorFinanceiro = new GerenciadorFinanceiro();
	
	
	@Test
	public void validaAdicionarDivida(){
		//Cria uma instancia de Inclusao de dividas que contém uma lista de dividas
		//primeira inserção
		Assertions.assertEquals(true,gerenciadorFinanceiro.adicionarDivida(100.20, 10, LocalDate.of(2000, 1, 1)));
	}	
	@Test
	public void validaAdicionarDivida_2(){
		//segunda insercao
		Assertions.assertEquals(true,gerenciadorFinanceiro.adicionarDivida(2000.99, 10, LocalDate.of(2000, 1, 1)));
	}
	@Test
	public void validaAdicionarDivida_3(){
		//terceira inserção
		Assertions.assertEquals(true,gerenciadorFinanceiro.adicionarDivida(100.2000, 10, LocalDate.of(2000, 1, 1)));
	}
	@Test
	public void validaAdicionarDivida_4(){
		//quarta inserção
		Assertions.assertEquals(true,gerenciadorFinanceiro.adicionarDivida(100.99, 10, LocalDate.of(2000, 1, 1)));
	}
	@Test
	public void validaAdicionarDividaException(){
		//quinta inserção lança erro -> nesse caso não há a inserção na lista
		//valor da dívida contém frações de centavos
		Assertions.assertThrows(RuntimeException.class, () -> {
			gerenciadorFinanceiro.adicionarDivida(2000.947, 10, LocalDate.of(2000, 1, 1));
        });
	}
	
	@Test
	public void validaAdicionarDividaException_2() {
		//sexta Inserçao lança erro
		Assertions.assertThrows(RuntimeException.class, () -> {
			gerenciadorFinanceiro.adicionarDivida(-0.01, 10, LocalDate.of(2000, 1, 1));
        });
	}

	@Test
	public void validaAdicionarDivida_5(){
		//primeira insersao na lista de dividas 2
		Assertions.assertEquals(true,gerenciadorFinanceiro.adicionarDivida(100.2000, 10, LocalDate.of(2000, 1, 1)));
	}
		
	@Test
	public void validaAdicionarDivida_6(){
		//segunda insersao na lista de dividas 2
		Assertions.assertEquals(true,gerenciadorFinanceiro.adicionarDivida(100.2000, 10, LocalDate.of(2000, 1, 1)));
	}
	@Test
	public void validaAdicionarDivida_7(){
		Assertions.assertEquals(true,gerenciadorFinanceiro.adicionarDivida(0.0, 10, LocalDate.of(2000, 1, 1)));
	}
		
	@Test
	public void validaAdicionarDividaException_3() {
		//terceira insersao na lista de dividas 2
		Assertions.assertThrows(RuntimeException.class, () -> {
			gerenciadorFinanceiro.adicionarDivida(0.009, 10, LocalDate.of(2000, 1, 1));
        });
	}
	
	@Test
	public void validaAdicionarDividaGeral() {
		//verifica diversas inserções para prever algum erro fora do escopo
		Assertions.assertEquals(true,gerenciadorFinanceiro.adicionarDivida(100.20, 10, LocalDate.of(2000, 1, 1)));
		Assertions.assertEquals(true,gerenciadorFinanceiro.adicionarDivida(2000.99, 10, LocalDate.of(2000, 1, 1)));
		Assertions.assertEquals(true,gerenciadorFinanceiro.adicionarDivida(100.2000, 10, LocalDate.of(2000, 1, 1)));
		Assertions.assertEquals(true,gerenciadorFinanceiro.adicionarDivida(100.99, 10, LocalDate.of(2000, 1, 1)));
		Assertions.assertThrows(RuntimeException.class, () -> {gerenciadorFinanceiro.adicionarDivida(0.009, 10, LocalDate.of(2000, 1, 1));});
		Assertions.assertEquals(true,gerenciadorFinanceiro.adicionarDivida(0.0, 10, LocalDate.of(2000, 1, 1)));
		Assertions.assertThrows(RuntimeException.class, () -> {gerenciadorFinanceiro.adicionarDivida(0.009, 10, LocalDate.of(2000, 1, 1));});
	}
	

}
