package br.ce.wcaquino.servicos;


import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule // Está rule é usada p/ a forma NOVA
	public ExpectedException exception = ExpectedException.none();
	
	// Deixar o método abaixo como método de teste do JUnit
	// public static void main(String[] args) {
	
	// 1. Colocar um nome qualquer no método
	// 2. Colocar a anotação @Test (do org.junit)
	// 3. Rodar como JUnit test
	@Test
	public void testeLocacao() throws Exception {
				
		// cenario
		// no cenário, eu vou inicializar tudo o que eu preciso, ou seja vou instanciar as classes: LocacaoService, Usuario e Filme
		
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0); // Instanciei o valor de 5.0, que é o valor que vou validar no meu teste
		
		// acao
		// é onde eu farei a execução do método que eu quero testar
		
		// Em teste unitário, quando um método necessitar de Exceção EVITAR envolvê-lo em try-catch, pois este irá dificultar
		// para localizar o erro. Ao invés do try-catch LANÇAR a exceção (throws Exception)
		// Se o teste não está esperando exceção nenhuma DEIXE que o JUnit gerencie p/ vc (usando o throws Exception)
		// Já quando o teste espera alguma exceção temos 3 formas de verificar
		
		
		Locacao locacao = service.alugarFilme(usuario, filme);
		
		// Diferença entre ERRO e FALHAS:
		// FALHAS: É quando o teste não passa em alguma verificação lógica, como os Asserts por exemplo
		// ERROS: É quando algum problema durante a execução do teste IMPEDE que ele seja concuído 
		
		// verificacao
		// para garantir a verificação (as assertivas, substituir o System.out... pela assertiva
		// importar a classe Assert do org.junit
		 System.out.println(locacao.getValor() == 5.0); // Coloquei o 5, pois é o valor que eu espero que seja retornado pelo método getValor
		 Assert.assertTrue(locacao.getValor() == 5.0); // Coloquei o 5, pois é o valor que eu espero que seja retornado pelo método getValor
		// Melhorei a validação acima, troquei p/ assertEquals que é mais correto. A ordem dos parametros foi colocada de acordo
		// que o cenário (5.0) seja validado contra o valor obtido
		Assert.assertEquals(5.0, locacao.getValor(), 0.01); // usei a tolerancia de arredondamento.
		
		// Usando o assertThat (verifiqueQue). Preciso usar alguns matchers - que ja'vem com o JUnit
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(5.0))); // Neste caso o equalTo é opcional
		Assert.assertThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.not(6.0)));
		
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		
		// Alterando p/ assertThat
		Assert.assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()),CoreMatchers.is(true));
		Assert.assertThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), CoreMatchers.is(true));
		
		// Caso eu não queira quebrar o meu teste em tantos métodos, com acertivas individuais eu posso usar o @Rule, que irá me trazer
		// todos os erros do meu método de 1x só
		// Alterando p/ checkThat do Rule
		//error.checkThat(locacao.getValor(), CoreMatchers.is(CoreMatchers.equalTo(6.0)));
		//error.checkThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()),CoreMatchers.is(false));
		//error.checkThat(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), CoreMatchers.is(false));
		
	}
	
	// Formas de dividir um teste
	// Existe um conceito na comunidade que a letra I do FIRST significa: independente e isolado. Desta forma, devemos ter 
	// uma única acertiva por método
	// Na classe acima temos 3 acertivas p/ o método, portanto eu poderia quebrar em mais 2 métodos
	
	
	@Test
	public void testeDataLocacao() throws Exception {
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);
		Locacao locacao = service.alugarFilme(usuario, filme);
		Assert.assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()),CoreMatchers.is(true));
	}
	
	@Test
	public void testeDataDevolucao() throws Exception {
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);
		Locacao locacao = service.alugarFilme(usuario, filme);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}
	
	// Formas de validar um teste que está esperando uma exeção
	
	// Forma 1. Elegante -> adicionar (expected=Exception.class) 
	@Test(expected=Exception.class)
	public void testLocacao_filmeSemEstoque() throws Exception {
		// cenário
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0); 
		
		// acao
		service.alugarFilme(usuario, filme);
	}
	
	// Forma 2. Robusta. Permite maior controle durante a execução do teste que a forma elegante não me dá
	// Neste caso eu não lanço a exceção eu trato ela com try-catch
	@Test
	public void testLocacao_filmeSemEstoque2() {
		// cenário
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0); 
		
		// acao
		try {
			service.alugarFilme(usuario, filme);
			// Estou usando Assert.fail p/ evitar um falso positivo, uma vez que o método só passa se o produto não tiver estoque
			Assert.fail("Deveria ter lançado uma exceção"); 
		} catch (Exception e) {
			// TODO Auto-generated catch block
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Filme sem estoque"));
			//e.printStackTrace();
		}
	}
	
	// Forma 3. NOVA forma + Recente -> Implementar a rule public ExpectedException exception = ExpectedException.none();
	@Test
	public void testLocacao_filmeSemEstoque3() throws Exception {
		// cenário
		LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0); 
		// implementar a @Rule dentro do cenário
		exception.expect(Exception.class);
		exception.expectMessage("Filme sem estoque");
		
		// acao
		service.alugarFilme(usuario, filme);
		
	}
}

































