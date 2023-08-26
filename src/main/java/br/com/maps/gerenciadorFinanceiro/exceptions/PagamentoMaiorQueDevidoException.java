package br.com.maps.gerenciadorFinanceiro.exceptions;

public class PagamentoMaiorQueDevidoException extends GerenciadorFinanceiroExceptions {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PagamentoMaiorQueDevidoException(String message) {
        super(message);
    }
}