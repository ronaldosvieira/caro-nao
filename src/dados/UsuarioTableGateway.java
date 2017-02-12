package dados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.RecordSet;
import util.Row;

public class UsuarioTableGateway extends TableGateway {

	public UsuarioTableGateway() throws ClassNotFoundException, SQLException {
		super("usuario");
	}

	public RecordSet obterTodos() throws SQLException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.select, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("id", rs.getInt("id"));
			row.put("nome", rs.getString("nome"));
			row.put("email", rs.getString("email"));
			row.put("telefone", rs.getString("telefone"));
			
			dataset.add(row);
		}
		
		return dataset;
	}

	public RecordSet obter(int id) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.selectId, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setInt(1, id);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("id", rs.getInt("id"));
			row.put("nome", rs.getString("nome"));
			row.put("email", rs.getString("email"));
			row.put("telefone", rs.getString("telefone"));
			
			dataset.add(row);
		}
		
		return dataset;
	}

	public RecordSet obterPeloEmail(String email) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.selectColumn, 
				this.getTableName(), "email");
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setString(1, email);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("id", rs.getInt("id"));
			row.put("nome", rs.getString("nome"));
			row.put("email", rs.getString("email"));
			row.put("telefone", rs.getString("telefone"));
			
			dataset.add(row);
		}
		
		return dataset;
	}

	
	public int inserir(String nome, String email, String telefone) throws SQLException {
		String sql = String.format(this.insert, this.getTableName());
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(
					sql, 
					Statement.RETURN_GENERATED_KEYS);
		
		stmt.setString(1, "nome, email, telefone");
		
		StringBuilder data = new StringBuilder();
		
		data.append(nome); data.append(", ");
		data.append(email); data.append(", ");
		data.append(telefone);
		
		stmt.setString(2, data.toString());
		
		int affectedRows = stmt.executeUpdate();
		
		if (affectedRows == 0) {
            throw new SQLException("Erro ao inserir usuário.");
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Erro ao inserir usuário.");
            }
        }
	}

	public void atualizar(int id, String nome, String email, String telefone) 
			throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.updateId, 
				this.getTableName(), "nome = ?, email = ?, telefone = ?");
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);
		
		stmt.setString(1, nome);
		stmt.setString(2, email);
		stmt.setString(3, telefone);
		stmt.setInt(4, id);
		
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
