package gateway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class TableGateway implements AutoCloseable {
	private Connection conn;
	private String table;
	
	protected String select = "select * from ?;";
	protected String selectId = "select * from ? where id = ?;";
	protected String selectColumn = "select * from ? where ? = ?";
	protected String insert = "insert into ? (?) values (?);";
	protected String insertMany = "insert into ? (?) values ?;";
	protected String update = "update ? set ? = ?;";
	protected String updateMany = "update ? set ?;";
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
	
	abstract public ResultSet get() 
			throws SQLException;
	abstract public ResultSet get(int id) 
			throws SQLException, IndexOutOfBoundsException;
	abstract public void insert(ResultSet data) 
			throws SQLException;
	abstract public void update(int id, ResultSet data)
			throws SQLException, IndexOutOfBoundsException;
	abstract public void delete(int id) 
			throws SQLException, IndexOutOfBoundsException;
	
	protected Connection getConnection() {return this.conn;} 
	protected String getTableName() {return this.table;}
	
	public void close() throws SQLException {
		if (!conn.isClosed()) {
			conn.rollback();
			conn.close();
		}
	}
}
