package dados;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;

public class EstadoCaronaTableGateway extends TableGateway {

	public EstadoCaronaTableGateway() throws ClassNotFoundException, SQLException {
		super("estado_carona");
	}

	public CachedRowSet obterTodos() throws SQLException {
		CachedRowSet crs = new CachedRowSetImpl();
		String sql = String.format(this.select, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		if (stmt.execute()) crs.populate(stmt.getResultSet());
		
		return crs;
	}

	public CachedRowSet obter(int id) throws SQLException, IndexOutOfBoundsException {
		CachedRowSet crs = new CachedRowSetImpl();
		String sql = String.format(this.selectId, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, id);
		
		if (stmt.execute()) crs.populate(stmt.getResultSet());
		else throw new IndexOutOfBoundsException();
		
		return crs;
	}
}
