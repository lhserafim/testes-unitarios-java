package br.ce.wcaquino.servicos;


import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	//Deixar o método abaixo como método de teste do JUnit
	//public static void main(String[] args) {
	
	// 1. Colocar um nome qualquer no método
	// 2. Colocar a anotação @Test (do org.junit)
	// 3. Rodar como JUnit test
	@Test
	public void teste() {
		//cenario
		//no cenário, eu vou inicializar tudo o que eu preciso, ou seja vou instanciar as classes: LocacaoService, Usuario e Filme
		
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0); // Instanciei o valor de 5.0, que é o valor que vou validar no meu teste
		
		//acao
		//é onde eu farei a execução do método que eu quero testar
		Locacao locacao = service.alugarFilme(usuario, filme);
		
		//verificacao
		//para garantir a verificação (as assertivas, substituir o System.out... pela assertiva
		//importar a classe Assert do org.junit
		//System.out.println(locacao.getValor() == 5.0); // Coloquei o 5, pois é o valor que eu espero que seja retornado pelo método getValor
		//Assert.assertTrue(locacao.getValor() == 5.0); // Coloquei o 5, pois é o valor que eu espero que seja retornado pelo método getValor
		//Melhorei a validação acima, troquei p/ assertEquals que é mais correto. A ordem dos parametros foi colocada de acordo
		//que o cenário (5.0) seja validado contra o valor obtido
		Assert.assertEquals(5.0, locacao.getValor(), 0.01); // usei a tolerancia de arredondamento.
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}
}
