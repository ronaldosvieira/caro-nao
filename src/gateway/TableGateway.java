package gateway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class TableGateway implements AutoCloseable {
	private Connection conn;
	private String table;
	
	protected String select = "select * from ?;";
	protected String selectId = "select * from ? where id = ?;";
	protected String selectColumn = "select * from ? where ? = ?";
	protected String insert = "insert into ? (?) values (?);";
	protected String insertMany = "insert into ? (?) values ?;";
	protected String updateId = "update ? set ? where id = ?;";
	protected String deleteId = "delete from ? where id = ?";
	protected String softDeleteId = "update ? set ativo = ?";
	
	public TableGateway(String table) 
			throws ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver");
		
		String url = "jdbc:h2:~/carona";
		String user = "carona";
		String pass = "123456";
		
		this.conn = DriverManager.getConnection(url, user, pass);
		this.table = table;
	}

	protected Connection getConnection() {return this.conn;} 
	protected String getTableName() {return this.table;}
	
	public void close() throws SQLException {
		if (!conn.isClosed()) {
			conn.rollback();
			conn.close();
		}
	}
}
