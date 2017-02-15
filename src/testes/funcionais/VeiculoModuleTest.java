package testes.funcionais;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dbunit.operation.DatabaseOperation;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import dominio.VeiculoModule;
import excecoes.VeiculoNaoExisteException;
import junit.framework.TestCase;
import util.ConnectionFactory;
import util.ConnectionFactory.AmbienteBanco;

public class VeiculoModuleTest extends TestCase {
	private Connection conn;
	private VeiculoModule vm;
	
	@BeforeClass
	public void setUp() throws SQLException, ClassNotFoundException, IOException {
		ConnectionFactory.alterarBanco(AmbienteBanco.teste);
		conn = ConnectionFactory.getConnection();
		
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
		
		vm = new VeiculoModule();
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
		int count = 0;
		
		while (rs.next()) ++count;
		
		assertEquals(1, count);
	}
	
	@AfterClass
	public void tearDown() throws SQLException {
		ConnectionFactory.alterarBanco(AmbienteBanco.dev);
	}
}
