package br.ce.wcaquino.builder;

import br.ce.wcaquino.entidades.Filme;

public class FilmeBuilder {

	
	private Filme filme;
	
	private FilmeBuilder() {}
	
	public static FilmeBuilder umFilme() {
		FilmeBuilder builder = new FilmeBuilder();
		builder.filme = new Filme();
		builder.filme.setEstoque(2);
		builder.filme.setNome("Filme 1");
		builder.filme.setPrecoLocacao(5.0);
		return builder;
	}
	
	// Aqui não precisei de static pois, apesar de ser um método de instancia, que permite o CHAIN METHOD 
	//ele SÓ PODERÁ ser chamado após o umFilme()
	public FilmeBuilder semEstoque() {
		filme.setEstoque(0);
		return this; // como eu estou em um método de instância (FilmeBuilder) retorno a própria instancia
	}
	
	public Filme agora() {
		return filme;
	}
}
