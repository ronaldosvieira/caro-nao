package testes.funcionais;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import util.ConnectionFactory;
import util.ConnectionFactory.AmbienteBanco;

public abstract class TesteFuncional {
	private static Connection conn;
	
	protected Connection getConnection() {
		return this.conn;
	}
	
	@BeforeClass
	public static void setUp() throws SQLException, ClassNotFoundException, IOException {
		ConnectionFactory.alterarBanco(AmbienteBanco.teste);
		conn = ConnectionFactory.getConnection();
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
