package dados;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import util.RecordSet;
import util.Row;

public class LogradouroTableGateway extends TableGateway {

	public LogradouroTableGateway() throws ClassNotFoundException, SQLException {
		super("logradouro");
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
			row.put("cep", rs.getString("cep"));
			row.put("estado", rs.getString("estado"));
			row.put("cidade", rs.getString("cidade"));
			row.put("distrito", rs.getString("distrito"));
			row.put("endereco", rs.getString("endereco"));
			row.put("numero", rs.getString("numero"));
			
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
			row.put("cep", rs.getString("cep"));
			row.put("estado", rs.getString("estado"));
			row.put("cidade", rs.getString("cidade"));
			row.put("distrito", rs.getString("distrito"));
			row.put("endereco", rs.getString("endereco"));
			row.put("numero", rs.getString("numero"));
			
			dataset.add(row);
		}
		
		return dataset;
	}
	
	public RecordSet obterPorCEP(String cep) throws SQLException, IndexOutOfBoundsException {
		ResultSet rs = null;
		RecordSet dataset = new RecordSet();
		String sql = String.format(this.selectColumn, 
				this.getTableName(), "cep");
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(sql);
		
		stmt.setString(1, cep);
		
		if (stmt.execute()) rs = stmt.getResultSet();
		else throw new IndexOutOfBoundsException();
		
		while (rs.next()) {
			Row row = new Row();
			
			row.put("id", rs.getInt("id"));
			row.put("cep", rs.getString("cep"));
			row.put("estado", rs.getString("estado"));
			row.put("cidade", rs.getString("cidade"));
			row.put("distrito", rs.getString("distrito"));
			row.put("endereco", rs.getString("endereco"));
			row.put("numero", rs.getString("numero"));
			
			dataset.add(row);
		}
		
		return dataset;
	}

	public int inserir(String cep, String estado, String cidade,
			String distrito, String endereco, String numero) 
			throws SQLException {
		String sql = String.format(this.insert, 
				this.getTableName(),
				"cep, estado, cidade, distrito, endereco, numero",
				"?, ?, ?, ?, ?, ?");
		PreparedStatement stmt = 
			this.getConnection().prepareStatement(
					sql, Statement.RETURN_GENERATED_KEYS);

		stmt.setString(1, cep);
		stmt.setString(2, estado);
		stmt.setString(3, cidade);
		stmt.setString(4, distrito);
		stmt.setString(5, endereco);
		stmt.setString(6, numero);
		
		int affectedRows = stmt.executeUpdate();
		
		if (affectedRows == 0) {
            throw new SQLException("Erro ao inserir logradouro.");
        }

        try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Erro ao inserir logradouro.");
            }
        }
	}

	public void atualizar(int id, String cep, String estado, 
			String cidade, String distrito, String endereco, 
			String numero) 
			throws SQLException, IndexOutOfBoundsException {
		String sql = String.format(this.updateId, 
				this.getTableName(),
				"cep = ?, estado = ?, cidade = ?, distrito = ?, "
				+ "endereco = ?, numero = ?");
		PreparedStatement stmt = 
				this.getConnection().prepareStatement(sql);

		stmt.setString(1, cep);
		stmt.setString(2, estado);
		stmt.setString(3, cidade);
		stmt.setString(4, distrito);
		stmt.setString(5, endereco);
		stmt.setString(6, numero);
		stmt.setInt(7, id);
		
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
