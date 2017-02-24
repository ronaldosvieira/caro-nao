package testes.unitarios;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import dominio.LogradouroModule;
import excecoes.CEPInvalidoException;
import excecoes.ErroDeValidacao;
import excecoes.ServicoDeEnderecosInacessivelException;

public class LogradouroModuleTeste {
	private static LogradouroModule lm;
	
	@BeforeClass
	public static void before() throws ClassNotFoundException, SQLException {
		lm = new LogradouroModule();
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeInserirLogradouroComCepNulo() 
			throws SQLException, ServicoDeEnderecosInacessivelException, 
			CEPInvalidoException, ErroDeValidacao {
		lm.inserirLogradouro(null, "28");
	}
	
	@Test(expected = CEPInvalidoException.class)
	public void testeInserirLogradouroComCepInválido1() 
			throws SQLException, ServicoDeEnderecosInacessivelException, 
			CEPInvalidoException, ErroDeValidacao {
		lm.inserirLogradouro("1234567890", "28");
	}
	
	@Test(expected = CEPInvalidoException.class)
	public void testeInserirLogradouroComCepInválido2() 
			throws SQLException, ServicoDeEnderecosInacessivelException, 
			CEPInvalidoException, ErroDeValidacao {
		lm.inserirLogradouro("abcdefgh", "28");
	}
}
