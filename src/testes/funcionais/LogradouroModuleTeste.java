package testes.funcionais;

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import dominio.LogradouroModule;
import excecoes.CEPInvalidoException;
import excecoes.LogradouroNaoExisteException;
import excecoes.ServicoDeEnderecosInacessivelException;
import util.RecordSet;

public class LogradouroModuleTeste extends TesteFuncional {
	private static LogradouroModule lm;
	
	public LogradouroModuleTeste() throws ClassNotFoundException, SQLException {
		lm = new LogradouroModule();
	}
	
	@Test
	public void testeObter() throws SQLException, LogradouroNaoExisteException {
		RecordSet logradouro = lm.obter(1);
		
		assertEquals(1, logradouro.size());
		assertEquals("26170230", logradouro.get(0).getString("cep"));
	}
	
	@Test(expected = LogradouroNaoExisteException.class)
	public void testeObterNaoExistente() throws SQLException, LogradouroNaoExisteException {
		lm.obter(50);
	}
	
	@Test
	public void testeObterPorCep() throws LogradouroNaoExisteException, SQLException {
		RecordSet logradouro = lm.obterPorCep("26170230");
		
		assertEquals(1, logradouro.size());
		assertEquals("26170230", logradouro.get(0).getString("cep"));
	}
	
	@Test(expected = LogradouroNaoExisteException.class)
	public void testeObterPorCepNaoExistente() throws LogradouroNaoExisteException, SQLException {
		lm.obterPorCep("66666666");
	}
	
	@Test
	public void testeInserirLogradouro() throws SQLException, ServicoDeEnderecosInacessivelException, CEPInvalidoException {
		String cep = "26170230";
		String numero = "71";
		
		lm.inserirLogradouro(cep, numero);
		
		String sql = "select * from logradouro where id = 3;";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		rs.next();
		
		assertEquals(rs.getString("cep"), cep);
		assertEquals(rs.getString("numero"), numero);
	}
}
