package br.com.maps.gerenciadorFinanceiro.model.services.taxas;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import br.com.maps.gerenciadorFinanceiro.model.services.formatacao.FormatacaoDecimalOitoCasas;

public class TaxaBase252 implements TaxaDeJurosDiario {
	
	@Override
	public Double calculaTaxaDeJurosDiario(Double taxaDeJuros, LocalDate dataInicialDoAcordo, LocalDate data) {
		Double taxaAoAno = taxaDeJuros/100;
		long diasPorFinalDeSemana = ChronoUnit.DAYS.between(dataInicialDoAcordo, data)/7 * 2;//quantidade de dias por final de semana
		long intervaloDeDias = ChronoUnit.DAYS.between(dataInicialDoAcordo, data);
		long intervaloDeDiasUteis =  intervaloDeDias - diasPorFinalDeSemana;
		if(intervaloDeDiasUteis< 1) {
			throw new RuntimeException("Valor de intervalo entre as datas menor que 1 dia");
		}
		double expoente = FormatacaoDecimalOitoCasas.formataDecimalOitoCasas((double)intervaloDeDiasUteis/252);
		Double taxaDeJurosDiaria = FormatacaoDecimalOitoCasas.formataDecimalOitoCasas(Math.pow((1.0+taxaAoAno), expoente));
		return taxaDeJurosDiaria;
	}

	

	
	
}
