package dados;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.rowset.CachedRowSet;

import com.sun.rowset.CachedRowSetImpl;

public class GrupoTableGateway extends TableGateway {

	public GrupoTableGateway() throws ClassNotFoundException, SQLException {
		super("grupo");
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

	public void inserir(String nome, String descricao, String regras, int limite, boolean ativo) 
			throws SQLException {
		String sql = String.format(this.insert, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setString(1, "nome, descricao, regras, "
				+ "limite_avaliacoes_negativas, ativo");
		
		StringBuilder data = new StringBuilder();
		
		data.append(nome); data.append(", ");
		data.append(descricao); data.append(", ");
		data.append(regras); data.append(", ");
		data.append(limite); data.append(", ");
		data.append(ativo);
		
		stmt.setString(2, data.toString());
		
		stmt.executeUpdate();
	}

	public void atualizar(int id, String nome, String descricao, 
			String regras, int limite, boolean ativo) 
			throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.updateId, this.getTableName());
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);
		
		StringBuilder data = new StringBuilder();
		
		data.append("nome = "); data.append(nome); data.append(", ");
		data.append("descricao = "); data.append(descricao); data.append(", ");
		data.append("regras = "); data.append(regras); data.append(", ");
		data.append("limite = "); data.append(limite); data.append(", ");
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
