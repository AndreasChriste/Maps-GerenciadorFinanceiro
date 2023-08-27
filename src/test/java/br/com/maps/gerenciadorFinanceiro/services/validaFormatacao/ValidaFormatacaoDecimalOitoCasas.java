package br.com.maps.gerenciadorFinanceiro.services.validaFormatacao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.com.maps.gerenciadorFinanceiro.model.services.formatacao.FormatacaoDecimalOitoCasas;

class ValidaFormatacaoDecimalOitoCasas {

	@Test
	public void validaFormatacaoDecimal() {
		double valor = 15.67899999999;
		double valorFormatado = FormatacaoDecimalOitoCasas.formataDecimalOitoCasas(valor);
		Assertions.assertEquals(15.67899999, valorFormatado);
	}
	@Test
	public void validaFormatacaoDecimal_2() {
		double valor = 455.429897775;
		double valorFormatado = FormatacaoDecimalOitoCasas.formataDecimalOitoCasas(valor);
		Assertions.assertEquals(455.42989777, valorFormatado);
		}
	@Test
	public void validaFormatacaoDecimal_3() {
		double valor = 123.99994444447;
		double valorFormatado = FormatacaoDecimalOitoCasas.formataDecimalOitoCasas(valor);
		Assertions.assertEquals(123.99994444, valorFormatado);
	}

}
