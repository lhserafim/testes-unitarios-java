package br.ce.wcaquino.servicos;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import junit.framework.Assert;

public class CalculadoraMockTest {
	
	
	// COMO O MOCK FUNCIONA?
	// O mock sempre vai retornar um valor padrão (de acordo com o tipo, seja ele primitivo ou wrapper).
	// Porém, quando eu crio uma expectativa eu posso definir o que eu espero neste retorno .thenReturn(value) 
	@Mock
	private Calculadora calcMock;
	
	// COMO O SPY FUNCIONA?
	// O spy vai fazer a execução do método e retornar o valor do método a menos que você crie uma expectativa
	// O spy funciona apenas com classes concretas, ou seja, não funciona com interfaces.
	@Spy
	private Calculadora calcSpy;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void deveMostrarDiferencaEntreMockSpy() {
		// MOCK
		Mockito.when(calcMock.somar(1, 2)).thenReturn(1000);
		System.out.println("Mock igual expectativa: " + calcMock.somar(1, 2));
		System.out.println("Mock diferente da expectativa [retorna o valor default]: " + calcMock.somar(2, 2));
		
		// SPY
		Mockito.when(calcSpy.somar(1, 2)).thenReturn(1000);
		System.out.println("Spy igual expectativa: " + calcSpy.somar(1, 2));
		System.out.println("Spy diferente da expectativa [retorna valor da exec do metodo]: " + calcSpy.somar(2, 2));
		
		// Em alguns casos eu quero dizer a minha expectativa ANTES de executar o método. Assim uso o doReturn ou doNothing
		Mockito.doReturn(5).when(calcSpy).somar(1, 2);
		
	}
	
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
	
	//ArgumentCaptor
	@Test
	public void testeUsandoArgumentCaptor() {
		Calculadora calc = Mockito.mock(Calculadora.class);
		
		ArgumentCaptor<Integer> argCapt = ArgumentCaptor.forClass(Integer.class);
		Mockito.when(calc.somar(argCapt.capture(), argCapt.capture())).thenReturn(5);
		
		Assert.assertEquals(5, calc.somar(2304234, -23232));
		
		System.out.println(argCapt.getAllValues());
	}

}
