package br.ce.waquino.exceptions;

public class FilmeSemEstoqueException extends Exception {

	private static final long serialVersionUID = -6486007797930958034L;

	// Esta classe foi criada para garantir o uso seguro da forma elegante, ou seja, que a exceção está vindo
	// apenas por 1 motivo!
	
	// Na prática isso não é o que acontece. Normalmente é criada uma exceção genérica ex. LocadoraException (um pouco menos genérica do que a Exception)
	// e a mesma é invocada passando uma string com a mensagem de erro
}
