package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class AssertTest {
	
	@Test
	public void test() {
		// Tipos de Assert. Booleans (true e false)
		Assert.assertTrue(true);
		Assert.assertFalse(false);
		// assertEquals, valida se a expressão é válida através da comparação. 
		Assert.assertEquals(1, 1);
		// Quando for fazer uma comparação de valores flutuantes, é necessário informar a margem de arredondamento
		Assert.assertEquals(Math.PI, 3.1513, 0.01);
		
		// Todos os tipos primitivos possuem uma representação em forma de objeto, as famosas classes Wrappers
		// No assertEquals o boxing e unboxing não é permitido
		int i = 5;
		Integer x = 5;
		//Assert.assertEquals(i, x); // Não é permitido. Necessário fazer a conversão
		Assert.assertEquals(Integer.valueOf(i), x);
		// ou
		Assert.assertEquals(i, x.intValue());
		Assert.assertEquals("bola", "bola");
		Assert.assertNotEquals("bola", "casa");
		// Para comparar ignorando o case sensitive necessário usar o assertTrue mais função p/ ignorar o case
		Assert.assertTrue("Bola".equalsIgnoreCase("bola"));
		Assert.assertTrue("bola".startsWith("bo"));
		
		// Quando comparar objetos é necessário tomar alguns cuidados.
		Usuario u1 = new Usuario("Usuario 1");
		Usuario u2 = new Usuario("Usuario 1");
		
		Assert.assertEquals(u1, u2); // isto irá comparar as instancias e não o conteúdo
		// para resolver é necessário implementar o método EQUALS do objeto 
		
		// Para comparar instancia
		Usuario u3 = u2;
		Assert.assertSame(u3, u2);  // assertNotSame
		
		// Para verificar se o objeto está nulo
		Usuario u4 = null;
		Assert.assertNull(u4);
		Assert.assertNotNull(u3);
		
	}

}

























