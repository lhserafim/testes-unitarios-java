package br.ce.wcaquino.suites;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import br.ce.wcaquino.servicos.AssertTest;
import br.ce.wcaquino.servicos.CalculadoraTest;
import br.ce.wcaquino.servicos.CalculoValorLocacaoTest;
import br.ce.wcaquino.servicos.LocacaoServiceTest;

@RunWith(Suite.class)
@SuiteClasses({ // adicionar manualmente todos as classes de teste
	AssertTest.class,
	CalculadoraTest.class,
	CalculoValorLocacaoTest.class,
	LocacaoServiceTest.class
})
public class SuiteExecucao {

	/**
	 * Esta classe centraliza a execucão das demais classes de teste
	 */
	
	// Posso implementar o Before e After na suíte e é muito útil quando precisa preparar algo p/ teste funcionais
	
	@BeforeClass
	public static void before() {
		System.out.println("Before");
	}
	
	@AfterClass
	public static void after() {
		System.out.println("After");
	}
	
}


















