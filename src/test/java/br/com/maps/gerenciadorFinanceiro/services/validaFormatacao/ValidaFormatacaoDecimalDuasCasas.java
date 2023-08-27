package br.com.maps.gerenciadorFinanceiro.services.validaFormatacao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import br.com.maps.gerenciadorFinanceiro.model.services.formatacao.FormatacaoDecimalDuasCasas;

class ValidaFormatacaoDecimalDuasCasas {

	@Test
	public void validaFormatacaoDecimalDuasCasas() {
		double valor = 15.67899;
		double valorFormatado = FormatacaoDecimalDuasCasas.formataDecimalDuasCasas(valor);
		Assertions.assertEquals(15.67, valorFormatado);
		
	}
	@Test
	public void validaFormatacaoDecimalDuasCasas_2() {
		double valor = 455.429897775;
		double valorFormatado = FormatacaoDecimalDuasCasas.formataDecimalDuasCasas(valor);
		Assertions.assertEquals(455.42, valorFormatado);
	}
	@Test
	public void validaFormatacaoDecimalDuasCasas_3() {
		double valor = 123.9999999;
		double valorFormatado = FormatacaoDecimalDuasCasas.formataDecimalDuasCasas(valor);
		Assertions.assertEquals(123.99, valorFormatado);
	}
	@Test
	public void validaFormatacaoDecimalDuasCasas_4() {
		double valor = 12.0;
		double valorFormatado = FormatacaoDecimalDuasCasas.formataDecimalDuasCasas(valor);
		Assertions.assertEquals(12.00, valorFormatado);
	}

}
