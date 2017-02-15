package testes.funcionais;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import dominio.CaronaUsuarioModule;
import excecoes.CaronaUsuarioJaExisteException;
import excecoes.LogradouroNaoExisteException;
import util.RecordSet;

public class CaronaUsuarioModuleTeste extends TesteFuncional {
	private static CaronaUsuarioModule cum;
	
	public CaronaUsuarioModuleTeste() throws ClassNotFoundException, SQLException {
		cum = new CaronaUsuarioModule();
	}
	
	@Test
	public void testeObterCaronaUsuarioPorCarona() throws SQLException {
		RecordSet cu = cum.obterCaronaUsuarioPorCarona(1);
		
		assertEquals(1, cu.size());
		assertEquals(1, cu.get(0).getInt("usuario_id"));
		assertEquals(1, cu.get(0).getInt("carona_id"));
	}
	
	@Test
	public void testeObterCaronaUsuarioPorCaronaVazio() throws SQLException {
		RecordSet cu = cum.obterCaronaUsuarioPorCarona(2);
		
		assertEquals(0, cu.size());
	}
	
	@Test
	public void testeObterCaronaUsuarioPorCaronaNaoExistente() throws SQLException {
		RecordSet cu = cum.obterCaronaUsuarioPorCarona(50);
		
		assertEquals(0, cu.size());
	}
	
	@Test
	public void testeObterCaronaUsuarioPorUsuario() throws SQLException {
		RecordSet cu = cum.obterCaronaUsuarioPorUsuario(1);
		
		assertEquals(1, cu.size());
		assertEquals(1, cu.get(0).getInt("usuario_id"));
		assertEquals(1, cu.get(0).getInt("carona_id"));
	}
	
	@Test
	public void testeObterCaronaUsuarioPorUsuarioNaoExistente() throws SQLException {
		RecordSet cu = cum.obterCaronaUsuarioPorUsuario(50);
		
		assertEquals(0, cu.size());
	}
	
	@Test
	public void testeListarUsuariosPorCarona() throws ClassNotFoundException, SQLException, LogradouroNaoExisteException {
		RecordSet usuarios = cum.listarUsuariosPorCarona(1);
		
		assertEquals(1, usuarios.size());
		assertEquals("Rodrigo", usuarios.get(0).getString("nome"));
	}
	
	@Test
	public void testeListarUsuariosPorCaronaVazio() throws ClassNotFoundException, SQLException, LogradouroNaoExisteException {
		RecordSet usuarios = cum.listarUsuariosPorCarona(2);
		
		assertEquals(0, usuarios.size());
	}
	
	@Test
	public void testeListarUsuariosPorCaronaNaoExistente() throws ClassNotFoundException, SQLException, LogradouroNaoExisteException {
		RecordSet usuarios = cum.listarUsuariosPorCarona(50);
		
		assertEquals(0, usuarios.size());
	}
	
	@Test
	public void testeInserirCaronaUsuario() throws SQLException, CaronaUsuarioJaExisteException {
		int idCarona = 1;
		int idUsuario = 2;
		int idLogradouro = 2;
		
		cum.inserirCaronaUsuario(idCarona, idUsuario, idLogradouro);
		
		String sql = "select * from carona_usuario "
				+ "where carona_id = 1 and usuario_id = 2;";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		rs.next();
		
		assertEquals(rs.getInt("carona_id"), idCarona);
		assertEquals(rs.getInt("usuario_id"), idUsuario);
	}
	
	@Test(expected = CaronaUsuarioJaExisteException.class)
	public void testeInserirCaronasUsuarioJaExistente() throws SQLException, CaronaUsuarioJaExisteException {
		cum.inserirCaronaUsuario(1, 1, 2);
	}
	
	@Test
	public void testeDesativarCaronaUsuario() throws SQLException {
		cum.excluirCaronaUsuario(1, 1);
		
		String sql = "select * from carona_usuario "
				+ "where carona_id = 1 and usuario_id = 1;";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		assertFalse(rs.next());
	}
}
