package testes.funcionais;

import static org.junit.Assert.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Test;

import dominio.CaronaModule;
import excecoes.CEPInvalidoException;
import excecoes.CaronaJaContemPassageirosException;
import excecoes.CaronaNaoAtivaException;
import excecoes.CaronaNaoExisteException;
import excecoes.CaronaUsuarioJaExisteException;
import excecoes.CaronaUsuarioNaoExisteException;
import excecoes.DataInvalidaException;
import excecoes.ErroDeValidacao;
import excecoes.LogradouroNaoExisteException;
import excecoes.ServicoDeEnderecosInacessivelException;
import excecoes.UsuarioNaoExisteException;
import excecoes.VeiculoComMenosVagasException;
import excecoes.VeiculoJaSelecionadoException;
import excecoes.VeiculoNaoExisteException;
import util.RecordSet;

public class CaronaModuleTeste extends TesteFuncional {
	private static CaronaModule cm;
	
	public CaronaModuleTeste() throws ClassNotFoundException, SQLException {
		cm = new CaronaModule();
	}
	
	@Test
	public void testeObterCarona() throws SQLException, CaronaNaoExisteException {
		RecordSet carona = cm.obter(1);
		
		assertEquals(1, carona.get(0).getInt("veiculo_id"));
		assertEquals(1, carona.get(0).getInt("logradouro_origem_id"));
		assertEquals(2, carona.get(0).getInt("logradouro_destino_id"));
	}
	
	@Test(expected = CaronaNaoExisteException.class)
	public void testeObterCaronaNaoExistente() throws SQLException, CaronaNaoExisteException {
		cm.obter(50);
	}
	
	@Test
	public void testeObterPeloVeiculo() throws SQLException {
		RecordSet caronas = cm.obterPeloVeiculo(1);
		
		assertEquals(1, caronas.size());
	}
	
	@Test
	public void testeObterPeloVeiculoNaoExistente() throws SQLException {
		RecordSet caronas = cm.obterPeloVeiculo(50);
		
		assertEquals(0, caronas.size());
	}
	
	@Test
	public void testeObterDono() throws ClassNotFoundException, SQLException, CaronaNaoExisteException, VeiculoNaoExisteException, UsuarioNaoExisteException {
		RecordSet dono = cm.obterDono(1);
		
		assertEquals(1, dono.size());
		assertEquals("Rodrigo", dono.get(0).getString("nome"));
	}
	
	@Test(expected = CaronaNaoExisteException.class)
	public void testeObterDonoCaronaNaoExistente() throws ClassNotFoundException, SQLException, CaronaNaoExisteException, VeiculoNaoExisteException, UsuarioNaoExisteException {
		cm.obterDono(50);
	}
	
	@Test
	public void testeListarUsuarios() throws ClassNotFoundException, SQLException, LogradouroNaoExisteException {
		RecordSet usuarios = cm.listarUsuarios(1);
		
		assertEquals(1, usuarios.size());
		assertEquals("Rodrigo", usuarios.get(0).getString("nome"));
	}
	
	@Test
	public void testeListarUsuariosCaronaNaoExistente() throws ClassNotFoundException, SQLException, LogradouroNaoExisteException {
		RecordSet usuarios = cm.listarUsuarios(50);
		
		assertEquals(0, usuarios.size());
	}
	
	@Test
	public void testeInserirCarona() 
			throws SQLException, ClassNotFoundException, 
			VeiculoJaSelecionadoException, ServicoDeEnderecosInacessivelException, 
			DataInvalidaException, CEPInvalidoException, VeiculoNaoExisteException, 
			CaronaUsuarioJaExisteException, UsuarioNaoExisteException, 
			ErroDeValidacao {
		int idVeiculo = 1;
		String data = "2018-02-15";
		String horario = "03:20";
		String cepOrigem = "26170230";
		String numeroOrigem = "11";
		String cepDestino = "26170230";
		String numeroDestino = "12";
		
		cm.inserirCarona(idVeiculo, data, horario, cepOrigem, 
				numeroOrigem, cepDestino, numeroDestino);
		
		String sql = "select * from carona where id = 2;";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		rs.next();
		
		assertEquals(rs.getInt("veiculo_id"), idVeiculo);
	}
	
	@Test(expected = DataInvalidaException.class)
	public void testeInserirCaronaDataInvalida() 
			throws ClassNotFoundException, SQLException, 
			VeiculoJaSelecionadoException, ServicoDeEnderecosInacessivelException, 
			DataInvalidaException, CEPInvalidoException, 
			VeiculoNaoExisteException, CaronaUsuarioJaExisteException, 
			UsuarioNaoExisteException, ErroDeValidacao {
		int idVeiculo = 1;
		String data = "2018-02-ds";
		String horario = "03:20";
		String cepOrigem = "26170230";
		String numeroOrigem = "11";
		String cepDestino = "26170230";
		String numeroDestino = "12";
		
		cm.inserirCarona(idVeiculo, data, horario, cepOrigem, 
				numeroOrigem, cepDestino, numeroDestino);
	}
	
	@Test(expected = CEPInvalidoException.class)
	public void testeInserirCaronaCEPInvalido() 
			throws ClassNotFoundException, SQLException, 
			VeiculoJaSelecionadoException, ServicoDeEnderecosInacessivelException, 
			DataInvalidaException, CEPInvalidoException, 
			VeiculoNaoExisteException, CaronaUsuarioJaExisteException, 
			UsuarioNaoExisteException, ErroDeValidacao {
		int idVeiculo = 1;
		String data = "2018-02-15";
		String horario = "03:20";
		String cepOrigem = "00000000";
		String numeroOrigem = "11";
		String cepDestino = "26170230";
		String numeroDestino = "12";
		
		cm.inserirCarona(idVeiculo, data, horario, cepOrigem, 
				numeroOrigem, cepDestino, numeroDestino);
	}
	
	@Test(expected = VeiculoNaoExisteException.class)
	public void testeInserirCaronaVeiculoNaoExistente() 
			throws ClassNotFoundException, SQLException, 
			VeiculoJaSelecionadoException, ServicoDeEnderecosInacessivelException, 
			DataInvalidaException, CEPInvalidoException, 
			VeiculoNaoExisteException, CaronaUsuarioJaExisteException, 
			UsuarioNaoExisteException, ErroDeValidacao {
		int idVeiculo = 50;
		String data = "2018-02-15";
		String horario = "03:20";
		String cepOrigem = "26170230";
		String numeroOrigem = "11";
		String cepDestino = "26170230";
		String numeroDestino = "12";
		
		cm.inserirCarona(idVeiculo, data, horario, cepOrigem, 
				numeroOrigem, cepDestino, numeroDestino);
	}
	
	@Test
	public void testeAtualizarVeiculoDaCarona() 
			throws SQLException, ClassNotFoundException, 
			VeiculoNaoExisteException, VeiculoJaSelecionadoException, 
			CaronaNaoExisteException, VeiculoComMenosVagasException, 
			CaronaJaContemPassageirosException, LogradouroNaoExisteException, 
			ServicoDeEnderecosInacessivelException, CEPInvalidoException, 
			CaronaUsuarioNaoExisteException, ErroDeValidacao {
		String sql = "update veiculo set vagas = 50 where id = 2;";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		stmt.executeUpdate();
		
		int idVeiculo = 2;
		
		cm.atualizarCarona(1, idVeiculo, "26170230", "28", "29238237", "s/n");
		
		String sql2 = "select * from carona where id = 1;";
		PreparedStatement stmt2 = this.getConnection().prepareStatement(sql2);
		
		ResultSet rs = stmt2.executeQuery();
		rs.next();
		
		assertEquals(rs.getInt("veiculo_id"), idVeiculo);
	}
	
	@Test(expected = VeiculoComMenosVagasException.class)
	public void testeAtualizarVeiculoDaCaronaMenosVagas() 
			throws ClassNotFoundException, SQLException, 
			VeiculoNaoExisteException, VeiculoJaSelecionadoException, 
			CaronaNaoExisteException, VeiculoComMenosVagasException, 
			CaronaJaContemPassageirosException, LogradouroNaoExisteException, 
			ServicoDeEnderecosInacessivelException, CEPInvalidoException, 
			CaronaUsuarioNaoExisteException, ErroDeValidacao {
		cm.atualizarCarona(1, 2, "26170230", "28", "29238237", "s/n");
	}
	
	@Test(expected = CaronaNaoExisteException.class)
	public void testeAtualizarVeiculoDaCaronaNaoExistente() 
			throws ClassNotFoundException, SQLException, VeiculoNaoExisteException, 
			VeiculoJaSelecionadoException, CaronaNaoExisteException, 
			VeiculoComMenosVagasException, CaronaJaContemPassageirosException, 
			LogradouroNaoExisteException, ServicoDeEnderecosInacessivelException, 
			CEPInvalidoException, CaronaUsuarioNaoExisteException, ErroDeValidacao {
		cm.atualizarCarona(50, 1, "26170230", "28", "29238237", "s/n");
	}
	
	@Test(expected = VeiculoNaoExisteException.class)
	public void testeAtualizarVeiculoDaCaronaVeiculoNaoExistente() 
			throws ClassNotFoundException, SQLException, VeiculoNaoExisteException, 
			VeiculoJaSelecionadoException, CaronaNaoExisteException, 
			VeiculoComMenosVagasException, CaronaJaContemPassageirosException, 
			LogradouroNaoExisteException, ServicoDeEnderecosInacessivelException, 
			CEPInvalidoException, CaronaUsuarioNaoExisteException, ErroDeValidacao {
		cm.atualizarCarona(1, 50, "26170230", "28", "29238237", "s/n");
	}
	
	@Test
	public void testeCancelarCarona() throws SQLException, CaronaNaoExisteException {
		String sql = "update carona set dia_horario = {ts \'2118-02-15 18:00:00.00\'} "
				+ "where id = 1;";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		stmt.executeUpdate();
		
		cm.cancelarCarona(1);
		
		String sql2 = "select * from carona where id = 1;";
		PreparedStatement stmt2 = this.getConnection().prepareStatement(sql2);
		
		ResultSet rs = stmt2.executeQuery();
		rs.next();
		
		assertEquals(2, rs.getInt("estado_carona_id"));
	}
	
	@Test
	public void testeCancelarCaronaAntiga() throws SQLException, CaronaNaoExisteException {
		String sql = "update carona set dia_horario = {ts \'2016-02-15 18:00:00.00\'} "
				+ "where id = 1;";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		stmt.executeUpdate();
		
		cm.cancelarCarona(1);
		
		String sql2 = "select * from carona where id = 1;";
		PreparedStatement stmt2 = this.getConnection().prepareStatement(sql2);
		
		ResultSet rs = stmt2.executeQuery();
		rs.next();
		
		assertEquals(1, rs.getInt("estado_carona_id"));
	}
	
	@Test(expected = CaronaNaoExisteException.class)
	public void testeCancelarCaronaNaoExistente() throws SQLException, CaronaNaoExisteException {
		cm.cancelarCarona(50);
	}
	
	@Test
	public void testeConcluirCarona() 
			throws SQLException, CaronaNaoExisteException, 
			ClassNotFoundException {
		String sql = "update carona set dia_horario = {ts \'2016-02-15 18:00:00.00\'} "
				+ "where id = 1;";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		stmt.executeUpdate();
		
		cm.concluirCarona(1);
		
		String sql2 = "select * from carona where id = 1;";
		PreparedStatement stmt2 = this.getConnection().prepareStatement(sql2);
		
		ResultSet rs = stmt2.executeQuery();
		rs.next();
		
		assertEquals(3, rs.getInt("estado_carona_id"));
	}
	
	@Test
	public void testeConcluirCaronaFutura() 
			throws SQLException, CaronaNaoExisteException, 
			ClassNotFoundException {
		String sql = "update carona set dia_horario = {ts \'2118-02-15 18:00:00.00\'} "
				+ "where id = 1;";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		stmt.executeUpdate();
		
		cm.concluirCarona(1);
		
		String sql2 = "select * from carona where id = 1;";
		PreparedStatement stmt2 = this.getConnection().prepareStatement(sql2);
		
		ResultSet rs = stmt2.executeQuery();
		rs.next();
		
		assertEquals(1, rs.getInt("estado_carona_id"));
	}
	
	@Test(expected = CaronaNaoExisteException.class)
	public void testeConcluirCaronaNaoExistente() 
			throws SQLException, CaronaNaoExisteException, 
			ClassNotFoundException {
		cm.concluirCarona(50);
	}
	
	@Test
	public void testeConvidarUsuarioComCep() 
			throws ClassNotFoundException, SQLException, 
			ServicoDeEnderecosInacessivelException, CEPInvalidoException, 
			CaronaUsuarioJaExisteException, ErroDeValidacao {
		cm.convidarUsuario(1, 2, "26170230");
		
		String sql = "select * from carona_usuario where carona_id = 1 and "
				+ "usuario_id = 2;";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		assertTrue(rs.next());
		
		assertEquals(1, rs.getInt("carona_id"));
		assertEquals(2, rs.getInt("usuario_id"));
	}
	
	@Test(expected = CEPInvalidoException.class)
	public void testeConvidarUsuarioComCepInvalido() 
			throws ClassNotFoundException, SQLException, 
			ServicoDeEnderecosInacessivelException, CEPInvalidoException, 
			CaronaUsuarioJaExisteException, ErroDeValidacao {
		cm.convidarUsuario(1, 2, "00000000");
	}
	
	@Test
	public void testeConvidarUsuarioComIdLogradouro() 
			throws SQLException, ClassNotFoundException, 
			CaronaUsuarioJaExisteException, ServicoDeEnderecosInacessivelException, 
			CEPInvalidoException {
		cm.convidarUsuario(1, 2, 2);
		
		String sql = "select * from carona_usuario where carona_id = 1 and "
				+ "usuario_id = 2;";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		assertTrue(rs.next());
		
		assertEquals(1, rs.getInt("carona_id"));
		assertEquals(2, rs.getInt("usuario_id"));
		assertEquals(2, rs.getInt("logradouro_id"));
	}
	
	@Test
	public void testeRemoverUsuarioDaCarona() 
			throws ClassNotFoundException, SQLException, 
			CaronaUsuarioNaoExisteException, CaronaNaoAtivaException, 
			CaronaNaoExisteException {
		cm.removerUsuarioDaCarona(1, 1);
		
		String sql = "select * from carona_usuario where carona_id = 1;";
		PreparedStatement stmt = this.getConnection().prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		assertFalse(rs.next());
	}
	
	@Test(expected = CaronaNaoExisteException.class)
	public void testeRemoverUsuarioDaCaronaNaoExistente() 
			throws ClassNotFoundException, SQLException, 
			CaronaUsuarioNaoExisteException, CaronaNaoAtivaException, 
			CaronaNaoExisteException {
		cm.removerUsuarioDaCarona(50, 1);
	}
	
	@Test(expected = CaronaUsuarioNaoExisteException.class)
	public void testeRemoverUsuarioNaoExistenteDaCarona() 
			throws ClassNotFoundException, SQLException, 
			CaronaUsuarioNaoExisteException, CaronaNaoAtivaException, 
			CaronaNaoExisteException {
		cm.removerUsuarioDaCarona(1, 50);
	}
}
