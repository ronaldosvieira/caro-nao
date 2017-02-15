package testes.funcionais;

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import dominio.GrupoModule;
import excecoes.DesativacaoGrupoInvalidaException;
import excecoes.GrupoNaoExisteException;
import excecoes.GrupoUsuarioJaExisteException;
import excecoes.GrupoUsuarioNaoExisteException;
import util.RecordSet;
import util.Row;

public class GrupoModuleTeste extends TesteFuncional {
	private static GrupoModule gm;
	
	public GrupoModuleTeste() throws ClassNotFoundException, SQLException {
		gm = new GrupoModule();
	}
	
	@Test
	public void testeObter() throws SQLException, GrupoNaoExisteException {
		RecordSet grupo = gm.obter(1);
		
		assertEquals(1, grupo.size());
		assertEquals("Grupo de carona #1", grupo.get(0).getString("nome"));
	}
	
	@Test(expected = GrupoNaoExisteException.class)
	public void testeObterNaoExistente() throws SQLException, GrupoNaoExisteException {
		gm.obter(50);
	}
	
	@Test
	public void testeObterVarios() throws SQLException {
		RecordSet varios = new RecordSet();
		
		Row grupo1 = new Row();
		grupo1.put("grupo_id", 1);
		varios.add(grupo1);
		
		Row grupo2 = new Row();
		grupo2.put("grupo_id", 2);
		varios.add(grupo2);
		
		RecordSet grupos = gm.obterVarios("grupo_id", varios);
		
		assertEquals(2, grupos.size());
		
		assertTrue(grupos.contains("id", 1));
		assertTrue(grupos.contains("id", 2));
		
		assertTrue(grupos.contains("nome", "Grupo de carona #1"));
		assertTrue(grupos.contains("nome", "Grupo vazio"));
	}
	
	@Test
	public void testeObterVariosNaoExistente() throws SQLException {
		RecordSet varios = new RecordSet();
		
		Row grupo1 = new Row();
		grupo1.put("grupo_id", 1);
		varios.add(grupo1);
		
		Row grupo2 = new Row();
		grupo2.put("grupo_id", 50);
		varios.add(grupo2);
		
		RecordSet grupos = gm.obterVarios("grupo_id", varios);
		
		assertEquals(1, grupos.size());
		
		assertTrue(grupos.contains("id", 1));
		
		assertTrue(grupos.contains("nome", "Grupo de carona #1"));
	}
	
	@Test
	public void testeObterVariosVazio() throws SQLException {
		RecordSet grupos = gm.obterVarios("grupo_id", new RecordSet());
		
		assertEquals(0, grupos.size());
	}
	
	@Test
	public void testeListarUsuarios() throws ClassNotFoundException, SQLException {
		RecordSet usuarios = gm.listarUsuarios(1);
		
		assertEquals(1, usuarios.size());
		assertEquals("Rodrigo", usuarios.get(0).getString("nome"));
	}
	
	@Test
	public void testeListarUsuariosVazio() throws ClassNotFoundException, SQLException {
		RecordSet usuarios = gm.listarUsuarios(2);
		
		assertEquals(0, usuarios.size());
	}
	
	@Test
	public void testeListarUsuariosNaoExistente() throws ClassNotFoundException, SQLException {
		RecordSet usuarios = gm.listarUsuarios(50);
		
		assertEquals(0, usuarios.size());
	}
	
	@Test
	public void testeInserirGrupo() throws SQLException, ClassNotFoundException, GrupoUsuarioJaExisteException {
		String nome = "AAAAAAAAA";
		String descricao = "aaaaaaaaaa";
		String regras = "Nenhuma";
		int limite = 5;
		
		gm.inserirGrupo(2, nome, descricao, regras, limite);
		
		String sql = "select * from grupo where nome = \'AAAAAAAAA\';";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		rs.next();
		
		assertEquals(rs.getString("nome"), nome);
		assertEquals(rs.getString("descricao"), descricao);
		assertEquals(rs.getString("regras"), regras);
		assertEquals(rs.getInt("limite_avaliacoes_negativas"), limite);
	}
	
	@Test
	public void testeAtualizarGrupo() throws SQLException, GrupoNaoExisteException {
		String nome = "AAAAA";
		String descricao = "aaaa";
		int limite = 10;
		
		gm.atualizarGrupo(2, nome, descricao, limite);
		
		String sql = "select * from grupo where nome = \'AAAAA\';";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		
		assertTrue(rs.next());
		
		assertEquals(rs.getString("nome"), nome);
		assertEquals(rs.getString("descricao"), descricao);
		assertEquals(rs.getInt("limite_avaliacoes_negativas"), limite);
	}
	
	@Test(expected = GrupoNaoExisteException.class)
	public void testeAtualizarGrupoNaoExistente() throws SQLException, GrupoNaoExisteException {
		gm.atualizarGrupo(50, "AAAAAA", "aaa", 1);
	}
	
	@Test
	public void testeDesativarGrupo() throws ClassNotFoundException, SQLException, DesativacaoGrupoInvalidaException, GrupoNaoExisteException, GrupoUsuarioNaoExisteException {
		gm.desativarGrupo(1);
		
		String sql = "select * from grupo where id = 1;";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		
		assertTrue(rs.next());
		assertFalse(rs.getBoolean("ativo"));
	}
	
	@Test(expected = DesativacaoGrupoInvalidaException.class)
	public void testeDesativarGrupoComMaisDeUmaPessoa() throws SQLException, ClassNotFoundException, DesativacaoGrupoInvalidaException, GrupoNaoExisteException, GrupoUsuarioNaoExisteException {
		String sql = "insert into grupo_usuario (grupo_id, usuario_id) "
				+ "values (1, 2);";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		stmt.executeUpdate();
		
		gm.desativarGrupo(1);
	}
}
