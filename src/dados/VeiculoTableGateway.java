package dados;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;

public class VeiculoTableGateway extends TableGateway {

	public VeiculoTableGateway() throws ClassNotFoundException, SQLException {
		super("veiculo");
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
	
	public CachedRowSet obterPorUsuario(int idUsuario) throws SQLException, IndexOutOfBoundsException {
		CachedRowSet crs = new CachedRowSetImpl();
		String sql = String.format(this.selectColumn, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setString(1, "usuario_id");
		stmt.setInt(2, idUsuario);
		
		if (stmt.execute()) crs.populate(stmt.getResultSet());
		else throw new IndexOutOfBoundsException();
		
		return crs;
	}

	public void inserir(String modelo, String placa, int vagas, int idUsuario, boolean ativo) 
			throws SQLException {
		String sql = String.format(this.insert, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setString(1, "modelo, placa, vagas, usuario_id, ativo");
		
		StringBuilder data = new StringBuilder();
		
		data.append(modelo); data.append(", ");
		data.append(placa); data.append(", ");
		data.append(vagas); data.append(", ");
		data.append(idUsuario); data.append(", ");
		data.append(ativo);
		
		stmt.setString(2, data.toString());
		
		stmt.executeUpdate();
	}

	public void atualizar(int id, String modelo, String placa, int vagas, 
			int idUsuario, boolean ativo) 
			throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.updateId, this.getTableName());
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);
		
		StringBuilder data = new StringBuilder();
		
		data.append("modelo = "); data.append(modelo); data.append(", ");
		data.append("placa = "); data.append(placa); data.append(", ");
		data.append("vagas = "); data.append(vagas); data.append(", ");
		data.append("idUsuario = "); data.append(idUsuario); data.append(", ");
		data.append("ativo = "); data.append(ativo);
		
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
