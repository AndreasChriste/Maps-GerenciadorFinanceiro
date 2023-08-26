package br.com.maps.gerenciadorFinanceiro.model.services.inclusaoDeParcelas;

import br.com.maps.gerenciadorFinanceiro.model.services.formatacao.FormatacaoDecimalDuasCasas;

public class ServiceInclusaoDeParcelas {
	public static double atualizaJuros(double saldo ,double taxaDeJurosDiaria) {
		double novoJuros = FormatacaoDecimalDuasCasas.formataDecimalDuasCasas((saldo * taxaDeJurosDiaria) - saldo);
		return novoJuros;
		
	}
	
	
	//Função que atualiza o atributo juros. Abate o o valor pago do juros e retorna o restante do pagamento;
	public static double abaterOPagamentoDoJuros(double juros, double valorDoPagamento) {

		if(valorDoPagamento >= juros) {
			double restanteSobreOJuros = valorDoPagamento - juros;
			return restanteSobreOJuros;
		}else {
			return 0;
		}
	}
	
	//Função que atualiza o atributo saldo. Retorna o restante do saldo;
	public static double abaterDoSaldo(double saldo, double valorDoPagamento) {
		double novoSaldo;
		if(valorDoPagamento == saldo) {
			novoSaldo = 0;
			return novoSaldo;
		}else {
			novoSaldo = saldo - valorDoPagamento;
			return novoSaldo;	
		}
	}
	
}
