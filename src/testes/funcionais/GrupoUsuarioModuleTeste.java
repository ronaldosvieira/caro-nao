package testes.funcionais;

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import dominio.GrupoUsuarioModule;
import excecoes.GrupoUsuarioJaExisteException;
import excecoes.GrupoUsuarioNaoExisteException;
import util.RecordSet;

public class GrupoUsuarioModuleTeste extends TesteFuncional {
	private static GrupoUsuarioModule gum;
	
	public GrupoUsuarioModuleTeste() throws ClassNotFoundException, SQLException {
		gum = new GrupoUsuarioModule();
	}
	
	@Test
	public void testeObterGrupoUsuarioPorGrupo() throws SQLException {
		RecordSet gu = gum.obterGrupoUsuarioPorGrupo(1);
		
		assertEquals(1, gu.size());
		assertEquals(1, gu.get(0).getInt("usuario_id"));
		assertEquals(1, gu.get(0).getInt("grupo_id"));
	}
	
	@Test
	public void testeObterGrupoUsuarioPorGrupoVazio() throws SQLException {
		RecordSet gu = gum.obterGrupoUsuarioPorGrupo(2);
		
		assertEquals(0, gu.size());
	}
	
	@Test
	public void testeObterGrupoUsuarioPorGrupoNaoExistente() throws SQLException {
		RecordSet gu = gum.obterGrupoUsuarioPorGrupo(50);
		
		assertEquals(0, gu.size());
	}
	
	@Test
	public void testeObterGrupoUsuarioPorUsuario() throws SQLException {
		RecordSet gu = gum.obterGrupoUsuarioPorUsuario(1);
		
		assertEquals(1, gu.size());
		assertEquals(1, gu.get(0).getInt("usuario_id"));
		assertEquals(1, gu.get(0).getInt("grupo_id"));
	}
	
	@Test
	public void testeObterGrupoUsuarioPorUsuarioNaoExistente() throws SQLException {
		RecordSet gu = gum.obterGrupoUsuarioPorUsuario(50);
		
		assertEquals(0, gu.size());
	}
	
	@Test
	public void testeListarGruposPorUsuario() throws ClassNotFoundException, SQLException {
		RecordSet grupos = gum.listarGruposPorUsuario(1);
		
		assertEquals(1, grupos.size());
		assertEquals("Grupo de carona #1", grupos.get(0).getString("nome"));
	}
	
	@Test
	public void testeListarGruposPorUsuarioVazio() throws ClassNotFoundException, SQLException {
		RecordSet grupos = gum.listarGruposPorUsuario(2);
		
		assertEquals(0, grupos.size());
	}
	
	@Test
	public void testeListarGruposPorUsuarioNaoExistente() throws ClassNotFoundException, SQLException {
		RecordSet grupos = gum.listarGruposPorUsuario(50);
		
		assertEquals(0, grupos.size());
	}
	
	@Test
	public void testeListarUsuariosPorGrupo() throws ClassNotFoundException, SQLException {
		RecordSet usuarios = gum.listarUsuariosPorGrupo(1);
		
		assertEquals(1, usuarios.size());
		assertEquals("Rodrigo", usuarios.get(0).getString("nome"));
	}
	
	@Test
	public void testeListarUsuariosPorGrupoVazio() throws ClassNotFoundException, SQLException {
		RecordSet usuarios = gum.listarUsuariosPorGrupo(2);
		
		assertEquals(0, usuarios.size());
	}
	
	@Test
	public void testeListarUsuariosPorGrupoNaoExistente() throws ClassNotFoundException, SQLException {
		RecordSet usuarios = gum.listarUsuariosPorGrupo(50);
		
		assertEquals(0, usuarios.size());
	}
	
	@Test
	public void testeInserirGrupoUsuario() throws SQLException, GrupoUsuarioJaExisteException {
		int idGrupo = 1;
		int idUsuario = 2;
		
		gum.inserirGrupoUsuario(idGrupo, idUsuario);
		
		String sql = "select * from grupo_usuario "
				+ "where grupo_id = 1 and usuario_id = 2;";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		rs.next();
		
		assertEquals(rs.getInt("grupo_id"), idGrupo);
		assertEquals(rs.getInt("usuario_id"), idUsuario);
	}
	
	@Test(expected = GrupoUsuarioJaExisteException.class)
	public void testeInserirGruposUsuarioJaExistente() throws SQLException, GrupoUsuarioJaExisteException {
		gum.inserirGrupoUsuario(1, 1);
	}
	
	@Test
	public void testeDesativarGrupoUsuario() throws SQLException, GrupoUsuarioNaoExisteException {
		gum.desativarGrupoUsuario(1);
		
		String sql = "select * from grupo_usuario "
				+ "where grupo_id = 1 and usuario_id = 1;";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		rs.next();
		
		assertFalse(rs.getBoolean("ativo"));
	}
	
	@Test(expected = GrupoUsuarioNaoExisteException.class)
	public void testeDesativarGrupoUsuarioNaoExistente() throws SQLException, GrupoUsuarioNaoExisteException {
		gum.desativarGrupoUsuario(50);
	}
}
