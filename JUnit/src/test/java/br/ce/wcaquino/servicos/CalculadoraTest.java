package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.ce.waquino.exceptions.NaoPodeDividirPorZeroException;

public class CalculadoraTest {
	
	// TDD -> TEST DRIVEN DEVELOPMENT
	// Neste caso eu começo o desenvolvimento pelos testes. Eu crio a classe de testes e norteio o meu desenvolvimento
	// pelos testes que vou fazendo
	
	// transformando em global
	private Calculadora calc;
	
	@Before // Usando o befor p/ instanciar calculadora em cada teste
	public void setup() {
		calc = new Calculadora();
	}
	
	@Test
	public void deveSomarDoisValores() {
		//cenario
		int a = 5;
		int b = 3;
		//Calculadora calc = new Calculadora(); transformado em global
		
		//acao
		int resultado = calc.somar(a,b);
		
		//verificacao
		Assert.assertEquals(8, resultado);
	}
	
	@Test
	public void deveSubtrairDoisValores() {
		//cenario
		int a = 8;
		int b = 5;
		//Calculadora calc = new Calculadora();
		
		//acao
		int resultado = calc.subtrair(a,b);
		
		//verificacao
		Assert.assertEquals(3, resultado);
	}
	
	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException {
		//cenario
		int a = 6;
		int b = 3;
		//Calculadora calc = new Calculadora();
		
		//acao
		int resultado = calc.divide(a,b);
		
		//verificacao
		Assert.assertEquals(2, resultado);
	}
	
	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		//cenario
		int a = 10;
		int b = 0;
		//Calculadora calc = new Calculadora();
		
		//acao
		calc.divide(a, b);
		
		//verificacao
		// neste caso é o lançamento da exceção tratada no método da classe
	}

}























