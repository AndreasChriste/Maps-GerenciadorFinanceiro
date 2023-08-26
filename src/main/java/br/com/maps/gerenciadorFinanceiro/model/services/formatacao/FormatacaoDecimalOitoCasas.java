package br.com.maps.gerenciadorFinanceiro.model.services.formatacao;


import java.math.BigDecimal;
import java.math.RoundingMode;

public class FormatacaoDecimalOitoCasas {
    public static double formataDecimalOitoCasas(double valor) {
    	
        BigDecimal bigDecimal = new BigDecimal(valor);
        BigDecimal valorFormatado = bigDecimal.setScale(8, RoundingMode.DOWN);

        double valorFormatadoDouble = valorFormatado.doubleValue();

        return valorFormatadoDouble;
    }
}
