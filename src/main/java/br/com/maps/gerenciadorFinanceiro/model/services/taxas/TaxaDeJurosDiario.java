package br.com.maps.gerenciadorFinanceiro.model.services.taxas;


import java.time.LocalDate;

//Interface que aplica a regra de negócio para o cálculo da taxa de juros diário.
public interface TaxaDeJurosDiario {
	
	//função que calcula a taxa de juros diário em função da taxa de juros Anual e da data (intervalo de tempo entre a data da dívida e a data passada) .
	Double calculaTaxaDeJurosDiario(Double taxaDeJuros,LocalDate dataInicialDoAcordo ,LocalDate data);
}
