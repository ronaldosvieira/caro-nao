package dados;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;

public class AvaliacaoTableGateway extends TableGateway {

	public AvaliacaoTableGateway() throws ClassNotFoundException, SQLException {
		super("avaliacao");
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
		
	public CachedRowSet obterPorAvaliador(int idAvaliador) throws SQLException, IndexOutOfBoundsException {
		CachedRowSet crs = new CachedRowSetImpl();
		String sql = String.format(this.selectId, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setString(1, "avaliador_id");
		stmt.setInt(2, idAvaliador);
		
		if (stmt.execute()) crs.populate(stmt.getResultSet());
		else throw new IndexOutOfBoundsException();
		
		return crs;
	}
	
	public CachedRowSet obterPorAvaliado(int idAvaliado) throws SQLException, IndexOutOfBoundsException {
		CachedRowSet crs = new CachedRowSetImpl();
		String sql = String.format(this.selectColumn, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setString(1, "avaliado_id");
		stmt.setInt(2, idAvaliado);
		
		if (stmt.execute()) crs.populate(stmt.getResultSet());
		else throw new IndexOutOfBoundsException();
		
		return crs;
	}

	public void inserir(int idAvaliador, int idAvaliado, int nota) 
			throws SQLException {
		String sql = String.format(this.insert, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setString(1, "avaliador_id, avaliado_id, nota");
		
		StringBuilder data = new StringBuilder();
		
		data.append(idAvaliador); data.append(", ");
		data.append(idAvaliado); data.append(", ");
		data.append(nota);
		
		stmt.setString(2, data.toString());
		
		stmt.executeUpdate();
	}

	public void atualizar(int id, int idAvaliador, 
			int idAvaliado, int nota) 
			throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.updateId, this.getTableName());
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);
		
		StringBuilder data = new StringBuilder();
		
		data.append("avaliador_id = "); data.append(idAvaliador); data.append(", ");
		data.append("avaliado_id = "); data.append(idAvaliado); data.append(", ");
		data.append("nota = "); data.append(nota);
		
		stmt.setString(1, data.toString());
		stmt.setInt(2, id);
		
		stmt.executeUpdate();
	}

	public void excluir(int id) throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.deleteId, this.getTableName());
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, id);
		
		stmt.executeUpdate();
	}
}
