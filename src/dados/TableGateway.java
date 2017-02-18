package dados;

import java.sql.Connection;
import java.sql.SQLException;

import util.ConnectionFactory;

public abstract class TableGateway implements AutoCloseable {
	private Connection conn;
	private String table;
	
	protected String select = "select * from %s;";
	protected String selectId = "select * from %s where id = ?;";
	protected String selectColumn = "select * from %s where %s = ?;";
	protected String selectMany = "select * from %s where %s;";
	protected String insert = "insert into %s (%s) values (%s);";
	protected String insertMany = "insert into %s (?) values ?;";
	protected String updateId = "update %s set %s where id = ?;";
	protected String updateMany = "update %s set %s where %s;";
	protected String deleteId = "delete from %s where id = ?;";
	protected String deleteMany = "delete from %s where %s;";
	
	public TableGateway(String table) 
			throws ClassNotFoundException, SQLException {
		this.conn = ConnectionFactory.getConnection();
		this.table = table;
	}

	protected Connection getConnection() {
		try {
			if (conn.isClosed()) conn = ConnectionFactory.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return this.conn;
	} 
	protected String getTableName() {return this.table;}
	
	public void close() throws SQLException {
		if (!conn.isClosed()) {
			conn.rollback();
			conn.close();
		}
	}
}
