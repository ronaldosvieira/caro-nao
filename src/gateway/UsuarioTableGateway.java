package gateway;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioTableGateway extends TableGateway {

	public UsuarioTableGateway() throws ClassNotFoundException, SQLException {
		super("usuario");
	}

	public ResultSet obterTodos() throws SQLException {
		ResultSet rs = null;
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.select);
		
		stmt.setString(1, this.getTableName());
		
		if (stmt.execute()) rs = stmt.getResultSet();
		
		return rs;
	}

	public ResultSet obter(int id) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.selectId);
		
		stmt.setString(1, this.getTableName());
		stmt.setInt(2, id);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		return rs;
	}

	public void inserir(String nome, String email, String telefone) throws SQLException {
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(this.insert);
		
		stmt.setString(1, this.getTableName());
		stmt.setString(2, "nome, email, telefone");
		
		StringBuilder data = new StringBuilder();
		
		data.append(nome); data.append(", ");
		data.append(email); data.append(", ");
		data.append(telefone);
		
		stmt.setString(3, data.toString());
		
		stmt.executeUpdate();
	}

	public void atualizar(int id, String nome, String email, String telefone) 
			throws SQLException, IndexOutOfBoundsException {
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(this.updateId);
		
		stmt.setString(1, this.getTableName());
		
		StringBuilder data = new StringBuilder();
		
		data.append("nome = "); data.append(nome); data.append(", ");
		data.append("email = "); data.append(email); data.append(", ");
		data.append("telefone = "); data.append(telefone);
		
		stmt.setString(2, data.toString());
		stmt.setInt(3, id);
		
		stmt.executeUpdate();
	}

	public void excluir(int id) throws SQLException, IndexOutOfBoundsException {
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(this.deleteId);
		
		stmt.setString(1, this.getTableName());
		stmt.setInt(2, id);
		
		stmt.executeUpdate();
	}
}
