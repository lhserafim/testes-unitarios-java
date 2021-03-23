package br.ce.wcaquino.servicos;


import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.sun.tools.javac.code.Attribute.Array;

import br.ce.waquino.exceptions.FilmeSemEstoqueException;
import br.ce.waquino.exceptions.LocadoraException;
import br.ce.wcaquino.builder.FilmeBuilder;
import br.ce.wcaquino.builder.LocacaoBuilder;
import br.ce.wcaquino.builder.UsuarioBuilder;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.servicos.LocacaoService;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoServiceTest {

	// Estou colocando a LocacaoService como global, para poder usar no @Before e remover as diversas instanciações de cada test
	@InjectMocks
	private LocacaoService service;
	private int contador = 0;
	
	// Coloquei como variáveis globais, tirei do before, para poder ser usado por outros métodos
	@Mock
	private LocacaoDAO dao;
	@Mock
	private SPCService spc;
	@Mock
	private EmailService email;
	
	@Rule
	public ErrorCollector error = new ErrorCollector();
	
	@Rule // Está rule é usada p/ a forma NOVA
	public ExpectedException exception = ExpectedException.none();
	
	// Utilização de @Before e @After 
	// Como o próprio nome diz, posso utilizar para SIMPLIFICAR meus testes e já deixar algumas ações "automatizadas"
	@Before
	public void setup() {
		System.out.println("Before");	
		service = new LocacaoService();		
		contador++;
		System.out.println(contador); // contador não funciona pq o JUnit sempre zera o valor p/ garantir e evitar sujeira nas variáveis
		// CASO EU QUEIRA levar o valor p/ os demais testes. Usar static na variável!
		
		// USANDO O MOCKITO para mockar e poder usar no método salvar()
//		dao = Mockito.mock(LocacaoDAO.class);//classToMock // interface LocacaoDAO 
//		service.setLocacaoDAO(dao);
//		spc = Mockito.mock(SPCService.class);
//		service.setSPCService(spc);
//		email = Mockito.mock(EmailService.class);
//		service.setEmailService(email);
		
		// como usei as anotações @InjectMock e @Mock NÃO PRECISO FAZER os usos acima. Basta
		MockitoAnnotations.initMocks(this);
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
		//Como o teste só vai funcionar em um sábado, para evitar erros eu preciso usar o Assume
	    //O ASSUME vai trabalhar como se fosse um IF. Se NÃO FOR sábado ele roda o teste, se não ele skip
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
				
		// cenario
		// no cenário, eu vou inicializar tudo o que eu preciso, ou seja vou instanciar as classes: LocacaoService, Usuario e Filme
		
		//LocacaoService service = new LocacaoService();
		
		//Usuario usuario = new Usuario("Usuario 1");
		//Alterado p/ chamar o BUILDER
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		//Filme filme = new Filme("Filme 1", 2, 5.0); // Instanciei o valor de 5.0, que é o valor que vou validar no meu teste
		//List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		// acao
		// é onde eu farei a execução do método que eu quero testar
		
		// Em teste unitário, quando um método necessitar de Exceção EVITAR envolvê-lo em try-catch, pois este irá dificultar
		// para localizar o erro. Ao invés do try-catch LANÇAR a exceção (throws Exception)
		// Se o teste não está esperando exceção nenhuma DEIXE que o JUnit gerencie p/ vc (usando o throws Exception)
		// Já quando o teste espera alguma exceção temos 3 formas de verificar
		
		
		Locacao locacao = service.alugarFilme(usuario, filmes);
		
		// Diferença entre ERRO e FALHAS:
		// FALHAS: É quando o teste não passa em alguma verificação lógica, como os Asserts por exemplo
		// ERROS: É quando algum problema durante a execução do teste IMPEDE que ele seja concuído 
		
		// Eu só posso colocar várias assertivas se o cenário e a ação forem os mesmos.
		
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
		Usuario usuario = UsuarioBuilder.umUsuario().agora(); //UsuarioBuilder.umUsuario().agora();
		//Filme filme = new Filme("Filme 1", 2, 5.0);
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));
		Locacao locacao = service.alugarFilme(usuario, filmes);
		Assert.assertThat(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()),CoreMatchers.is(true));
	}
	
	@Test
	public void testeDataDevolucao() throws Exception {
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		//LocacaoService service = new LocacaoService();
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		//Filme filme = new Filme("Filme 1", 2, 5.0);
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 5.0));
		Locacao locacao = service.alugarFilme(usuario, filmes);
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
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		//Filme filme = new Filme("Filme 1", 0, 5.0);
		//List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));
		//USANDO O BUILDER
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().semEstoque().agora());
		
		// acao
		service.alugarFilme(usuario, filmes);
	}
	
	// Forma 2. Robusta. Permite maior controle durante a execução do teste que a forma elegante não me dá
	// Neste caso eu não lanço a exceção eu trato ela com try-catch
	@Test
	public void testLocacao_filmeSemEstoque2() {
		// cenário
		//LocacaoService service = new LocacaoService();
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		//Filme filme = new Filme("Filme 1", 0, 5.0);
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));
		
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
			service.alugarFilme(usuario, filmes);
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
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		//Filme filme = new Filme("Filme 1", 0, 5.0);
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));
		// implementar a @Rule dentro do cenário
		//exception.expect(Exception.class);
		//exception.expectMessage("Filme sem estoque");
		exception.expect(FilmeSemEstoqueException.class);
		
		// acao
		service.alugarFilme(usuario, filmes);
		
	}
	
	// Forma robusta + indicada pelo professor
	// REPARE que como eu só quero validar o LocadoraException, as demais exceptions ficarm fora do try catch
	// para evitar falso positivo
	@Test
	public void testLocacao_usuarioVazio() throws FilmeSemEstoqueException, Exception {
		// cenario
		//LocacaoService service = new LocacaoService();
		//Filme filme = new Filme("Filme 2", 1, 4.0);
		List<Filme> filmes = Arrays.asList(new Filme("Filme 2", 1, 4.0));
		// não estou instanciando usuario, pq quero q de null
		
		// acao
		try {
			service.alugarFilme(null, filmes);
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
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		// implementar a @Rule dentro do cenário
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme vazio"); // A mensagem precisa ser IDENTICA a da classe service
		
		// acao
		service.alugarFilme(usuario, null);		
		
	}
	
	
	@Test
	public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException, Exception {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1",2,4.0),new Filme("Filme 2",2,4.0),new Filme("Filme 3",2,4.0));
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificacao
		// o resultado esperado aqui é 11 (4 + 4 + 4) - 25% desc
		Assert.assertThat(resultado.getValor(), CoreMatchers.is(11.0));
	}
	
	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException, Exception {
		//Como o teste só vai funcionar em um sábado, para evitar erros eu preciso usar o Assume
		//O ASSUME vai trabalhar como se fosse um IF. Se for sábado ele roda o teste, se não ele skip
		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
				
		//cenario
		Usuario usuario = new Usuario("Usuário 1");
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));
		
		//acao
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		//verificacao
		boolean ehSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		Assert.assertTrue(ehSegunda);
	}
	
	// Este teste precisa da injeção usando Mockito
	// NESTE TESTE ESTOU FAZENDO 2 VALIDAÇÕES
	// 1 -> Se o sistema está IMPEDINDO a locação para negativado
	// 2 -> Se o método de verificação no SPC está sendo chamado
	@Test
	public void naoDeveAlugarFilmeParaNegativadoSPC() throws FilmeSemEstoqueException, LocadoraException, Exception {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		// Como o retorno PADRÃO do mockito, para booleans é FALSE e para meu teste eu preciso de TRUE, eu "seto" mudo o comportamento
		// de retorno do mockito, conforme abaixo.
		Mockito.when(spc.possuiNegativacao(usuario)).thenReturn(true);
		
		// Como o resultado experado é o lançamento de uma exceção, preciso tratar pela forma nova
		exception.expect(LocadoraException.class);
		exception.expectMessage("Usuário Negativado"); // VERIFICAÇÃO 1
		
		// ATENÇÃO O exception precisa vir antes da ação!
		
		//acao
		service.alugarFilme(usuario, filmes);
		
		//verificacao
		//VERIFICAÇÃO 2
		Mockito.verify(spc).possuiNegativacao(usuario);
		
	}
	
	@Test
	public void deveEnviarEmailParaLocacoesAtrasadas() {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		Usuario usuario2 = UsuarioBuilder.umUsuario().comNome("Usuario em dia").agora();
		Usuario usuario3 = UsuarioBuilder.umUsuario().comNome("Outro atrasado").agora();
		
		// Preciso que o dao retorne uma lista de locações pendentes
		// Ou seja, criei um cenário com 1 locação pendente de 2 dias de atraso
		//List<Locacao> locacoes = Arrays.asList(LocacaoBuilder.umLocacao().comUsuario(usuario).comDataRetorno(DataUtils.obterDataComDiferencaDias(-2)).agora());		
		List<Locacao> locacoes = Arrays.asList(
				LocacaoBuilder.umLocacao().atrasado().comUsuario(usuario).agora(),
				LocacaoBuilder.umLocacao().comUsuario(usuario2).agora(),
				LocacaoBuilder.umLocacao().atrasado().comUsuario(usuario3).agora()
				);
		
		// expectativa p/ o dao retornar esta lista, usando mock
		// ou seja, eu falo o que o mockito precisa retornar, que é um alista com locação pendente setada no cenário acima
		Mockito.when(dao.obterLocacoesPendentes()).thenReturn(locacoes);
		
		//acao
		service.notificarAtrasos();
		
		//verificacao
		// O que vou verificar aqui é se o sistema está enviando os emails para os usuario em atraso
		Mockito.verify(email).notificarAtraso(usuario); // verificar se usuario (atrasado) recebeu email
		Mockito.verify(email).notificarAtraso(usuario3);// verificar se usuario3 (atrasado) recebeu email
		Mockito.verify(email,Mockito.atLeastOnce()).notificarAtraso(usuario3); // verificar se o email foi enviado ao usuário3 ao menos 1x
		// usando o never()
		Mockito.verify(email, Mockito.never()).notificarAtraso(usuario2); // verificar se usuario2 (no prazo) NÃO recebeu email
		
		// VERIFICAR que nenhum outro usuário (fora os atrasados) recebeu email de notificação
		Mockito.verifyNoMoreInteractions(email);
		
		// VERIFICAS se o método do SPC foi chamado. Ele NÃO DEVE ser chamado!
		Mockito.verifyZeroInteractions(spc);
		
		// Verificar se a notificação por atraso está acontecendo 2x p/ Usuários
		// Preciso usar o any e a class, para não especificar o usuário
		// o any é um Matcher
		Mockito.verify(email, Mockito.times(2)).notificarAtraso(Mockito.any(Usuario.class));
	}
	
	// Lançando Exceções
	// Supondo que eu acesse um serviço externo e que pode ser instável, como o SPC
	@Test
	public void deveTratarErroNoSPC() throws FilmeSemEstoqueException, LocadoraException, Exception {
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(FilmeBuilder.umFilme().agora());
		
		//Vou criar uma expectativa p/ simular uma chamada ao serviço do SPC que caiu (Timeout)
		Mockito.when(spc.possuiNegativacao(usuario)).thenThrow(new Exception("Timeout"));
		
		//verificacao
		// Quando uso a "forma nova" a verificação precisa ficar antes
		// Além disso, para que este teste possa funcionar, preciso adicionar a exceção na Interface SPCService
		exception.expect(LocadoraException.class);
		exception.expectMessage("Timeout ao consultar o serviço do SPC");
		
		//acao
		service.alugarFilme(usuario, filmes);
				
	}
	
	@Test
	public void deveProrrogarUmaLocacao() {
		//cenario
		Locacao locacao = LocacaoBuilder.umLocacao().agora();
		
		//acao
		service.prorrogarLocacao(locacao, 3);
		
		//verificacao
		Mockito.verify(dao).salvar(Mockito.any(Locacao.class));
	}
}
































































