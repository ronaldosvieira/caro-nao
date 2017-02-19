package testes.funcionais;

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import dominio.UsuarioModule;
import excecoes.CaronaNaoAutorizadaException;
import excecoes.CaronaNaoExisteException;
import excecoes.EmailJaCadastradoException;
import excecoes.GrupoNaoAutorizadoException;
import excecoes.GrupoNaoExisteException;
import excecoes.GrupoUsuarioJaExisteException;
import excecoes.LogradouroNaoExisteException;
import excecoes.UsuarioNaoExisteException;
import excecoes.VeiculoNaoAutorizadoException;
import excecoes.VeiculoNaoExisteException;
import util.RecordSet;
import util.Row;

public class UsuarioModuleTeste extends TesteFuncional {
	private static UsuarioModule um;
	
	public UsuarioModuleTeste() throws ClassNotFoundException, SQLException {
		um = new UsuarioModule();
	}
	
	@Test
	public void testeAutenticar() throws SQLException, UsuarioNaoExisteException {
		RecordSet usuario = um.validarExistencia("ronaldo.vieira@ufrrj.br");
		
		assertEquals(1, usuario.size());
	}
	
	@Test(expected = UsuarioNaoExisteException.class)
	public void testeAutenticarNaoExistente() throws SQLException, UsuarioNaoExisteException {
		um.validarExistencia("email@nao.existente");
	}
	
	@Test
	public void testeObter() throws SQLException, UsuarioNaoExisteException {
		RecordSet usuario = um.obter(2);
		
		assertEquals(1, usuario.size());
		assertEquals("Ronaldo", usuario.get(0).getString("nome"));
	}
	
	@Test(expected = UsuarioNaoExisteException.class)
	public void testeObterNaoExistente() throws SQLException, UsuarioNaoExisteException {
		um.obter(50);
	}
	
	@Test
	public void testeObterVarios() throws SQLException {
		RecordSet usuarios = new RecordSet();
		
		Row usuarioRow = new Row();
		usuarioRow.put("usuario_id", 1);
		usuarios.add(usuarioRow);
		
		Row usuarioRow2 = new Row();
		usuarioRow2.put("usuario_id", 2);
		usuarios.add(usuarioRow2);
		
		RecordSet resultados = um.obterVarios("usuario_id", usuarios);
		
		assertEquals(2, resultados.size());
		
		assertTrue(resultados.contains("id", 1));
		assertTrue(resultados.contains("id", 2));
		
		assertTrue(resultados.contains("nome", "Rodrigo"));
		assertTrue(resultados.contains("nome", "Ronaldo"));
	}
	
	@Test
	public void testeObterVariosNaoExistente() throws SQLException {
		RecordSet usuarios = new RecordSet();
		
		Row usuarioRow = new Row();
		usuarioRow.put("usuario_id", 1);
		usuarios.add(usuarioRow);
		
		Row usuarioRow2 = new Row();
		usuarioRow2.put("usuario_id", 50);
		usuarios.add(usuarioRow2);
		
		RecordSet resultados = um.obterVarios("usuario_id", usuarios);
		
		assertEquals(1, resultados.size());
		
		assertTrue(resultados.contains("id", 1));
		
		assertTrue(resultados.contains("nome", "Rodrigo"));
	}
	
	@Test
	public void testeObterVariosColunaNaoExistente() throws SQLException {
		RecordSet usuarios = new RecordSet();
		
		Row usuarioRow = new Row();
		usuarioRow.put("usuario_id", 1);
		usuarios.add(usuarioRow);
		
		Row usuarioRow2 = new Row();
		usuarioRow2.put("usuario_id", 50);
		usuarios.add(usuarioRow2);
		
		RecordSet resultados = um.obterVarios("paralelepipedo", usuarios);
		
		assertEquals(0, resultados.size());
	}
	
	@Test
	public void testeListarGrupos() throws ClassNotFoundException, SQLException, UsuarioNaoExisteException {
		RecordSet grupos = um.listarGrupos(1);
		
		assertEquals(1, grupos.size());
		assertEquals("Grupo de carona #1", grupos.get(0).getString("nome"));
	}
	
	@Test
	public void testeListarGruposVazio() throws ClassNotFoundException, SQLException, UsuarioNaoExisteException {
		RecordSet grupos = um.listarGrupos(2);
		
		assertEquals(0, grupos.size());
	}
	
	@Test(expected = UsuarioNaoExisteException.class)
	public void testeListarGruposUsuarioNaoExistente() throws ClassNotFoundException, SQLException, UsuarioNaoExisteException {
		um.listarGrupos(50);
	}

	@Test
	public void testeListarVeiculos() throws ClassNotFoundException, SQLException, UsuarioNaoExisteException {
		RecordSet veiculos = um.listarVeiculos(1);
		
		assertEquals(2, veiculos.size());
		assertTrue(veiculos.contains("modelo", "Fiat Uno"));
		assertTrue(veiculos.contains("modelo", "Veloster"));
	}
	
	@Test
	public void testeListarVeiculosVazio() throws ClassNotFoundException, SQLException, UsuarioNaoExisteException {
		RecordSet veiculos = um.listarVeiculos(2);
		
		assertEquals(0, veiculos.size());
	}
	
	@Test(expected = UsuarioNaoExisteException.class)
	public void testeListarVeiculosUsuarioNaoExistente() throws ClassNotFoundException, SQLException, UsuarioNaoExisteException {
		um.listarVeiculos(50);
	}
	
	@Test
	public void testeListarCaronas() throws ClassNotFoundException, SQLException, UsuarioNaoExisteException, LogradouroNaoExisteException {
		RecordSet caronas = um.listarCaronas(1);
		
		assertEquals(1, caronas.size());
		assertTrue(caronas.contains("logradouro_origem_id", 1));
		assertTrue(caronas.contains("logradouro_destino_id", 2));
	}
	
	@Test
	public void testeListarCaronasVazio() throws ClassNotFoundException, SQLException, UsuarioNaoExisteException, LogradouroNaoExisteException {
		RecordSet caronas = um.listarCaronas(2);
		
		assertEquals(0, caronas.size());
	}
	
	@Test(expected = UsuarioNaoExisteException.class)
	public void testeListarCaronasUsuarioNaoExistente() throws ClassNotFoundException, SQLException, UsuarioNaoExisteException, LogradouroNaoExisteException {
		um.listarCaronas(50);
	}

	@Test
	public void testeInserirUsuario() throws SQLException, EmailJaCadastradoException {
		String nome = "Fellipe Duarte";
		String email = "duartefellipe@gmail.com";
		String telefone = "43243243";
		
		um.inserirUsuario(nome, email, telefone);
		
		String sql = "select * from usuario "
				+ "where email = \'duartefellipe@gmail.com\';";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		rs.next();
		
		assertEquals(rs.getString("nome"), nome);
		assertEquals(rs.getString("email"), email);
		assertEquals(rs.getString("telefone"), telefone);
	}
	
	@Test(expected = EmailJaCadastradoException.class)
	public void testeInserirUsuarioJaCadastrado() throws SQLException, EmailJaCadastradoException {
		String nome = "Ronaldo Nutella";
		String email = "ronaldo.vieira@ufrrj.br";
		String telefone = "43243243";
		
		um.inserirUsuario(nome, email, telefone);
	}
	
	@Test
	public void testeAtualizarUsuario() throws SQLException, UsuarioNaoExisteException {
		String nome = "Ronaldo Nutella";
		String telefone = "99999999";
		
		um.atualizarUsuario(2, nome, telefone);
		
		String sql = "select * from usuario where id = 2;";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		rs.next();
		
		assertEquals(rs.getString("nome"), nome);
		assertEquals(rs.getString("telefone"), telefone);
	}
	
	@Test(expected = UsuarioNaoExisteException.class)
	public void testeAtualizarUsuarioNaoExistente() throws SQLException, UsuarioNaoExisteException {
		String nome = "Ronaldo Nutella";
		String telefone = "99999999";
		
		um.atualizarUsuario(50, nome, telefone);
	}
	
	@Test
	public void testeValidarVeiculo() throws ClassNotFoundException, SQLException, VeiculoNaoAutorizadoException, UsuarioNaoExisteException {
		um.validarVeiculo(1, 1);
		um.validarVeiculo(1, 2);
	}
	
	@Test(expected = VeiculoNaoAutorizadoException.class)
	public void testeValidarVeiculoNaoPertencente() throws ClassNotFoundException, SQLException, VeiculoNaoAutorizadoException, UsuarioNaoExisteException {
		um.validarVeiculo(2, 1);
	}
	
	@Test(expected = UsuarioNaoExisteException.class)
	public void testeValidarVeiculoUsuarioNaoExistente() throws ClassNotFoundException, SQLException, VeiculoNaoAutorizadoException, UsuarioNaoExisteException {
		um.validarVeiculo(50, 1);
	}
	
	@Test(expected = VeiculoNaoAutorizadoException.class)
	public void testeValidarVeiculoVeiculoNaoExistente() throws ClassNotFoundException, SQLException, VeiculoNaoAutorizadoException, UsuarioNaoExisteException {
		um.validarVeiculo(1, 50);
	}

	@Test
	public void testeValidarGrupo() throws ClassNotFoundException, GrupoNaoAutorizadoException, SQLException, UsuarioNaoExisteException {
		um.validarGrupo(1, 1);
	}
	
	@Test(expected = GrupoNaoAutorizadoException.class)
	public void testeValidarGrupoNaoPertencente() throws ClassNotFoundException, GrupoNaoAutorizadoException, SQLException, UsuarioNaoExisteException {
		um.validarGrupo(2, 1);
	}
	
	@Test(expected = UsuarioNaoExisteException.class)
	public void testeValidarGrupoUsuarioNaoExistente() throws ClassNotFoundException, GrupoNaoAutorizadoException, SQLException, UsuarioNaoExisteException {
		um.validarGrupo(50, 1);
	}
	
	@Test(expected = GrupoNaoAutorizadoException.class)
	public void testeValidarGrupoGrupoNaoExistente() throws ClassNotFoundException, GrupoNaoAutorizadoException, SQLException, UsuarioNaoExisteException {
		um.validarGrupo(1, 50);
	}

	@Test
	public void testeValidarCarona() throws ClassNotFoundException, SQLException, CaronaNaoAutorizadaException, VeiculoNaoExisteException, UsuarioNaoExisteException, LogradouroNaoExisteException {
		um.validarCarona(1, 1);
	}
	
	@Test(expected = CaronaNaoAutorizadaException.class)
	public void testeValidarCaronaNaoPertencente() throws ClassNotFoundException, SQLException, CaronaNaoAutorizadaException, VeiculoNaoExisteException, UsuarioNaoExisteException, LogradouroNaoExisteException {
		um.validarCarona(2, 1);
	}
	
	@Test(expected = UsuarioNaoExisteException.class)
	public void testeValidarCaronaUsuarioNaoExistente() throws ClassNotFoundException, SQLException, CaronaNaoAutorizadaException, VeiculoNaoExisteException, UsuarioNaoExisteException, LogradouroNaoExisteException {
		um.validarCarona(50, 1);
	}
	
	@Test(expected = CaronaNaoAutorizadaException.class)
	public void testeValidarCaronaCaronaNaoExistente() throws ClassNotFoundException, SQLException, CaronaNaoAutorizadaException, VeiculoNaoExisteException, UsuarioNaoExisteException, LogradouroNaoExisteException {
		um.validarCarona(1, 50);
	}
	
	@Test
	public void testeValidarDonoCarona() throws ClassNotFoundException, SQLException, CaronaNaoExisteException, VeiculoNaoExisteException, CaronaNaoAutorizadaException, UsuarioNaoExisteException {
		um.validarDonoCarona(1, 1);
	}
	
	@Test(expected = CaronaNaoAutorizadaException.class)
	public void testeValidarDonoCaronaNaoPertencente() throws ClassNotFoundException, SQLException, CaronaNaoExisteException, VeiculoNaoExisteException, CaronaNaoAutorizadaException, UsuarioNaoExisteException {
		um.validarDonoCarona(2, 1);
	}
	
	@Test(expected = UsuarioNaoExisteException.class)
	public void testeVvalidarDonoCaronaUsuarioNaoExistente() throws ClassNotFoundException, SQLException, CaronaNaoExisteException, VeiculoNaoExisteException, CaronaNaoAutorizadaException, UsuarioNaoExisteException {
		um.validarDonoCarona(50, 1);
	}
	
	@Test(expected = CaronaNaoExisteException.class)
	public void testeValidarDonoCaronaCaronaNaoExistente() throws ClassNotFoundException, SQLException, CaronaNaoExisteException, VeiculoNaoExisteException, CaronaNaoAutorizadaException, UsuarioNaoExisteException {
		um.validarDonoCarona(1, 50);
	}
	
	@Test
	public void testeIsMotorista() throws ClassNotFoundException, SQLException, UsuarioNaoExisteException {
		assertTrue(um.isMotorista(1));
		assertFalse(um.isMotorista(2));
	}
	
	@Test(expected = UsuarioNaoExisteException.class)
	public void testeIsMotoristaNaoExistente() throws ClassNotFoundException, SQLException, UsuarioNaoExisteException {
		um.isMotorista(50);
	}
	
	@Test
	public void testeConvidarUsuario() throws ClassNotFoundException, SQLException, UsuarioNaoExisteException, GrupoUsuarioJaExisteException, GrupoNaoExisteException {
		um.convidarUsuario("ronaldo.vieira@ufrrj.br", 1);
		
		String sql = "select * from grupo_usuario "
				+ "where grupo_id = 1 and usuario_id = 2;";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		rs.next();
		
		assertEquals(rs.getInt("grupo_id"), 1);
		assertEquals(rs.getInt("usuario_id"), 2);
	}
	
	@Test(expected = GrupoUsuarioJaExisteException.class)
	public void testeConvidarUsuarioJaParticipa() throws ClassNotFoundException, SQLException, UsuarioNaoExisteException, GrupoUsuarioJaExisteException, GrupoNaoExisteException {
		um.convidarUsuario("rodrigovicente@gmail.com", 1);
	}
	
	@Test(expected = UsuarioNaoExisteException.class)
	public void testeConvidarUsuarioUsuarioNaoExistente() throws ClassNotFoundException, SQLException, UsuarioNaoExisteException, GrupoUsuarioJaExisteException, GrupoNaoExisteException {
		um.convidarUsuario("usuario@nao.existente", 1);
	}
	
	@Test(expected = GrupoNaoExisteException.class)
	public void testeConvidarUsuarioGrupoNaoExistente() throws ClassNotFoundException, SQLException, UsuarioNaoExisteException, GrupoUsuarioJaExisteException, GrupoNaoExisteException {
		um.convidarUsuario("rodrigovicente@gmail.com", 50);
	}
	
}
