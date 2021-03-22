package br.ce.wcaquino.servicos;

import org.junit.Test;
import org.mockito.Mockito;

public class CalculadoraMockTest {

	
	@Test
	public void teste() {
		// Usando o mockito p/ mockar classes concretas
		Calculadora calc = Mockito.mock(Calculadora.class);
		//Expectativa
		//Ou seja, estou "ensinando" ao mock que ele deve retornar 5 na soma de 1 + 2
		Mockito.when(calc.somar(1, 2)).thenReturn(5);		
		System.out.println(calc.somar(1, 2));
		
		// Usando o matcher any. se usar um matcher como param precisa usar em todos os parametros
		Mockito.when(calc.somar(Mockito.anyInt(), Mockito.anyInt())).thenReturn(5);
		System.out.println(calc.somar(1, 2));
				
	}
}
