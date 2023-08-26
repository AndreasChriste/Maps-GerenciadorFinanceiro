package br.com.maps.gerenciadorFinanceiro.model.services.formatacao;


import java.math.BigDecimal;
import java.math.RoundingMode;

public class FormatacaoDecimalDuasCasas {
    public static double formataDecimalDuasCasas(double valor) {
    	
        BigDecimal bigDecimal = BigDecimal.valueOf(valor);
        BigDecimal valorArredondado = bigDecimal.setScale(2, RoundingMode.DOWN);

        double valorFormatadoDouble = valorArredondado.doubleValue();

        return valorFormatadoDouble;
    }
}
