package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
	private static Connection conn;
	private static String[] bancos = {"~/carona", "mem:carona"};
	private static AmbienteBanco ambiente = AmbienteBanco.dev;
	
	public enum AmbienteBanco {
		dev(0), teste(1);
		private int num;
		
		private AmbienteBanco(int num) {this.num = num;}
		public int getNum() {return this.num;}
	}
	
	private ConnectionFactory() {}
	
	public static Connection getConnection() {
		if (conn == null) {
			try {
				Class.forName("org.h2.Driver");
				
				String url = "jdbc:h2:" + bancos[ambiente.getNum()];
				String user = "carona";
				String pass = "123456";
				
				conn = DriverManager.getConnection(url, user, pass);
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
	
	public static void alterarBanco(AmbienteBanco _ambiente) throws SQLException {
		ambiente = _ambiente;
		
		if (conn != null) {
			conn.close();
			conn = null;
		}
	}
	
	public static void alterarBanco(String url) throws SQLException, ClassNotFoundException {
		if (conn != null) {
			conn.close();
			conn = null;
		}
		
		Class.forName("org.h2.Driver");
		
		url = "jdbc:h2:" + url;
		String user = "carona";
		String pass = "123456";
		
		conn = DriverManager.getConnection(url, user, pass);
	}
}
