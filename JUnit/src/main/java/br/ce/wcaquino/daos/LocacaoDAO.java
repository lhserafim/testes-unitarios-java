package br.ce.wcaquino.daos;

import br.ce.wcaquino.entidades.Locacao;

// O mockito cria um objeto que RESPONDE como se fosse um objeto REAL. 
// Ou seja, se seu método retorna uma string, o mockito vai retonar uma string vazia, Se retorna um number, vai retornar 0 e assim por diante

// Porque mockamos? 
// Mockamos p/ evitar que o nosso testes unitário faça I/O, ou que persista algo em BD ou que acesse um endpoint externo etc.
// Ou seja, agentes externos que podem causar inconsistencias nos nossos testes unitários

public interface LocacaoDAO {
	
	public void salvar(Locacao locacao);
}
 