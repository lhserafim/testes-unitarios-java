package br.ce.wcaquino.servicos;


import java.util.Date;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.waquino.exceptions.FilmeSemEstoqueException;
import br.ce.waquino.exceptions.LocadoraException;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	// Estou colocando a LocacaoService como global, para poder usar no @Before e remover as diversas instanciações de cada test
	private LocacaoService service;
	private int contador = 0;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule // Está rule é usada p/ a forma NOVA
	public ExpectedException exception = ExpectedException.none();
	
	// Utilização de @Before e @After 
	// Como o próprio nome diz, posso utilizar para SIMPLIFICAR meus testes e já deixar algumas ações "automatizadas"
	@Before
	public void exemploBefore() {
		System.out.println("Before");	
		service = new LocacaoService();		
		contador++;
		System.out.println(contador); // contador não funciona pq o JUnit sempre zera o valor p/ garantir e evitar sujeira nas variáveis
	}
	
	@After
	public void exemploAfter() {
		System.out.println("After");	
	}
	
	
	@BeforeClass
	public static void exemploBeforeClass() { // precisa ser static
		System.out.println("Before Class");	
		
	}
	
	@AfterClass
	public static void exemploAfterClass() {
		System.out.println("After Class");	
	}	
	
	
	// Deixar o método abaixo como método de teste do JUnit
	// public static void main(String[] args) {
	
	// 1. Colocar um nome qualquer no método
	// 2. Colocar a anotação @Test (do org.junit)
	// 3. Rodar como JUnit test
	@Test
	public void testeLocacao() throws Exception {
				
		// cenario
		// no cenário, eu vou inicializar tudo o que eu preciso, ou seja vou instanciar as classes: LocacaoService, Usuario e Filme
		
		//LocacaoService service = new LocacaoService();
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
		//LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);
		Locacao locacao = service.alugarFilme(usuario, filme);
		Assert.assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()),CoreMatchers.is(true));
	}
	
	@Test
	public void testeDataDevolucao() throws Exception {
		//LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);
		Locacao locacao = service.alugarFilme(usuario, filme);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
	}
	
	// Formas de validar um teste que está esperando uma exeção
	
	// Forma 1. Elegante -> adicionar (expected=Exception.class)
	// O problema com a solução elegante é que ela lança um Exception e esse é muito genérico. Para que esta solução seja usada
	// de forma correta é necessário garantir que uma exceção só seja lançada por 1 caminho (cria classe de exeção ex: FilmeSemEstoqueException)
	//@Test(expected=Exception.class)
	@Test(expected=FilmeSemEstoqueException.class)
	public void testLocacao_filmeSemEstoque() throws Exception {
		// cenário
		//LocacaoService service = new LocacaoService();
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
		//LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0); 
		
		// acao
//		try {
//			service.alugarFilme(usuario, filme);
//			// Estou usando Assert.fail p/ evitar um falso positivo, uma vez que o método só passa se o produto não tiver estoque
//			Assert.fail("Deveria ter lançado uma exceção"); 
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			Assert.assertThat(e.getMessage(), CoreMatchers.is("Filme sem estoque"));
//			//e.printStackTrace();
//		}
		
		// Como a LocacaoService foi alterada, precisei incluir as demais exceptions no try catch
		try {
			service.alugarFilme(usuario, filme);
		} catch (FilmeSemEstoqueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LocadoraException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Forma 3. NOVA forma + Recente -> Implementar a rule public ExpectedException exception = ExpectedException.none();
	@Test
	public void testLocacao_filmeSemEstoque3() throws Exception {
		// cenário
		//LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0); 
		// implementar a @Rule dentro do cenário
		//exception.expect(Exception.class);
		//exception.expectMessage("Filme sem estoque");
		exception.expect(FilmeSemEstoqueException.class);
		
		// acao
		service.alugarFilme(usuario, filme);
		
	}
	
	// Forma robusta + indicada pelo professor
	// REPARE que como eu só quero validar o LocadoraException, as demais exceptions ficarm fora do try catch
	// para evitar falso positivo
	@Test
	public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException, Exception {
		// cenario
		//LocacaoService service = new LocacaoService();
		Filme filme = new Filme("Filme 2", 1, 4.0);
		// não estou instanciando usuario, pq quero q de null
		
		// acao
		try {
			service.alugarFilme(null, filme);
			Assert.fail(); // quando usar a forma robusta, sempre usar
		} catch (LocadoraException e) {
			Assert.assertThat(e.getMessage(), CoreMatchers.is("Usuário vazio"));
		}		
	}
	
	// Forma elegante
	@Test
	public void testLocacao_FilmeVazio() throws FilmeSemEstoqueException, LocadoraException, Exception {
		// cenário
		//LocacaoService service = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		
		// implementar a @Rule dentro do cenário
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio"); // A mensagem precisa ser IDENTICA a da classe service
		
		// acao
		service.alugarFilme(usuario, null);		
		
	}
	
}

































