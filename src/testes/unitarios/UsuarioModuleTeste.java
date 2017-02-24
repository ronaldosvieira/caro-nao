package testes.unitarios;

import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import dominio.UsuarioModule;
import excecoes.EmailJaCadastradoException;
import excecoes.ErroDeValidacao;
import excecoes.UsuarioNaoExisteException;

public class UsuarioModuleTeste {
	private static UsuarioModule um;
	
	@BeforeClass
	public static void before() throws ClassNotFoundException, SQLException {
		um = new UsuarioModule();
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeInserirUsuarioComNomeNulo() 
			throws EmailJaCadastradoException, SQLException, ErroDeValidacao {
		um.inserirUsuario(null, "teste@teste.com", "39282943");
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeInserirUsuarioComEmailNulo() 
			throws EmailJaCadastradoException, SQLException, ErroDeValidacao {
		um.inserirUsuario("Teste", null, "39282943");
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeInserirUsuarioComEmailInvalido() 
			throws EmailJaCadastradoException, SQLException, ErroDeValidacao {
		um.inserirUsuario("Teste", "teste", "39282943");
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeInserirUsuarioComTelefoneNulo() 
			throws EmailJaCadastradoException, SQLException, ErroDeValidacao {
		um.inserirUsuario("Teste", "teste@teste.com", null);
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeAtualizarUsuarioComNomeNulo() 
			throws SQLException, UsuarioNaoExisteException, ErroDeValidacao {
		um.atualizarUsuario(1, null, "88798789");
	}
	
	@Test(expected = ErroDeValidacao.class)
	public void testeAtualizarUsuarioComTelefoneNulo() 
			throws SQLException, UsuarioNaoExisteException, ErroDeValidacao {
		um.atualizarUsuario(1, "Teste", null);
	}
}
