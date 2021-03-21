package br.ce.wcaquino.builder;

import br.ce.wcaquino.entidades.Usuario;

public class UsuarioBuilder {
	
	
	private Usuario usuario;
	
	// O construtor do UsuarioBuilder ficou PRIVATE para que não seja possível criar instâncias do builder sem ser pelo método
	// Ou seja, não funciona new UsuarioBuilder...
	private UsuarioBuilder() {}
	
	// Método p/ retornar uma instância de UsuarioBuilder
	// ficou STATIC para que possa ser acessado externamente sem a necessidade de uma instância
	// este método foi criado p/ poder fazer o CHAIN METHODS, ou seja. UsuarioBuilder.umUsuario().agora()
	public static UsuarioBuilder umUsuario() {
		UsuarioBuilder builder = new UsuarioBuilder(); // cria instancia (nao tem outra forma, uma vez que o construtor é private)
		builder.usuario = new Usuario(); // inicializa instancia
		builder.usuario.setNome("Usuario 1"); // seta valor
		return builder; // retorna esta instancia de usuariobuilder
	}
	
	// este método "finaliza" o encadeamento
	public Usuario agora() {
		return usuario;
	}

}
