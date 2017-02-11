package dados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioTableGateway extends TableGateway {

	public UsuarioTableGateway() throws ClassNotFoundException, SQLException {
		super("usuario");
	}

	public ResultSet obterTodos() throws SQLException {
		ResultSet rs = null;
		String sql = String.format(this.select, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		
		return rs;
	}

	public ResultSet obter(int id) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		String sql = String.format(this.selectId, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, id);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		return rs;
	}

	public void inserir(String nome, String email, String telefone) throws SQLException {
		String sql = String.format(this.insert, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.insert);
		
		stmt.setString(1, "nome, email, telefone");
		
		StringBuilder data = new StringBuilder();
		
		data.append(nome); data.append(", ");
		data.append(email); data.append(", ");
		data.append(telefone);
		
		stmt.setString(2, data.toString());
		
		stmt.executeUpdate();
	}

	public void atualizar(int id, String nome, String email, String telefone) 
			throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.updateId, this.getTableName());
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);
		
		StringBuilder data = new StringBuilder();
		
		data.append("nome = "); data.append(nome); data.append(", ");
		data.append("email = "); data.append(email); data.append(", ");
		data.append("telefone = "); data.append(telefone);
		
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
