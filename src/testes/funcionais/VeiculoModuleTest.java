package testes.funcionais;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dominio.VeiculoModule;
import excecoes.VeiculoNaoExisteException;
import util.ConnectionFactory;
import util.ConnectionFactory.AmbienteBanco;
import util.RecordSet;
import util.Row;

public class VeiculoModuleTest {
	private static Connection conn;
	private static VeiculoModule vm;
	
	@BeforeClass
	public static void setUp() throws SQLException, ClassNotFoundException, IOException {
		ConnectionFactory.alterarBanco(AmbienteBanco.teste);
		conn = ConnectionFactory.getConnection();
		
		vm = new VeiculoModule();
	}
	
	@Before
	public void populate() throws IOException, SQLException {
		String line;
		String sql = "";
		String sql2 = "";
		
		BufferedReader input = 
			new BufferedReader(
				new FileReader(
					new File("bd.sql")));
		BufferedReader input2 = 
				new BufferedReader(
					new FileReader(
						new File("test-data.sql")));
		
		while ((line = input.readLine()) != null) {
			sql += line;
		}
		
		input.close();
		
		while ((line = input2.readLine()) != null) {
			sql2 += line;
		}
		
		input2.close();
		
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.execute();
		
		PreparedStatement stmt2 = conn.prepareStatement(sql2);
		stmt2.execute();
	}
	
	@Test
	public void testeObterVeiculo() throws SQLException, VeiculoNaoExisteException {
		RecordSet veiculo = vm.obter(1);
		
		assertEquals("Fiat Uno", veiculo.get(0).getString("modelo"));
		assertEquals("SOD-4394", veiculo.get(0).getString("placa"));
		assertEquals("Azul marinho", veiculo.get(0).getString("cor"));
		assertEquals(4, veiculo.get(0).getInt("vagas"));
		assertEquals(1, veiculo.get(0).getInt("usuario_id"));
	}
	
	@Test(expected = VeiculoNaoExisteException.class)
	public void testeObterVeiculoNaoExistente() throws SQLException, VeiculoNaoExisteException {
		vm.obter(50);
	}
	
	@Test
	public void testeObterVarios() throws SQLException {
		RecordSet veiculos = new RecordSet();
		
		Row veiculoRow = new Row();
		veiculoRow.put("veiculo_id", 1);
		veiculos.add(veiculoRow);
		
		Row veiculoRow2 = new Row();
		veiculoRow2.put("veiculo_id", 2);
		veiculos.add(veiculoRow2);
		
		RecordSet resultado = vm.obterVarios("veiculo_id", veiculos);
		
		assertEquals(2, resultado.size());
		
		assertTrue(resultado.contains("id", 1));
		assertTrue(resultado.contains("id", 2));
		
		assertTrue(resultado.contains("placa", "SOD-4394"));
		assertTrue(resultado.contains("placa", "NWE-4821"));
	}

	@Test
	public void testeObterVariosNaoExistente() throws SQLException {
		RecordSet veiculos = new RecordSet();
		
		Row veiculoRow = new Row();
		veiculoRow.put("veiculo_id", 1);
		veiculos.add(veiculoRow);
		
		Row veiculoRow2 = new Row();
		veiculoRow2.put("veiculo_id", 50);
		veiculos.add(veiculoRow2);
		
		RecordSet resultado = vm.obterVarios("veiculo_id", veiculos);
		
		assertEquals(1, resultado.size());
		
		assertTrue(resultado.contains("id", 1));
		assertTrue(resultado.contains("placa", "SOD-4394"));
	}
	
	public void testeObterVariosPorUsuario() throws SQLException {
		RecordSet usuario = new RecordSet();
		
		Row usuarioRow = new Row();
		usuarioRow.put("usuario_id", 1);
		usuario.add(usuarioRow);
		
		RecordSet resultado = vm.obterVariosPorUsuario("usuario_id", usuario);
		
		assertEquals(2, resultado.size());
	}
	
	@Test
	public void testeObterVariosPorVariosUsuarios() throws SQLException {
		RecordSet usuario = new RecordSet();
		
		Row usuarioRow = new Row();
		usuarioRow.put("usuario_id", 1);
		usuario.add(usuarioRow);
		
		Row usuarioRow2 = new Row();
		usuarioRow2.put("usuario_id", 2);
		usuario.add(usuarioRow2);
		
		RecordSet resultado = vm.obterVariosPorUsuario("usuario_id", usuario);
		
		assertEquals(2, resultado.size());
	}
	
	@Test
	public void testeObterVariosPorUsuarioVazio() throws SQLException {
		RecordSet usuario = new RecordSet();
		
		RecordSet resultado = vm.obterVariosPorUsuario("usuario_id", usuario);
		
		assertEquals(0, resultado.size());
	}

	@Test
	public void testeObterDono() 
			throws SQLException, ClassNotFoundException, 
			VeiculoNaoExisteException {
		RecordSet resultado = vm.obterDono(1);
		
		assertEquals(1, resultado.get(0).getInt("id"));
		assertEquals("Rodrigo", resultado.get(0).getString("nome"));
	}
	
	@Test(expected = VeiculoNaoExisteException.class)
	public void testeObterDonoNaoExistente() 
			throws ClassNotFoundException, SQLException, 
			VeiculoNaoExisteException {
		vm.obterDono(50);
	}
	
	@Test
	public void testeInserirVeiculo() throws SQLException, VeiculoNaoExisteException {
		String modelo = "Palio";
		String placa = "JSD-8743";
		String cor = "Prata";
		int vagas = 5;
		
		vm.inserirVeiculo(modelo, placa, cor, vagas, 1);
		
		String sql = "select * from veiculo where placa = \'JSD-8743\';";
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		rs.next();
		
		assertEquals(rs.getString("modelo"), modelo);
		assertEquals(rs.getString("placa"), placa);
		assertEquals(rs.getString("cor"), cor);
		assertEquals(rs.getInt("vagas"), vagas);
	}
	
	@Test
	public void testeInserirVeiculoCount() throws SQLException, VeiculoNaoExisteException {
		String modelo = "Palio";
		String placa = "JSD-8743";
		String cor = "Prata";
		int vagas = 5;
		
		vm.inserirVeiculo(modelo, placa, cor, vagas, 1);
		
		String sql = "select * from veiculo where placa = \'JSD-8743\';";
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		int count = 0;
		
		while (rs.next()) {++count;}
		
		assertEquals(1, count);
	}
	
	@Test
	public void testeAtualizarVeiculo() throws SQLException, VeiculoNaoExisteException {
		String cor = "Verde";
		
		vm.atualizarVeiculo(1, cor);
		
		String sql = "select * from veiculo where id = 1;";
		PreparedStatement stmt = conn.prepareStatement(sql);
		
		ResultSet rs = stmt.executeQuery();
		rs.next();
		
		assertEquals(rs.getString("cor"), cor);
	}
	
	@Test(expected = VeiculoNaoExisteException.class)
	public void testeAtualizarVeiculoNaoExistente() throws SQLException, VeiculoNaoExisteException {
		String cor = "Verde";
		
		vm.atualizarVeiculo(50, cor);
	}
	
	@After
	public void erase() throws IOException, SQLException {
		String sql = "drop all objects;";
		
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.execute();
	}
	
	@AfterClass
	public static void tearDown() throws SQLException {
		ConnectionFactory.alterarBanco(AmbienteBanco.dev);
	}
}
