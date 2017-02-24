package testes.unitarios;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import dominio.GrupoModule;
import excecoes.ErroDeValidacao;
import excecoes.GrupoNaoExisteException;
import excecoes.GrupoUsuarioJaExisteException;

public class GrupoModuleTeste {
	private static GrupoModule gm;
	
	@BeforeClass
	public static void before() throws ClassNotFoundException, SQLException {
		gm = new GrupoModule();
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeInserirGrupoComNomeNulo() 
			throws ClassNotFoundException, SQLException, 
			GrupoUsuarioJaExisteException, ErroDeValidacao {
		gm.inserirGrupo(1, null, "teste teste", "testestestes", 5);
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeInserirGrupoComDescricaoNula() 
			throws ClassNotFoundException, SQLException, 
			GrupoUsuarioJaExisteException, ErroDeValidacao {
		gm.inserirGrupo(1, "Teste", null, "testestestes", 5);
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeInserirGrupoComRegrasNulo() 
			throws ClassNotFoundException, SQLException, 
			GrupoUsuarioJaExisteException, ErroDeValidacao {
		gm.inserirGrupo(1, "Teste", "teste teste", null, 5);
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeInserirGrupoComLimiteNegativo() 
			throws ClassNotFoundException, SQLException, 
			GrupoUsuarioJaExisteException, ErroDeValidacao {
		gm.inserirGrupo(1, "Teste", "teste teste", "testestestes", -1);
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeInserirGrupoComLimiteExcessivo() 
			throws ClassNotFoundException, SQLException, 
			GrupoUsuarioJaExisteException, ErroDeValidacao {
		gm.inserirGrupo(1, "Teste", "teste teste", "testestestes", 5000);
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeAtualizarGrupoComNomeNulo() 
			throws SQLException, GrupoNaoExisteException, ErroDeValidacao {
		gm.atualizarGrupo(1, null, "teste teste", 5);
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeAtualizarGrupoComDescricaoNula() 
			throws SQLException, GrupoNaoExisteException, ErroDeValidacao {
		gm.atualizarGrupo(1, "Teste", null, 5);
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeAtualizarGrupoComLimiteNegativo() 
			throws SQLException, GrupoNaoExisteException, ErroDeValidacao {
		gm.atualizarGrupo(1, "Teste", "teste teste", -1);
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeAtualizarGrupoComLimiteExcessivo() 
			throws SQLException, GrupoNaoExisteException, ErroDeValidacao {
		gm.atualizarGrupo(1, "Teste", "teste teste", 5000);
	}
}
