package br.ce.wcaquino.servicos;

import static br.ce.wcaquino.utils.DataUtils.adicionarDias;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.ce.waquino.exceptions.FilmeSemEstoqueException;
import br.ce.waquino.exceptions.LocadoraException;
import br.ce.wcaquino.daos.LocacaoDAO;
import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.utils.DataUtils;

public class LocacaoService {
	
	// Fazer as referências as interfaces
	private LocacaoDAO dao;
	private SPCService spcService;
	private EmailService emailService;
	
	public Locacao alugarFilme(Usuario usuario, List<Filme> filmes) throws FilmeSemEstoqueException, LocadoraException, Exception {
		
		
		if(usuario == null) {
			throw new LocadoraException("Usuário vazio");
		}
		
		if(filmes == null || filmes.isEmpty()) {
			throw new LocadoraException("Filme vazio");
		}
			
		// Colocar uma regra p/ validação de estoque
		for(Filme filme: filmes) {
			if(filme.getEstoque() == 0) {
				//throw new Exception("Filme sem estoque");
				throw new FilmeSemEstoqueException();
			}
		}
		
		boolean negativado;
		try {
			negativado = spcService.possuiNegativacao(usuario); 
		} catch(Exception e) {
			throw new LocadoraException("Timeout ao consultar o serviço do SPC");
		}
		
		if(negativado) {
			throw new LocadoraException("Usuário Negativado");
		}
		
		Locacao locacao = new Locacao();
		locacao.setFilmes(filmes);
		locacao.setUsuario(usuario);
		locacao.setDataLocacao(new Date());
		Double valorTotal = 0d;
//		for(Filme filme: filmes) {
//			valorTotal += filme.getPrecoLocacao();
//		}
		for(int i=0; i<filmes.size(); i++) {
			Filme filme = filmes.get(i);
			Double valorFilme = filme.getPrecoLocacao();
//			if(i == 2) {
//				valorFilme = valorFilme * 0.75;
//			}
			// Refatoração
			// Aplicando a regra dos descontos progressivos 
			switch(i) {
			case 2: valorFilme = valorFilme * 0.75; break;
			case 3: valorFilme = valorFilme * 0.50; break;
			case 4: valorFilme = valorFilme * 0.25; break;
			case 5: valorFilme = 0d; break;
			
			}
			
			valorTotal += valorFilme;
		}
		
		
//		locacao.setValor(filme.getPrecoLocacao());
		locacao.setValor(valorTotal);

		//Entrega no dia seguinte
		Date dataEntrega = new Date();
		dataEntrega = adicionarDias(dataEntrega, 1);
		//Se entrega cair no domingo, jogar p/ segunda
		if(DataUtils.verificarDiaSemana(dataEntrega, Calendar.SUNDAY)) {
			dataEntrega = adicionarDias(dataEntrega, 1);
		}
		locacao.setDataRetorno(dataEntrega);
		
		//Salvando a locacao...	
		dao.salvar(locacao);
		
		return locacao;
	}
	
	public void notificarAtrasos() {
		List<Locacao> locacoes = dao.obterLocacoesPendentes();
		// percorrendo a lista
		for(Locacao locacao: locacoes) { 
			if(locacao.getDataRetorno().before(new Date())) {
				emailService.notificarAtraso(locacao.getUsuario());
			
			}
			
		}
	}
	
	// Método criado p/ permitir uma prorrogação da locação, basicamente estou criando uma nova locação, 
	// pegando os parâmetros da locação anterior e atualizando data de entrega e valor
	public void prorrogarLocacao(Locacao locacao, int dias) {
		Locacao novaLocacao = new Locacao();
		novaLocacao.setUsuario(locacao.getUsuario());
		novaLocacao.setFilmes(locacao.getFilmes());
		novaLocacao.setDataLocacao(new Date());
		novaLocacao.setDataRetorno(DataUtils.obterDataComDiferencaDias(dias));
		novaLocacao.setValor(locacao.getValor() * dias);
		dao.salvar(novaLocacao);
	}
	
	
	// Para poder usar o mockito, no teste do método salvar, precisamos fazer a Injeção abaixo
	// Injeção da dependência
	// Para SIMULAR como se o objeto estivesse com valores para persistir na base
	public void setLocacaoDAO(LocacaoDAO dao) {
		this.dao = dao;
	}
	
	// Injetar
	public void setSPCService(SPCService spc) {
		spcService = spc;
	}
	
	// Injetar
	public void setEmailService(EmailService email) {
		emailService = email;
	}
		 
	

}




















