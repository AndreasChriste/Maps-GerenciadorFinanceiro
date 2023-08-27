package br.com.maps.gerenciadorFinanceiro.services.validaTaxas;

import java.time.LocalDate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.com.maps.gerenciadorFinanceiro.model.services.taxas.TaxaBase252;
import br.com.maps.gerenciadorFinanceiro.model.services.taxas.TaxaDeJurosDiario;

class ValidaTaxaBase252 {

	@Test
	public void validaTaxaBase252() {
		//teste 1
		TaxaDeJurosDiario taxaBase252_test1 = new TaxaBase252();
		double taxaBaseNoIntervaloDeDias = taxaBase252_test1.calculaTaxaDeJurosDiario(10.00, LocalDate.of(2000,10,12), LocalDate.of(2000,10,23));	
		Assertions.assertEquals(1.00340973, taxaBaseNoIntervaloDeDias);
		//teste 2
		TaxaDeJurosDiario taxaBase252_test2 = new TaxaBase252();
		double taxaBaseNoIntervaloDeDias_test2 = taxaBase252_test2.calculaTaxaDeJurosDiario(10.00, LocalDate.of(2000,10,10), LocalDate.of(2000,10,11));
		Assertions.assertEquals(1.00037828, taxaBaseNoIntervaloDeDias_test2);
		
		//teste 3
		TaxaDeJurosDiario taxaBase252_test3 = new TaxaBase252();
		Assertions.assertThrows(RuntimeException.class, () -> {
			taxaBase252_test3.calculaTaxaDeJurosDiario(10.0, LocalDate.of(2000,10,10), LocalDate.of(2000,10,10));
        });
		
		//teste 4
		TaxaDeJurosDiario taxaBase252_test4 = new TaxaBase252();
		Assertions.assertThrows(RuntimeException.class, () -> {
			taxaBase252_test4.calculaTaxaDeJurosDiario(10.0, LocalDate.of(2000,10,10), LocalDate.of(2000,10,9));
        });
		
	}

}
